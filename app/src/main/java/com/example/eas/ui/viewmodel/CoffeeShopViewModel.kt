package com.example.eas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eas.data.entity.CartItem
import com.example.eas.data.entity.MembershipSettings
import com.example.eas.data.entity.Order
import com.example.eas.data.entity.OrderStatus
import com.example.eas.data.entity.OrderType
import com.example.eas.data.entity.PaymentMethod
import com.example.eas.data.entity.Product
import com.example.eas.data.entity.RewardEntity
import com.example.eas.data.entity.User
import com.example.eas.data.entity.UserRole
import com.example.eas.data.repository.CoffeeShopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AppEvent {
    data class Message(val text: String) : AppEvent()
    data class OrderCreated(val orderId: Int) : AppEvent()
}

class CoffeeShopViewModel(private val repository: CoffeeShopRepository) : ViewModel() {

    private val _bootstrapped = MutableStateFlow(false)
    val bootstrapped: StateFlow<Boolean> = _bootstrapped

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _events = MutableSharedFlow<AppEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<AppEvent> = _events

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredProducts: StateFlow<List<Product>> = repository.featuredProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val users: StateFlow<List<User>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rewards: StateFlow<List<RewardEntity>> = repository.allRewards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<MembershipSettings?> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            repository.ensureSeedData()
            _currentUser.value = repository.activeUser()
            _bootstrapped.value = true
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = repository.login(email, password)
            if (user == null) {
                _events.emit(AppEvent.Message("Login gagal. Periksa email dan password."))
            } else {
                _currentUser.value = user
                _events.emit(AppEvent.Message("Selamat datang, ${user.name}."))
            }
        }
    }

    fun quickLoginAdmin() = login("admin@cb.com", "admin")

    fun quickLoginTestUser() = login("user@cb.com", "1234")

    fun register(name: String, email: String, phone: String, password: String) {
        if (name.isBlank() || email.isBlank() || phone.isBlank() || password.length < 4) {
            _events.tryEmit(AppEvent.Message("Lengkapi data. Password minimal 4 karakter."))
            return
        }
        viewModelScope.launch {
            repository.register(name, email, phone, password)
                .onSuccess {
                    _currentUser.value = it
                    _events.emit(AppEvent.Message("Akun berhasil dibuat."))
                }
                .onFailure {
                    _events.emit(AppEvent.Message("Email sudah digunakan."))
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _currentUser.value = null
        }
    }

    fun observeProduct(productId: Int): Flow<Product?> = repository.observeProduct(productId)

    fun observeCart(userId: Int): Flow<List<CartItem>> = repository.observeCart(userId)

    fun observeCustomerOrders(userId: Int): Flow<List<Order>> = repository.observeOrders(userId)

    fun observeOrder(orderId: Int) = repository.observeOrder(orderId)

    fun observeOrderItems(orderId: Int) = repository.observeOrderItems(orderId)

    fun observeUser(userId: Int): Flow<User?> = repository.observeUser(userId)

    fun addToCart(productId: Int, quantity: Int, orderType: OrderType, notes: String?) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.addToCart(user.id, productId, quantity, orderType, notes)
            _events.emit(AppEvent.Message("Produk ditambahkan ke keranjang."))
        }
    }

    fun updateCartQuantity(item: CartItem, quantity: Int) {
        viewModelScope.launch { repository.updateCartQuantity(item, quantity) }
    }

    fun removeCartItem(itemId: Int) {
        viewModelScope.launch { repository.removeCartItem(itemId) }
    }

    fun checkout(orderType: OrderType, paymentMethod: PaymentMethod, tableNumber: String?, notes: String?) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val orderId = repository.checkout(user.id, orderType, paymentMethod, tableNumber, notes)
            if (orderId == null) {
                _events.emit(AppEvent.Message("Keranjang masih kosong."))
            } else {
                _currentUser.value = repository.activeUser()
                _events.emit(AppEvent.OrderCreated(orderId))
            }
        }
    }

    fun redeemReward(rewardId: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val success = repository.redeemReward(user.id, rewardId)
            _currentUser.value = repository.activeUser()
            _events.emit(AppEvent.Message(if (success) "Reward berhasil ditukar." else "Poin tidak cukup."))
        }
    }

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            repository.saveProduct(product)
            _events.emit(AppEvent.Message("Produk tersimpan."))
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
            _events.emit(AppEvent.Message("Produk dinonaktifkan."))
        }
    }

    fun saveReward(reward: RewardEntity) {
        viewModelScope.launch {
            repository.saveReward(reward)
            _events.emit(AppEvent.Message("Reward tersimpan."))
        }
    }

    fun saveSettings(settings: MembershipSettings) {
        viewModelScope.launch {
            repository.saveSettings(settings)
            _events.emit(AppEvent.Message("Pengaturan membership tersimpan."))
        }
    }

    fun adjustUserPoints(userId: Int, delta: Int, reason: String) {
        val admin = _currentUser.value ?: return
        viewModelScope.launch {
            repository.adjustUserPoints(userId, admin.id, delta, reason.ifBlank { "Penyesuaian admin" })
            _events.emit(AppEvent.Message("Poin pengguna disesuaikan."))
        }
    }

    fun setUserActive(userId: Int, isActive: Boolean) {
        viewModelScope.launch { repository.setUserActive(userId, isActive) }
    }

    fun updateOrderStatus(orderId: Int, status: OrderStatus) {
        viewModelScope.launch { repository.updateOrderStatus(orderId, status) }
    }

    fun updateProfile(name: String, email: String, phone: String, newPassword: String?) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val result = repository.updateProfile(user.id, name, email, phone, newPassword)
            if (result.isSuccess) {
                _events.emit(AppEvent.Message("Profil berhasil diperbarui."))
            } else {
                val msg = when (result.exceptionOrNull()?.message) {
                    "EMAIL_EXISTS" -> "Email sudah digunakan akun lain."
                    else -> "Gagal memperbarui profil."
                }
                _events.emit(AppEvent.Message(msg))
            }
        }
    }

    fun sendForgotPasswordNotice() {
        _events.tryEmit(AppEvent.Message("Permintaan terkirim. Hubungi admin untuk reset password."))
    }

    fun isAdmin(): Boolean = _currentUser.value?.role == UserRole.ADMIN.name

    companion object {
        fun factory(repository: CoffeeShopRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CoffeeShopViewModel(repository) as T
                }
            }
        }
    }
}
