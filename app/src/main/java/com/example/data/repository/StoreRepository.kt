package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entities.*
import kotlinx.coroutines.flow.Flow

class StoreRepository(private val database: AppDatabase) {

    // Products
    val allProducts: Flow<List<ProductEntity>> = database.productDao().getAllProducts()
    val featuredProducts: Flow<List<ProductEntity>> = database.productDao().getFeaturedProducts()
    val flashSaleProducts: Flow<List<ProductEntity>> = database.productDao().getFlashSaleProducts()
    val productCount: Flow<Int> = database.productDao().getProductCount()
    val lowStockCount: Flow<Int> = database.productDao().getLowStockCount()

    fun getProductById(id: Long): Flow<ProductEntity?> = database.productDao().getProductById(id)
    fun getProductsByCategory(catId: Long): Flow<List<ProductEntity>> = database.productDao().getProductsByCategory(catId)
    fun searchProducts(query: String): Flow<List<ProductEntity>> = database.productDao().searchProducts(query)

    suspend fun insertOrUpdateProduct(product: ProductEntity): Long = database.productDao().insertProduct(product)
    suspend fun deleteProduct(product: ProductEntity) = database.productDao().deleteProduct(product)
    suspend fun deleteProductById(id: Long) = database.productDao().deleteProductById(id)

    // Categories
    val allCategories: Flow<List<CategoryEntity>> = database.categoryDao().getAllCategories()
    suspend fun insertCategory(category: CategoryEntity) = database.categoryDao().insertCategory(category)
    suspend fun deleteCategoryById(id: Long) = database.categoryDao().deleteCategoryById(id)

    // Brands
    val allBrands: Flow<List<BrandEntity>> = database.brandDao().getAllBrands()
    suspend fun insertBrand(brand: BrandEntity) = database.brandDao().insertBrand(brand)
    suspend fun deleteBrandById(id: Long) = database.brandDao().deleteBrandById(id)

    // Banners
    val activeBanners: Flow<List<BannerEntity>> = database.bannerDao().getActiveBanners()
    val allBanners: Flow<List<BannerEntity>> = database.bannerDao().getAllBanners()
    suspend fun insertBanner(banner: BannerEntity) = database.bannerDao().insertBanner(banner)
    suspend fun deleteBannerById(id: Long) = database.bannerDao().deleteBannerById(id)

    // Coupons
    val allCoupons: Flow<List<CouponEntity>> = database.couponDao().getAllCoupons()
    suspend fun getCouponByCode(code: String) = database.couponDao().getCouponByCode(code)
    suspend fun insertCoupon(coupon: CouponEntity) = database.couponDao().insertCoupon(coupon)
    suspend fun deleteCouponById(id: Long) = database.couponDao().deleteCouponById(id)

    // Orders
    val allOrders: Flow<List<OrderEntity>> = database.orderDao().getAllOrders()
    val totalRevenue: Flow<Double?> = database.orderDao().getTotalRevenue()
    val orderCount: Flow<Int> = database.orderDao().getOrderCount()

    fun getOrderById(id: Long) = database.orderDao().getOrderById(id)
    suspend fun getOrderByNumber(num: String) = database.orderDao().getOrderByNumber(num)
    fun getOrderItems(orderId: Long) = database.orderDao().getOrderItems(orderId)

    suspend fun createOrder(order: OrderEntity, items: List<OrderItemEntity>): Long {
        val id = database.orderDao().insertOrder(order)
        val itemsWithId = items.map { it.copy(orderId = id) }
        database.orderDao().insertOrderItems(itemsWithId)
        return id
    }

    suspend fun updateOrderStatus(orderId: Long, status: String) = database.orderDao().updateOrderStatus(orderId, status)

    // Cart
    val cartItems: Flow<List<CartItemEntity>> = database.cartDao().getCartItems()
    suspend fun addToCart(item: CartItemEntity) = database.cartDao().insertCartItem(item)
    suspend fun updateCartQuantity(id: Long, qty: Int) = database.cartDao().updateQuantity(id, qty)
    suspend fun removeCartItem(id: Long) = database.cartDao().deleteCartItem(id)
    suspend fun clearCart() = database.cartDao().clearCart()

    // Wishlist
    val wishlistItems: Flow<List<WishlistItemEntity>> = database.wishlistDao().getWishlistItems()
    fun isInWishlist(productId: Long): Flow<Boolean> = database.wishlistDao().isInWishlist(productId)
    suspend fun addToWishlist(item: WishlistItemEntity) = database.wishlistDao().insertWishlistItem(item)
    suspend fun removeFromWishlist(productId: Long) = database.wishlistDao().deleteWishlistItem(productId)

    // Reviews
    val allReviews: Flow<List<ReviewEntity>> = database.reviewDao().getAllReviews()
    fun getReviewsForProduct(productId: Long) = database.reviewDao().getReviewsForProduct(productId)
    suspend fun addReview(review: ReviewEntity) = database.reviewDao().insertReview(review)
    suspend fun setReviewApproval(id: Long, approved: Boolean) = database.reviewDao().updateReviewApproval(id, approved)

    // Store Settings
    val storeSettings: Flow<StoreSettingsEntity?> = database.settingsDao().getSettings()
    suspend fun updateSettings(settings: StoreSettingsEntity) = database.settingsDao().insertOrUpdateSettings(settings)

    // Return Requests
    val allReturnRequests: Flow<List<ReturnRequestEntity>> = database.returnRequestDao().getAllReturnRequests()
    suspend fun submitReturnRequest(request: ReturnRequestEntity) = database.returnRequestDao().insertReturnRequest(request)
    suspend fun updateReturnStatus(id: Long, status: String) = database.returnRequestDao().updateReturnStatus(id, status)
}
