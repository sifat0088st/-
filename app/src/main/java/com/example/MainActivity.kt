package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.navigation.Screen
import com.example.ui.screens.admin.*
import com.example.ui.screens.customer.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AdminViewModel
import com.example.ui.viewmodel.StoreViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppNavigation()
                }
            }
        }
    }
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()
    val storeViewModel: StoreViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.CustomerHome.route
    ) {
        // ================= CUSTOMER SCREENS =================
        composable(Screen.CustomerHome.route) {
            CustomerHomeScreen(
                viewModel = storeViewModel,
                onNavigateToProducts = { catId, query, flash ->
                    navController.navigate(Screen.ProductList.createRoute(catId, query, flash))
                },
                onNavigateToProductDetail = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToWishlist = {
                    navController.navigate(Screen.Wishlist.route)
                },
                onNavigateToOrderTracking = {
                    navController.navigate(Screen.OrderTracking.route)
                },
                onNavigateToContactSupport = {
                    navController.navigate(Screen.ContactSupport.route)
                },
                onSwitchToAdmin = {
                    navController.navigate(Screen.AdminDashboard.route)
                }
            )
        }

        composable(
            route = Screen.ProductList.route,
            arguments = listOf(
                navArgument("catId") { type = NavType.LongType; defaultValue = 0L },
                navArgument("query") { type = NavType.StringType; defaultValue = "" },
                navArgument("flash") { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            val catId = backStackEntry.arguments?.getLong("catId") ?: 0L
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val flash = backStackEntry.arguments?.getBoolean("flash") ?: false

            ProductListScreen(
                viewModel = storeViewModel,
                initialCatId = catId,
                initialQuery = query,
                isFlashSaleOnly = flash,
                onNavigateToProductDetail = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
            ProductDetailScreen(
                productId = productId,
                viewModel = storeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCart = { navController.navigate(Screen.Cart.route) },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                viewModel = storeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                onNavigateToProducts = { navController.navigate(Screen.ProductList.createRoute()) }
            )
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(
                viewModel = storeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onOrderSuccess = { orderNumber ->
                    navController.navigate(Screen.OrderSuccess.createRoute(orderNumber)) {
                        popUpTo(Screen.CustomerHome.route) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = Screen.OrderSuccess.route,
            arguments = listOf(navArgument("orderNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderNumber = backStackEntry.arguments?.getString("orderNumber") ?: ""
            OrderSuccessScreen(
                orderNumber = orderNumber,
                onNavigateToHome = { navController.navigate(Screen.CustomerHome.route) },
                onNavigateToTracking = { navController.navigate(Screen.OrderTracking.route) },
                onNavigateToInvoice = { ordNum -> navController.navigate(Screen.Invoice.createRoute(ordNum)) }
            )
        }

        composable(Screen.OrderTracking.route) {
            OrderTrackingScreen(
                viewModel = storeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToInvoice = { ordNum -> navController.navigate(Screen.Invoice.createRoute(ordNum)) }
            )
        }

        composable(
            route = Screen.Invoice.route,
            arguments = listOf(navArgument("orderNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderNumber = backStackEntry.arguments?.getString("orderNumber") ?: ""
            InvoiceScreen(
                orderNumber = orderNumber,
                viewModel = storeViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Wishlist.route) {
            WishlistScreen(
                viewModel = storeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProductDetail = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }

        composable(Screen.ReturnRefund.route) {
            ReturnRefundScreen(
                viewModel = storeViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ContactSupport.route) {
            ContactSupportScreen(
                viewModel = storeViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.CustomerProfile.route) {
            CustomerProfileScreen(
                viewModel = storeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToWishlist = { navController.navigate(Screen.Wishlist.route) },
                onNavigateToReturnRefund = { navController.navigate(Screen.ReturnRefund.route) },
                onNavigateToTracking = { navController.navigate(Screen.OrderTracking.route) },
                onNavigateToInvoice = { ordNum -> navController.navigate(Screen.Invoice.createRoute(ordNum)) },
                onSwitchToAdmin = { navController.navigate(Screen.AdminDashboard.route) }
            )
        }

        // ================= ADMIN PANEL SCREENS =================
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                viewModel = adminViewModel,
                onNavigateToProducts = { navController.navigate(Screen.AdminProductList.route) },
                onNavigateToProductForm = { id -> navController.navigate(Screen.AdminProductForm.createRoute(id)) },
                onNavigateToCategories = { navController.navigate(Screen.AdminCategory.route) },
                onNavigateToBrands = { navController.navigate(Screen.AdminBrand.route) },
                onNavigateToVariants = { navController.navigate(Screen.AdminVariant.route) },
                onNavigateToBanners = { navController.navigate(Screen.AdminBanner.route) },
                onNavigateToOrders = { navController.navigate(Screen.AdminOrders.route) },
                onNavigateToCustomers = { navController.navigate(Screen.AdminCustomers.route) },
                onNavigateToReviews = { navController.navigate(Screen.AdminReviews.route) },
                onNavigateToCoupons = { navController.navigate(Screen.AdminCoupons.route) },
                onNavigateToFlashSale = { navController.navigate(Screen.AdminFlashSale.route) },
                onNavigateToShipping = { navController.navigate(Screen.AdminShipping.route) },
                onNavigateToPayment = { navController.navigate(Screen.AdminPayment.route) },
                onNavigateToHomepageBuilder = { navController.navigate(Screen.AdminHomepageBuilder.route) },
                onNavigateToThemeCustomizer = { navController.navigate(Screen.AdminThemeCustomizer.route) },
                onNavigateToSeo = { navController.navigate(Screen.AdminSeo.route) },
                onNavigateToReports = { navController.navigate(Screen.AdminReports.route) },
                onNavigateToSettings = { navController.navigate(Screen.AdminSettings.route) },
                onNavigateToBackup = { navController.navigate(Screen.AdminBackup.route) },
                onNavigateToRoles = { navController.navigate(Screen.AdminRolePermission.route) },
                onSwitchToCustomerStore = { navController.navigate(Screen.CustomerHome.route) }
            )
        }

        composable(Screen.AdminProductList.route) {
            AdminProductListScreen(
                viewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProductForm = { id -> navController.navigate(Screen.AdminProductForm.createRoute(id)) }
            )
        }

        composable(
            route = Screen.AdminProductForm.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType; defaultValue = 0L })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
            AdminProductFormScreen(
                productId = productId,
                viewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminCategory.route) {
            AdminCategoryScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminBrand.route) {
            AdminBrandScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminVariant.route) {
            AdminVariantScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminBanner.route) {
            AdminBannerScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminOrders.route) {
            AdminOrderListScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminCustomers.route) {
            AdminCustomerListScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminReviews.route) {
            AdminReviewScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminCoupons.route) {
            AdminCouponScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminFlashSale.route) {
            AdminFlashSaleScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminShipping.route) {
            AdminShippingScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminPayment.route) {
            AdminPaymentScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminHomepageBuilder.route) {
            AdminHomepageBuilderScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminThemeCustomizer.route) {
            AdminThemeCustomizerScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminSeo.route) {
            AdminSeoScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminReports.route) {
            AdminReportsScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminSettings.route) {
            AdminSettingsScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminBackup.route) {
            AdminBackupScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AdminRolePermission.route) {
            AdminRolePermissionScreen(
                viewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() },
                onSwitchToCustomerStore = { navController.navigate(Screen.CustomerHome.route) }
            )
        }
    }
}
