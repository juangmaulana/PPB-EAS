package com.example.eas.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class UserRole { ADMIN, CUSTOMER }
enum class OrderType { DINE_IN, TAKE_AWAY, REWARD_REDEEM }
enum class OrderStatus { RECEIVED, PREPARING, READY, COMPLETED, CANCELLED }
enum class PaymentStatus { PENDING, PAID, FAILED }
enum class PaymentMethod { CASH, QRIS_DEMO, CARD_DEMO, POINTS }

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val passwordHash: String,
    val role: String = UserRole.CUSTOMER.name,
    val points: Int = 0,
    val isActive: Boolean = true,
    val createdAt: String
)

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val createdAt: String,
    val isActive: Boolean = true
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val category: String,
    val price: Long,
    val imageKey: String,
    val isAvailable: Boolean = true,
    val isFeatured: Boolean = false,
    val isDeleted: Boolean = false,
    val updatedAt: String
)

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val productId: Int,
    val quantity: Int,
    val orderType: String,
    val notes: String? = null
)

@Entity(tableName = "orders", indices = [Index(value = ["orderNumber"], unique = true)])
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val orderNumber: String,
    val orderType: String,
    val tableNumber: String? = null,
    val notes: String? = null,
    val subtotal: Long,
    val total: Long,
    val pointsEarned: Int,
    val orderStatus: String,
    val paymentStatus: String,
    val paymentMethod: String,
    val createdAt: String
)

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val productId: Int,
    val productNameSnapshot: String,
    val productPriceSnapshot: Long,
    val quantity: Int,
    val lineTotal: Long
)

@Entity(tableName = "rewards")
data class RewardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val pointCost: Int,
    val imageKey: String? = null,
    val isAvailable: Boolean = true
)

@Entity(tableName = "point_adjustments")
data class PointAdjustment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val adminId: Int,
    val delta: Int,
    val reason: String,
    val createdAt: String
)

@Entity(tableName = "membership_settings")
data class MembershipSettings(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rupiahPerPoint: Long = 10_000L,
    val silverMin: Int = 0,
    val goldMin: Int = 100,
    val platinumMin: Int = 250,
    val updatedAt: String
)
