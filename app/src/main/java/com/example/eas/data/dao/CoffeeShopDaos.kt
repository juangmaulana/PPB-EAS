package com.example.eas.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.eas.data.entity.CartItem
import com.example.eas.data.entity.MembershipSettings
import com.example.eas.data.entity.Order
import com.example.eas.data.entity.OrderItem
import com.example.eas.data.entity.PointAdjustment
import com.example.eas.data.entity.Product
import com.example.eas.data.entity.RewardEntity
import com.example.eas.data.entity.Session
import com.example.eas.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id")
    fun observeById(id: Int): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Int): User?

    @Query("SELECT * FROM users ORDER BY role ASC, name ASC")
    fun observeAll(): Flow<List<User>>

    @Query("UPDATE users SET points = points + :delta WHERE id = :userId")
    suspend fun adjustPoints(userId: Int, delta: Int): Int

    @Query("UPDATE users SET points = points - :cost WHERE id = :userId AND points >= :cost")
    suspend fun deductPointsIfEnough(userId: Int, cost: Int): Int

    @Query("UPDATE users SET isActive = :isActive WHERE id = :userId")
    suspend fun setActive(userId: Int, isActive: Boolean)
}

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: Session): Long

    @Query("UPDATE sessions SET isActive = 0")
    suspend fun clear()

    @Query("SELECT * FROM sessions WHERE isActive = 1 ORDER BY id DESC LIMIT 1")
    suspend fun active(): Session?
}

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product): Long

    @Update
    suspend fun update(product: Product)

    @Query("SELECT COUNT(*) FROM products WHERE isDeleted = 0")
    suspend fun count(): Int

    @Query("SELECT * FROM products WHERE isDeleted = 0 ORDER BY isFeatured DESC, name ASC")
    fun observeAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isDeleted = 0 AND isFeatured = 1 ORDER BY name ASC")
    fun observeFeatured(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id AND isDeleted = 0")
    fun observeById(id: Int): Flow<Product?>

    @Query("SELECT * FROM products WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: Int): Product?

    @Query("UPDATE products SET isDeleted = 1, isAvailable = 0 WHERE id = :id")
    suspend fun softDelete(id: Int)
}

@Dao
interface CartDao {
    @Insert
    suspend fun insert(item: CartItem): Long

    @Update
    suspend fun update(item: CartItem)

    @Query("SELECT * FROM cart_items WHERE userId = :userId ORDER BY id ASC")
    fun observeForUser(userId: Int): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    suspend fun getForUser(userId: Int): List<CartItem>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId AND orderType = :orderType LIMIT 1")
    suspend fun find(userId: Int, productId: Int, orderType: String): CartItem?

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearForUser(userId: Int)
}

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: Order): Long

    @Insert
    suspend fun insertItems(items: List<OrderItem>)

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY id DESC")
    fun observeForUser(userId: Int): Flow<List<Order>>

    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun observeAll(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun observeOrder(orderId: Int): Flow<Order?>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId ORDER BY id ASC")
    fun observeItems(orderId: Int): Flow<List<OrderItem>>

    @Query("UPDATE orders SET orderStatus = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: String)

    @Query("UPDATE orders SET paymentStatus = :status WHERE id = :orderId")
    suspend fun updatePaymentStatus(orderId: Int, status: String)

    @Query("SELECT COUNT(*) FROM orders")
    fun observeCount(): Flow<Int>
}

@Dao
interface RewardDao {
    @Insert
    suspend fun insert(reward: RewardEntity): Long

    @Update
    suspend fun update(reward: RewardEntity)

    @Query("SELECT COUNT(*) FROM rewards")
    suspend fun count(): Int

    @Query("SELECT * FROM rewards ORDER BY pointCost ASC")
    fun observeAll(): Flow<List<RewardEntity>>

    @Query("SELECT * FROM rewards WHERE id = :id")
    suspend fun getById(id: Int): RewardEntity?

    @Query("DELETE FROM rewards WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface MembershipDao {
    @Insert
    suspend fun insert(settings: MembershipSettings): Long

    @Update
    suspend fun update(settings: MembershipSettings)

    @Query("SELECT * FROM membership_settings ORDER BY id DESC LIMIT 1")
    fun observe(): Flow<MembershipSettings?>

    @Query("SELECT * FROM membership_settings ORDER BY id DESC LIMIT 1")
    suspend fun get(): MembershipSettings?
}

@Dao
interface PointAdjustmentDao {
    @Insert
    suspend fun insert(adjustment: PointAdjustment): Long

    @Query("SELECT * FROM point_adjustments WHERE userId = :userId ORDER BY id DESC")
    fun observeForUser(userId: Int): Flow<List<PointAdjustment>>
}
