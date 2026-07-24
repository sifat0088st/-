package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    // Customer Routes
    object CustomerHome : Screen("customer_home", "Home")
    object ProductList : Screen("product_list?catId={catId}&query={query}&flash={flash}", "Products") {
        fun createRoute(catId: Long = 0L, query: String = "", flash: Boolean = false) =
            "product_list?catId=$catId&query=$query&flash=$flash"
    }
    object ProductDetail : Screen("product_detail/{productId}", "Product Details") {
        fun createRoute(productId: Long) = "product_detail/$productId"
    }
    object Cart : Screen("cart", "Shopping Cart")
    object Checkout : Screen("checkout", "Checkout")
    object OrderSuccess : Screen("order_success/{orderNumber}", "Order Confirmed") {
        fun createRoute(orderNumber: String) = "order_success/$orderNumber"
    }
    object OrderTracking : Screen("order_tracking", "Track Order")
    object Invoice : Screen("invoice/{orderNumber}", "Order Invoice") {
        fun createRoute(orderNumber: String) = "invoice/$orderNumber"
    }
    object Wishlist : Screen("wishlist", "My Wishlist")
    object ReturnRefund : Screen("return_refund", "Return & Refund")
    object ContactSupport : Screen("contact_support", "Customer Support")
    object CustomerProfile : Screen("customer_profile", "My Account")

    // Admin Panel Routes
    object AdminDashboard : Screen("admin_dashboard", "Admin Dashboard")
    object AdminProductList : Screen("admin_product_list", "Product Management")
    object AdminProductForm : Screen("admin_product_form?productId={productId}", "Add/Edit Product") {
        fun createRoute(productId: Long = 0L) = "admin_product_form?productId=$productId"
    }
    object AdminCategory : Screen("admin_category", "Category Management")
    object AdminBrand : Screen("admin_brand", "Brand Management")
    object AdminVariant : Screen("admin_variant", "Variants Management")
    object AdminBanner : Screen("admin_banner", "Banner Management")
    object AdminOrders : Screen("admin_orders", "Orders Management")
    object AdminCustomers : Screen("admin_customers", "Customer Management")
    object AdminReviews : Screen("admin_reviews", "Reviews Management")
    object AdminCoupons : Screen("admin_coupons", "Coupon Management")
    object AdminFlashSale : Screen("admin_flash_sale", "Flash Sale Management")
    object AdminShipping : Screen("admin_shipping", "Shipping Settings")
    object AdminPayment : Screen("admin_payment", "Payment Gateways")
    object AdminHomepageBuilder : Screen("admin_homepage_builder", "Homepage Builder")
    object AdminThemeCustomizer : Screen("admin_theme_customizer", "Theme Customizer")
    object AdminSeo : Screen("admin_seo", "SEO Settings")
    object AdminReports : Screen("admin_reports", "Reports & Analytics")
    object AdminSettings : Screen("admin_settings", "Website Settings")
    object AdminBackup : Screen("admin_backup", "Backup & Restore")
    object AdminRolePermission : Screen("admin_role_permission", "Role & Permission")
}
