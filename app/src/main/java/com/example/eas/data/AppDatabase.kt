package com.example.eas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.eas.data.dao.CartDao
import com.example.eas.data.dao.MembershipDao
import com.example.eas.data.dao.OrderDao
import com.example.eas.data.dao.PointAdjustmentDao
import com.example.eas.data.dao.ProductDao
import com.example.eas.data.dao.RewardDao
import com.example.eas.data.dao.SessionDao
import com.example.eas.data.dao.UserDao
import com.example.eas.data.entity.CartItem
import com.example.eas.data.entity.MembershipSettings
import com.example.eas.data.entity.Order
import com.example.eas.data.entity.OrderItem
import com.example.eas.data.entity.PointAdjustment
import com.example.eas.data.entity.Product
import com.example.eas.data.entity.RewardEntity
import com.example.eas.data.entity.Session
import com.example.eas.data.entity.User

@Database(
    entities = [
        User::class,
        Session::class,
        Product::class,
        CartItem::class,
        Order::class,
        OrderItem::class,
        RewardEntity::class,
        PointAdjustment::class,
        MembershipSettings::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun rewardDao(): RewardDao
    abstract fun membershipDao(): MembershipDao
    abstract fun pointAdjustmentDao(): PointAdjustmentDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS transactions_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        memberId INTEGER NOT NULL,
                        amount INTEGER NOT NULL,
                        pointEarned INTEGER NOT NULL,
                        date TEXT NOT NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO transactions_new (id, memberId, amount, pointEarned, date)
                    SELECT id, memberId, CAST(amount AS INTEGER), pointEarned, date
                    FROM transactions
                """.trimIndent())
                db.execSQL("DROP TABLE transactions")
                db.execSQL("ALTER TABLE transactions_new RENAME TO transactions")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        email TEXT NOT NULL,
                        phone TEXT NOT NULL,
                        passwordHash TEXT NOT NULL,
                        role TEXT NOT NULL,
                        points INTEGER NOT NULL,
                        isActive INTEGER NOT NULL,
                        createdAt TEXT NOT NULL
                    )
                """.trimIndent())
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_users_email ON users(email)")
                db.execSQL("""
                    INSERT OR IGNORE INTO users (id, name, email, phone, passwordHash, role, points, isActive, createdAt)
                    SELECT id, name, email, phone, 'legacy123', 'CUSTOMER', points, 1, 'Legacy member'
                    FROM members
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS sessions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId INTEGER NOT NULL,
                        createdAt TEXT NOT NULL,
                        isActive INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS products (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        description TEXT NOT NULL,
                        category TEXT NOT NULL,
                        price INTEGER NOT NULL,
                        imageKey TEXT NOT NULL,
                        isAvailable INTEGER NOT NULL,
                        isFeatured INTEGER NOT NULL,
                        isDeleted INTEGER NOT NULL,
                        updatedAt TEXT NOT NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS cart_items (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId INTEGER NOT NULL,
                        productId INTEGER NOT NULL,
                        quantity INTEGER NOT NULL,
                        orderType TEXT NOT NULL,
                        notes TEXT
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS orders (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId INTEGER NOT NULL,
                        orderNumber TEXT NOT NULL,
                        orderType TEXT NOT NULL,
                        tableNumber TEXT,
                        notes TEXT,
                        subtotal INTEGER NOT NULL,
                        total INTEGER NOT NULL,
                        pointsEarned INTEGER NOT NULL,
                        orderStatus TEXT NOT NULL,
                        paymentStatus TEXT NOT NULL,
                        paymentMethod TEXT NOT NULL,
                        createdAt TEXT NOT NULL
                    )
                """.trimIndent())
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_orders_orderNumber ON orders(orderNumber)")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS order_items (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        orderId INTEGER NOT NULL,
                        productId INTEGER NOT NULL,
                        productNameSnapshot TEXT NOT NULL,
                        productPriceSnapshot INTEGER NOT NULL,
                        quantity INTEGER NOT NULL,
                        lineTotal INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS rewards (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        description TEXT NOT NULL,
                        pointCost INTEGER NOT NULL,
                        imageKey TEXT,
                        isAvailable INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS point_adjustments (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId INTEGER NOT NULL,
                        adminId INTEGER NOT NULL,
                        delta INTEGER NOT NULL,
                        reason TEXT NOT NULL,
                        createdAt TEXT NOT NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS membership_settings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        rupiahPerPoint INTEGER NOT NULL,
                        silverMin INTEGER NOT NULL,
                        goldMin INTEGER NOT NULL,
                        platinumMin INTEGER NOT NULL,
                        updatedAt TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DELETE FROM sessions")
                db.execSQL("DELETE FROM users WHERE email IN ('admin@coffeebliss.local','test@coffeebliss.local')")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS members")
                db.execSQL("DROP TABLE IF EXISTS transactions")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coffee_bliss_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .build()
                    .also { instance = it }
            }
        }
    }
}
