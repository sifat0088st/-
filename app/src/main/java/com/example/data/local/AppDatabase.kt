package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.*
import com.example.data.local.entities.*

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        BrandEntity::class,
        BannerEntity::class,
        CouponEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        CartItemEntity::class,
        WishlistItemEntity::class,
        ReviewEntity::class,
        StoreSettingsEntity::class,
        ReturnRequestEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun brandDao(): BrandDao
    abstract fun bannerDao(): BannerDao
    abstract fun couponDao(): CouponDao
    abstract fun orderDao(): OrderDao
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun reviewDao(): ReviewDao
    abstract fun settingsDao(): SettingsDao
    abstract fun returnRequestDao(): ReturnRequestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "titas_fashion_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
