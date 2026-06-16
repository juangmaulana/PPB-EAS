package com.example.eas.data.repository

import androidx.room.withTransaction
import com.example.eas.data.AppDatabase
import com.example.eas.data.entity.CartItem
import com.example.eas.data.entity.MembershipSettings
import com.example.eas.data.entity.Order
import com.example.eas.data.entity.OrderItem
import com.example.eas.data.entity.OrderStatus
import com.example.eas.data.entity.OrderType
import com.example.eas.data.entity.PaymentMethod
import com.example.eas.data.entity.PaymentStatus
import com.example.eas.data.entity.PointAdjustment
import com.example.eas.data.entity.Product
import com.example.eas.data.entity.RewardEntity
import com.example.eas.data.entity.Session
import com.example.eas.data.entity.User
import com.example.eas.data.entity.UserRole
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

class CoffeeShopRepository(private val database: AppDatabase) {

    private val users = database.userDao()
    private val sessions = database.sessionDao()
    private val products = database.productDao()
    private val cart = database.cartDao()
    private val orders = database.orderDao()
    private val rewards = database.rewardDao()
    private val membership = database.membershipDao()
    private val pointAdjustments = database.pointAdjustmentDao()

    val allProducts: Flow<List<Product>> = products.observeAll()
    val featuredProducts: Flow<List<Product>> = products.observeFeatured()
    val allUsers: Flow<List<User>> = users.observeAll()
    val allOrders: Flow<List<Order>> = orders.observeAll()
    val allRewards: Flow<List<RewardEntity>> = rewards.observeAll()
    val settings: Flow<MembershipSettings?> = membership.observe()

    suspend fun ensureSeedData() {
        database.withTransaction {
            seedUsers()
            seedProducts()
            seedRewards()
            seedSettings()
        }
    }

    suspend fun activeUser(): User? {
        val session = sessions.active() ?: return null
        return users.getById(session.userId)?.takeIf { it.isActive }
    }

    suspend fun login(email: String, password: String): User? {
        val user = users.getByEmail(email.trim().lowercase()) ?: return null
        if (!user.isActive || user.passwordHash != hashPassword(password)) return null
        sessions.clear()
        sessions.insert(Session(userId = user.id, createdAt = now()))
        return user
    }

    suspend fun register(name: String, email: String, phone: String, password: String): Result<User> {
        val normalizedEmail = email.trim().lowercase()
        if (users.getByEmail(normalizedEmail) != null) return Result.failure(IllegalArgumentException("EMAIL_EXISTS"))
        val userId = users.insert(
            User(
                name = name.trim(),
                email = normalizedEmail,
                phone = phone.trim(),
                passwordHash = hashPassword(password),
                role = UserRole.CUSTOMER.name,
                points = 0,
                createdAt = now()
            )
        ).toInt()
        val user = users.getById(userId) ?: return Result.failure(IllegalStateException("REGISTER_FAILED"))
        sessions.clear()
        sessions.insert(Session(userId = user.id, createdAt = now()))
        return Result.success(user)
    }

    suspend fun logout() {
        sessions.clear()
    }

    fun observeUser(userId: Int): Flow<User?> = users.observeById(userId)

    fun observeCart(userId: Int): Flow<List<CartItem>> = cart.observeForUser(userId)

    fun observeOrders(userId: Int): Flow<List<Order>> = orders.observeForUser(userId)

    fun observeOrder(orderId: Int): Flow<Order?> = orders.observeOrder(orderId)

    fun observeOrderItems(orderId: Int): Flow<List<OrderItem>> = orders.observeItems(orderId)

    fun observeProduct(productId: Int): Flow<Product?> = products.observeById(productId)

    suspend fun addToCart(userId: Int, productId: Int, quantity: Int, orderType: OrderType, notes: String?) {
        val product = products.getById(productId) ?: return
        if (!product.isAvailable) return
        val safeQuantity = max(1, quantity)
        val existing = cart.find(userId, productId, orderType.name)
        if (existing == null) {
            cart.insert(CartItem(userId = userId, productId = productId, quantity = safeQuantity, orderType = orderType.name, notes = notes))
        } else {
            cart.update(existing.copy(quantity = existing.quantity + safeQuantity, notes = notes ?: existing.notes))
        }
    }

    suspend fun updateCartQuantity(item: CartItem, quantity: Int) {
        if (quantity <= 0) {
            cart.deleteById(item.id)
        } else {
            cart.update(item.copy(quantity = quantity))
        }
    }

    suspend fun removeCartItem(itemId: Int) {
        cart.deleteById(itemId)
    }

    suspend fun checkout(
        userId: Int,
        orderType: OrderType,
        paymentMethod: PaymentMethod,
        tableNumber: String?,
        notes: String?
    ): Int? {
        return database.withTransaction {
            val items = cart.getForUser(userId)
            if (items.isEmpty()) return@withTransaction null
            val productMap = items.mapNotNull { item -> products.getById(item.productId)?.let { item to it } }
            if (productMap.isEmpty()) return@withTransaction null
            val subtotal = productMap.sumOf { (item, product) -> product.price * item.quantity }
            val settings = membership.get() ?: defaultSettings()
            val pointsEarned = (subtotal / settings.rupiahPerPoint).toInt()
            val orderId = orders.insertOrder(
                Order(
                    userId = userId,
                    orderNumber = "CB-${System.currentTimeMillis()}",
                    orderType = orderType.name,
                    tableNumber = tableNumber?.takeIf { it.isNotBlank() },
                    notes = notes?.takeIf { it.isNotBlank() },
                    subtotal = subtotal,
                    total = subtotal,
                    pointsEarned = pointsEarned,
                    orderStatus = OrderStatus.RECEIVED.name,
                    paymentStatus = PaymentStatus.PAID.name,
                    paymentMethod = paymentMethod.name,
                    createdAt = now()
                )
            ).toInt()
            orders.insertItems(
                productMap.map { (item, product) ->
                    OrderItem(
                        orderId = orderId,
                        productId = product.id,
                        productNameSnapshot = product.name,
                        productPriceSnapshot = product.price,
                        quantity = item.quantity,
                        lineTotal = product.price * item.quantity
                    )
                }
            )
            users.adjustPoints(userId, pointsEarned)
            cart.clearForUser(userId)
            orderId
        }
    }

    suspend fun redeemReward(userId: Int, rewardId: Int): Boolean {
        val reward = rewards.getById(rewardId) ?: return false
        if (!reward.isAvailable) return false
        return database.withTransaction {
            val success = users.deductPointsIfEnough(userId, reward.pointCost) == 1
            if (success) {
                val orderId = orders.insertOrder(
                    Order(
                        userId = userId,
                        orderNumber = "RW-${System.currentTimeMillis()}",
                        orderType = OrderType.REWARD_REDEEM.name,
                        subtotal = 0L,
                        total = 0L,
                        pointsEarned = -reward.pointCost,
                        orderStatus = OrderStatus.COMPLETED.name,
                        paymentStatus = PaymentStatus.PAID.name,
                        paymentMethod = PaymentMethod.POINTS.name,
                        notes = "Redeem reward: ${reward.name}",
                        createdAt = now()
                    )
                ).toInt()
                orders.insertItems(
                    listOf(
                        OrderItem(
                            orderId = orderId,
                            productId = 0,
                            productNameSnapshot = reward.name,
                            productPriceSnapshot = 0L,
                            quantity = 1,
                            lineTotal = 0L
                        )
                    )
                )
            }
            success
        }
    }

    suspend fun saveProduct(product: Product) {
        if (product.id == 0) products.insert(product.copy(updatedAt = now())) else products.update(product.copy(updatedAt = now()))
    }

    suspend fun deleteProduct(productId: Int) {
        products.softDelete(productId)
    }

    suspend fun saveReward(reward: RewardEntity) {
        if (reward.id == 0) rewards.insert(reward) else rewards.update(reward)
    }

    suspend fun deleteReward(rewardId: Int) {
        rewards.deleteById(rewardId)
    }

    suspend fun saveSettings(settings: MembershipSettings) {
        if (settings.id == 0) membership.insert(settings.copy(updatedAt = now())) else membership.update(settings.copy(updatedAt = now()))
    }

    suspend fun adjustUserPoints(userId: Int, adminId: Int, delta: Int, reason: String) {
        database.withTransaction {
            users.adjustPoints(userId, delta)
            pointAdjustments.insert(PointAdjustment(userId = userId, adminId = adminId, delta = delta, reason = reason, createdAt = now()))
        }
    }

    suspend fun setUserActive(userId: Int, isActive: Boolean) {
        users.setActive(userId, isActive)
    }

    suspend fun updateProfile(userId: Int, name: String, email: String, phone: String, newPassword: String?): Result<Unit> {
        val user = users.getById(userId) ?: return Result.failure(IllegalStateException("USER_NOT_FOUND"))
        val normalizedEmail = email.trim().lowercase()
        val existing = users.getByEmail(normalizedEmail)
        if (existing != null && existing.id != userId) return Result.failure(IllegalArgumentException("EMAIL_EXISTS"))
        val updatedHash = if (!newPassword.isNullOrBlank()) hashPassword(newPassword) else user.passwordHash
        users.update(user.copy(name = name.trim(), email = normalizedEmail, phone = phone.trim(), passwordHash = updatedHash))
        return Result.success(Unit)
    }

    suspend fun updateOrderStatus(orderId: Int, status: OrderStatus) {
        orders.updateOrderStatus(orderId, status.name)
    }

    suspend fun updatePaymentStatus(orderId: Int, status: PaymentStatus) {
        orders.updatePaymentStatus(orderId, status.name)
    }

    private suspend fun seedUsers() {
        if (users.getByEmail("admin@cb.com") == null) {
            users.insert(User(name = "Admin Coffee Bliss", email = "admin@cb.com", phone = "000", passwordHash = hashPassword("admin"), role = UserRole.ADMIN.name, createdAt = now()))
        }
        if (users.getByEmail("user@cb.com") == null) {
            users.insert(User(name = "Test", email = "user@cb.com", phone = "081234567890", passwordHash = hashPassword("1234"), role = UserRole.CUSTOMER.name, points = 1_000_000, createdAt = now()))
        }
    }

    private suspend fun seedProducts() {
        if (products.count() > 0) return
        defaultProducts().forEach { products.insert(it) }
    }

    private suspend fun seedRewards() {
        if (rewards.count() > 0) return
        listOf(
            RewardEntity(name = "Espresso", description = "Tukar poin untuk espresso klasik.", pointCost = 50, imageKey = "reward_espresso"),
            RewardEntity(name = "Cappuccino", description = "Reward cappuccino lembut.", pointCost = 100, imageKey = "reward_cappuccino"),
            RewardEntity(name = "Latte Gratis", description = "Latte gratis untuk member setia.", pointCost = 150, imageKey = "reward_latte")
        ).forEach { rewards.insert(it) }
    }

    private suspend fun seedSettings() {
        if (membership.get() == null) membership.insert(defaultSettings())
    }

    private fun defaultSettings() = MembershipSettings(updatedAt = now())

    private fun defaultProducts(): List<Product> = listOf(
        Product(name = "Espresso", description = "Shot kopi pekat dengan crema tebal dan rasa intens.", category = "Hot Coffee", price = 18_000, imageKey = "product_espresso", isFeatured = true, updatedAt = now()),
        Product(name = "Americano", description = "Espresso dengan air panas, ringan dan bersih.", category = "Hot Coffee", price = 22_000, imageKey = "product_americano", isFeatured = false, updatedAt = now()),
        Product(name = "Cappuccino", description = "Espresso, susu hangat, dan foam lembut.", category = "Hot Coffee", price = 30_000, imageKey = "product_cappuccino", isFeatured = true, updatedAt = now()),
        Product(name = "Cafe Latte", description = "Kopi susu creamy untuk teman kerja santai.", category = "Milk Coffee", price = 32_000, imageKey = "product_cafe_latte", isFeatured = true, updatedAt = now()),
        Product(name = "Caramel Macchiato", description = "Latte berlapis caramel manis dan espresso.", category = "Signature", price = 38_000, imageKey = "product_caramel_macchiato", isFeatured = true, updatedAt = now()),
        Product(name = "Mocha", description = "Kopi, cokelat, dan susu dengan rasa seimbang.", category = "Signature", price = 36_000, imageKey = "product_mocha", isFeatured = false, updatedAt = now()),
        Product(name = "Matcha Latte", description = "Matcha lembut dengan susu, alternatif tanpa kopi.", category = "Non Coffee", price = 34_000, imageKey = "product_matcha_latte", isFeatured = false, updatedAt = now()),
        Product(name = "Cold Brew", description = "Kopi dingin diseduh perlahan, smooth dan segar.", category = "Cold Coffee", price = 35_000, imageKey = "product_cold_brew", isFeatured = true, updatedAt = now()),
        Product(name = "Vanilla Frappe", description = "Minuman blended vanilla yang creamy dan dingin.", category = "Frappe", price = 40_000, imageKey = "product_vanilla_frappe", isFeatured = false, updatedAt = now()),
        Product(name = "Chocolate Croissant", description = "Pastry renyah dengan isi cokelat hangat.", category = "Pastry", price = 28_000, imageKey = "product_chocolate_croissant", isFeatured = false, updatedAt = now())
    )

    private fun hashPassword(password: String): String = password.trim()

    private fun now(): String = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date())
}
