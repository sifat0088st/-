package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val sku: String,
    val price: Double,
    val discountPrice: Double? = null,
    val categoryId: Long,
    val categoryName: String,
    val brandId: Long,
    val brandName: String,
    val imageUrls: String, // Comma-separated or JSON list
    val videoUrl: String = "",
    val isDigital: Boolean = false,
    val stockQuantity: Int = 10,
    val isFeatured: Boolean = false,
    val isFlashSale: Boolean = false,
    val colorsCsv: String = "Red,Black,Maroon,White,Gold", // Available color variants
    val sizesCsv: String = "S,M,L,XL,XXL,Free Size", // Available size variants
    val rating: Float = 4.8f,
    val reviewCount: Int = 12
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val imageUrl: String = "",
    val iconName: String = "Category",
    val productCount: Int = 0,
    val isActive: Boolean = true
)

@Entity(tableName = "brands")
data class BrandEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val logoUrl: String = "",
    val description: String = ""
)

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subtitle: String = "",
    val imageUrl: String,
    val targetCategory: String = "All",
    val isActive: Boolean = true
)

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String,
    val discountType: String = "PERCENTAGE", // "PERCENTAGE" or "FIXED"
    val discountValue: Double = 10.0,
    val minOrderAmount: Double = 500.0,
    val expiryDate: String = "2026-12-31",
    val isActive: Boolean = true,
    val usageCount: Int = 0
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String = "",
    val shippingAddress: String,
    val district: String = "Dhaka",
    val deliveryType: String = "Inside Dhaka", // "Inside Dhaka" or "Outside Dhaka"
    val paymentMethod: String = "Cash on Delivery", // "Cash on Delivery", "bKash", "Nagad"
    val subtotal: Double,
    val shippingFee: Double,
    val discountAmount: Double = 0.0,
    val totalAmount: Double,
    val orderStatus: String = "Pending", // "Pending", "Processing", "Shipped", "Delivered", "Cancelled"
    val createdAt: Long = System.currentTimeMillis(),
    val trackingNumber: String = "",
    val note: String = ""
)

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val productId: Long,
    val productTitle: String,
    val productImage: String,
    val selectedColor: String,
    val selectedSize: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val productTitle: String,
    val productImage: String,
    val price: Double,
    val selectedColor: String,
    val selectedSize: String,
    val quantity: Int = 1
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val productTitle: String,
    val productImage: String,
    val price: Double,
    val rating: Float = 4.8f
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val customerName: String,
    val rating: Int,
    val comment: String,
    val date: String,
    val isApproved: Boolean = true
)

@Entity(tableName = "store_settings")
data class StoreSettingsEntity(
    @PrimaryKey val id: Long = 1,
    val storeName: String = "তিতাস ফ্যাশন",
    val phone: String = "+880 1711-223344",
    val email: String = "info@titasfashion.com",
    val address: String = "House #12, Road #5, Dhanmondi, Dhaka-1205",
    val insideDhakaFee: Double = 80.0,
    val outsideDhakaFee: Double = 150.0,
    val bkashNumber: String = "01711223344",
    val nagadNumber: String = "01711223344",
    val codEnabled: Boolean = true,
    val metaTitle: String = "তিতাস ফ্যাশন | Premium Clothing Store",
    val metaDescription: String = "Buy original sarees, panjabi, salwar kameez & fashion items directly from Titas Fashion.",
    val metaKeywords: String = "fashion, saree, panjabi, clothing, bangladesh, titas fashion",
    val flashSaleActive: Boolean = true,
    val flashSaleTitle: String = "Grand Eid Special Flash Sale",
    val flashSaleDiscountPercent: Int = 20,
    val returnPolicyDays: Int = 7
)

@Entity(tableName = "return_requests")
data class ReturnRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val customerPhone: String,
    val reason: String,
    val refundMethod: String,
    val accountNumber: String,
    val status: String = "Pending", // "Pending", "Approved", "Rejected", "Refunded"
    val createdAt: Long = System.currentTimeMillis()
)
