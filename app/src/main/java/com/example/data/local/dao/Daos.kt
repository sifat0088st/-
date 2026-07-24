package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Long): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE isFeatured = 1 ORDER BY id DESC")
    fun getFeaturedProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isFlashSale = 1 ORDER BY id DESC")
    fun getFlashSaleProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY id DESC")
    fun getProductsByCategory(categoryId: Long): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR sku LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductById(id: Long)

    @Query("SELECT COUNT(*) FROM products")
    fun getProductCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM products WHERE stockQuantity <= 3")
    fun getLowStockCount(): Flow<Int>
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Long)
}

@Dao
interface BrandDao {
    @Query("SELECT * FROM brands ORDER BY name ASC")
    fun getAllBrands(): Flow<List<BrandEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: BrandEntity): Long

    @Query("DELETE FROM brands WHERE id = :id")
    suspend fun deleteBrandById(id: Long)
}

@Dao
interface BannerDao {
    @Query("SELECT * FROM banners WHERE isActive = 1 ORDER BY id DESC")
    fun getActiveBanners(): Flow<List<BannerEntity>>

    @Query("SELECT * FROM banners ORDER BY id DESC")
    fun getAllBanners(): Flow<List<BannerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banner: BannerEntity): Long

    @Update
    suspend fun updateBanner(banner: BannerEntity)

    @Query("DELETE FROM banners WHERE id = :id")
    suspend fun deleteBannerById(id: Long)
}

@Dao
interface CouponDao {
    @Query("SELECT * FROM coupons ORDER BY id DESC")
    fun getAllCoupons(): Flow<List<CouponEntity>>

    @Query("SELECT * FROM coupons WHERE code = :code AND isActive = 1 LIMIT 1")
    suspend fun getCouponByCode(code: String): CouponEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: CouponEntity): Long

    @Query("DELETE FROM coupons WHERE id = :id")
    suspend fun deleteCouponById(id: Long)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id")
    fun getOrderById(id: Long): Flow<OrderEntity?>

    @Query("SELECT * FROM orders WHERE orderNumber = :orderNumber LIMIT 1")
    suspend fun getOrderByNumber(orderNumber: String): OrderEntity?

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItems(orderId: Long): Flow<List<OrderItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("UPDATE orders SET orderStatus = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: String)

    @Query("SELECT COUNT(*) FROM orders")
    fun getOrderCount(): Flow<Int>

    @Query("SELECT SUM(totalAmount) FROM orders WHERE orderStatus != 'Cancelled'")
    fun getTotalRevenue(): Flow<Double?>
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity): Long

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Long)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist_items ORDER BY id DESC")
    fun getWishlistItems(): Flow<List<WishlistItemEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE productId = :productId)")
    fun isInWishlist(productId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(item: WishlistItemEntity): Long

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun deleteWishlistItem(productId: Long)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE productId = :productId AND isApproved = 1 ORDER BY id DESC")
    fun getReviewsForProduct(productId: Long): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY id DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Query("UPDATE reviews SET isApproved = :approved WHERE id = :id")
    suspend fun updateReviewApproval(id: Long, approved: Boolean)

    @Query("DELETE FROM reviews WHERE id = :id")
    suspend fun deleteReviewById(id: Long)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM store_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<StoreSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: StoreSettingsEntity)
}

@Dao
interface ReturnRequestDao {
    @Query("SELECT * FROM return_requests ORDER BY id DESC")
    fun getAllReturnRequests(): Flow<List<ReturnRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReturnRequest(request: ReturnRequestEntity): Long

    @Query("UPDATE return_requests SET status = :status WHERE id = :id")
    suspend fun updateReturnStatus(id: Long, status: String)
}
