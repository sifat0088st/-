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

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val repository = StoreRepository(database)

    // Admin Dashboard Stats
    val totalRevenue = repository.totalRevenue.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val orderCount = repository.orderCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val productCount = repository.productCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val lowStockCount = repository.lowStockCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allOrders = repository.allOrders.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allProducts = repository.allProducts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allCategories = repository.allCategories.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allBrands = repository.allBrands.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allBanners = repository.allBanners.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allCoupons = repository.allCoupons.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allReviews = repository.allReviews.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val storeSettings = repository.storeSettings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val returnRequests = repository.allReturnRequests.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Product CRUD
    fun saveProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.insertOrUpdateProduct(product)
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            repository.deleteProductById(id)
        }
    }

    // Category CRUD
    fun saveCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            repository.deleteCategoryById(id)
        }
    }

    // Brand CRUD
    fun saveBrand(brand: BrandEntity) {
        viewModelScope.launch {
            repository.insertBrand(brand)
        }
    }

    fun deleteBrand(id: Long) {
        viewModelScope.launch {
            repository.deleteBrandById(id)
        }
    }

    // Banner CRUD
    fun saveBanner(banner: BannerEntity) {
        viewModelScope.launch {
            repository.insertBanner(banner)
        }
    }

    fun deleteBanner(id: Long) {
        viewModelScope.launch {
            repository.deleteBannerById(id)
        }
    }

    // Coupon CRUD
    fun saveCoupon(coupon: CouponEntity) {
        viewModelScope.launch {
            repository.insertCoupon(coupon)
        }
    }

    fun deleteCoupon(id: Long) {
        viewModelScope.launch {
            repository.deleteCouponById(id)
        }
    }

    // Order Status
    fun updateOrderStatus(orderId: Long, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
        }
    }

    // Review Approval
    fun setReviewApproval(reviewId: Long, approved: Boolean) {
        viewModelScope.launch {
            repository.setReviewApproval(reviewId, approved)
        }
    }

    // Store Settings
    fun saveSettings(settings: StoreSettingsEntity) {
        viewModelScope.launch {
            repository.updateSettings(settings)
        }
    }

    // Return Request Status
    fun updateReturnStatus(id: Long, status: String) {
        viewModelScope.launch {
            repository.updateReturnStatus(id, status)
        }
    }

    // Reset Data to Default Seeds
    fun resetDataToDefault() {
        viewModelScope.launch {
            database.clearAllTables()
            DefaultDataSeed.seedIfEmpty(database)
        }
    }
}
