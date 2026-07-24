package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entities.*
import com.example.data.repository.StoreRepository
import com.example.data.seed.DefaultDataSeed
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val repository = StoreRepository(database)

    // Mode Toggle (Is User in Admin Mode or Customer Mode)
    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    fun setAdminMode(isAdmin: Boolean) {
        _isAdminMode.value = isAdmin
    }

    init {
        viewModelScope.launch {
            DefaultDataSeed.seedIfEmpty(database)
        }
    }

    // Customer Home Data
    val categories = repository.allCategories.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val activeBanners = repository.activeBanners.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val featuredProducts = repository.featuredProducts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val flashSaleProducts = repository.flashSaleProducts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allProducts = repository.allProducts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val storeSettings = repository.storeSettings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Search and Filter State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Cart State
    val cartItems = repository.cartItems.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Applied Coupon Code
    private val _appliedCoupon = MutableStateFlow<CouponEntity?>(null)
    val appliedCoupon: StateFlow<CouponEntity?> = _appliedCoupon.asStateFlow()

    private val _couponError = MutableStateFlow<String?>(null)
    val couponError: StateFlow<String?> = _couponError.asStateFlow()

    fun applyCoupon(code: String, subtotal: Double) {
        viewModelScope.launch {
            val coupon = repository.getCouponByCode(code.trim().uppercase())
            if (coupon == null) {
                _couponError.value = "অবৈধ কুপন কোড (Invalid coupon code)"
                _appliedCoupon.value = null
            } else if (subtotal < coupon.minOrderAmount) {
                _couponError.value = "সর্বনিম্ন ৳${coupon.minOrderAmount.toInt()} টাকার কেনাকাটায় প্রযোজ্য"
                _appliedCoupon.value = null
            } else {
                _appliedCoupon.value = coupon
                _couponError.value = null
            }
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _couponError.value = null
    }

    fun addToCart(product: ProductEntity, selectedColor: String, selectedSize: String, quantity: Int = 1) {
        viewModelScope.launch {
            val effectivePrice = product.discountPrice ?: product.price
            repository.addToCart(
                CartItemEntity(
                    productId = product.id,
                    productTitle = product.title,
                    productImage = product.imageUrls.split(",").firstOrNull() ?: "",
                    price = effectivePrice,
                    selectedColor = selectedColor,
                    selectedSize = selectedSize,
                    quantity = quantity
                )
            )
        }
    }

    fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                repository.removeCartItem(cartItemId)
            } else {
                repository.updateCartQuantity(cartItemId, newQuantity)
            }
        }
    }

    fun removeFromCart(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeCartItem(cartItemId)
        }
    }

    // Wishlist
    val wishlistItems = repository.wishlistItems.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleWishlist(product: ProductEntity) {
        viewModelScope.launch {
            val isWish = wishlistItems.value.any { it.productId == product.id }
            if (isWish) {
                repository.removeFromWishlist(product.id)
            } else {
                repository.addToWishlist(
                    WishlistItemEntity(
                        productId = product.id,
                        productTitle = product.title,
                        productImage = product.imageUrls.split(",").firstOrNull() ?: "",
                        price = product.discountPrice ?: product.price,
                        rating = product.rating
                    )
                )
            }
        }
    }

    // Place Order
    suspend fun placeOrder(
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        shippingAddress: String,
        district: String,
        deliveryType: String,
        paymentMethod: String,
        note: String
    ): String {
        val items = cartItems.value
        if (items.isEmpty()) return ""

        val subtotal = items.sumOf { it.price * it.quantity }
        val settings = storeSettings.value
        val shippingFee = if (deliveryType == "Inside Dhaka") {
            settings?.insideDhakaFee ?: 80.0
        } else {
            settings?.outsideDhakaFee ?: 150.0
        }

        val coupon = _appliedCoupon.value
        val discountAmount = if (coupon != null) {
            if (coupon.discountType == "PERCENTAGE") {
                (subtotal * coupon.discountValue / 100.0)
            } else {
                coupon.discountValue
            }
        } else 0.0

        val totalAmount = (subtotal + shippingFee - discountAmount).coerceAtLeast(0.0)
        val orderNum = "TF-2026-" + Random.nextInt(1000, 9999)
        val trackingNum = "TITAS-TRK-" + Random.nextInt(1000, 9999)

        val order = OrderEntity(
            orderNumber = orderNum,
            customerName = customerName,
            customerPhone = customerPhone,
            customerEmail = customerEmail,
            shippingAddress = shippingAddress,
            district = district,
            deliveryType = deliveryType,
            paymentMethod = paymentMethod,
            subtotal = subtotal,
            shippingFee = shippingFee,
            discountAmount = discountAmount,
            totalAmount = totalAmount,
            orderStatus = "Pending",
            createdAt = System.currentTimeMillis(),
            trackingNumber = trackingNum,
            note = note
        )

        val orderItemEntities = items.map { cart ->
            OrderItemEntity(
                orderId = 0,
                productId = cart.productId,
                productTitle = cart.productTitle,
                productImage = cart.productImage,
                selectedColor = cart.selectedColor,
                selectedSize = cart.selectedSize,
                unitPrice = cart.price,
                quantity = cart.quantity,
                totalPrice = cart.price * cart.quantity
            )
        }

        repository.createOrder(order, orderItemEntities)
        repository.clearCart()
        removeCoupon()
        return orderNum
    }

    // Reviews
    fun submitReview(productId: Long, customerName: String, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.addReview(
                ReviewEntity(
                    productId = productId,
                    customerName = customerName,
                    rating = rating,
                    comment = comment,
                    date = "2026-07-24",
                    isApproved = true
                )
            )
        }
    }

    // Return Request
    suspend fun submitReturnRequest(
        orderNumber: String,
        phone: String,
        reason: String,
        method: String,
        account: String
    ): Boolean {
        repository.submitReturnRequest(
            ReturnRequestEntity(
                orderNumber = orderNumber,
                customerPhone = phone,
                reason = reason,
                refundMethod = method,
                accountNumber = account,
                status = "Pending"
            )
        )
        return true
    }
}
