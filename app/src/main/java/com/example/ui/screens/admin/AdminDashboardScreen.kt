package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

data class AdminModule(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onNavigateToProducts: () -> Unit,
    onNavigateToProductForm: (Long) -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToBrands: () -> Unit,
    onNavigateToVariants: () -> Unit,
    onNavigateToBanners: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToCustomers: () -> Unit,
    onNavigateToReviews: () -> Unit,
    onNavigateToCoupons: () -> Unit,
    onNavigateToFlashSale: () -> Unit,
    onNavigateToShipping: () -> Unit,
    onNavigateToPayment: () -> Unit,
    onNavigateToHomepageBuilder: () -> Unit,
    onNavigateToThemeCustomizer: () -> Unit,
    onNavigateToSeo: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToBackup: () -> Unit,
    onNavigateToRoles: () -> Unit,
    onSwitchToCustomerStore: () -> Unit
) {
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val orderCount by viewModel.orderCount.collectAsState()
    val productCount by viewModel.productCount.collectAsState()
    val lowStockCount by viewModel.lowStockCount.collectAsState()

    val modules = listOf(
        AdminModule("পণ্য ব্যবস্থাপনা", Icons.Filled.ShoppingBag, PrimaryRed, onNavigateToProducts),
        AdminModule("ক্যাটাগরি", Icons.Filled.Category, StatusBlue, onNavigateToCategories),
        AdminModule("ব্র্যান্ড", Icons.Filled.BrandingWatermark, SuccessGreen, onNavigateToBrands),
        AdminModule("ভেরিয়েন্ট (রং/সাইজ)", Icons.Filled.Style, WarningOrange, onNavigateToVariants),
        AdminModule("ব্যানার ও অফার", Icons.Filled.ViewCarousel, PrimaryRedDark, onNavigateToBanners),
        AdminModule("অর্ডার সমূহ", Icons.Filled.Receipt, StatusBlue, onNavigateToOrders),
        AdminModule("গ্রাহক তালিকা", Icons.Filled.People, SuccessGreen, onNavigateToCustomers),
        AdminModule("রিভিউ রিঅ্যাকশন", Icons.Filled.RateReview, WarningOrange, onNavigateToReviews),
        AdminModule("কুপন ও ডিসকাউন্ট", Icons.Filled.ConfirmationNumber, PrimaryRed, onNavigateToCoupons),
        AdminModule("ফ্ল্যাশ সেল সেটিং", Icons.Filled.Bolt, AccentGold, onNavigateToFlashSale),
        AdminModule("শিপিং ও চার্জ", Icons.Filled.LocalShipping, StatusBlue, onNavigateToShipping),
        AdminModule("পেমেন্ট গেটওয়ে", Icons.Filled.Payment, SuccessGreen, onNavigateToPayment),
        AdminModule("হোমপেজ বিল্ডার", Icons.Filled.DashboardCustomize, WarningOrange, onNavigateToHomepageBuilder),
        AdminModule("থিম কাস্টমাইজ", Icons.Filled.Palette, PrimaryRed, onNavigateToThemeCustomizer),
        AdminModule("SEO সেটিংস", Icons.Filled.Search, StatusBlue, onNavigateToSeo),
        AdminModule("রিপোর্ট ও এনালাইটিক্স", Icons.Filled.BarChart, SuccessGreen, onNavigateToReports),
        AdminModule("ওয়েবসাইট সেটিংস", Icons.Filled.Settings, WarningOrange, onNavigateToSettings),
        AdminModule("ব্যাকআপ ও রিস্টোর", Icons.Filled.Backup, PrimaryRedDark, onNavigateToBackup),
        AdminModule("রোল ও পারমিশন", Icons.Filled.AdminPanelSettings, BlackDark, onNavigateToRoles)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("তিতাস ফ্যাশন এডমিন প্যানেল", fontWeight = FontWeight.Bold) },
                actions = {
                    Button(
                        onClick = onSwitchToCustomerStore,
                        colors = ButtonDefaults.buttonColors(containerColor = WhitePure, contentColor = PrimaryRed),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("customer_store_switch_button")
                    ) {
                        Icon(Icons.Filled.Store, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("কাস্টমার স্টোর", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryRed,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToProductForm(0L) },
                containerColor = PrimaryRed,
                contentColor = Color.White,
                modifier = Modifier.testTag("admin_add_product_fab")
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Product")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Row Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Revenue Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = PrimaryRedLight)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("মোট বিক্রি", fontSize = 11.sp, color = PrimaryRedDark, fontWeight = FontWeight.Bold)
                        Text("৳${(totalRevenue ?: 0.0).toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryRed)
                    }
                }

                // Orders Count Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("মোট অর্ডার", fontSize = 11.sp, color = StatusBlue, fontWeight = FontWeight.Bold)
                        Text("$orderCount টি", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = StatusBlue)
                    }
                }

                // Low Stock Alert Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("লো স্টক অ্যালার্ট", fontSize = 11.sp, color = WarningOrange, fontWeight = FontWeight.Bold)
                        Text("$lowStockCount টি", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WarningOrange)
                    }
                }
            }

            Text("এডমিন মডিউল সমূহ (Admin Control Modules)", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            // Modules Grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                modules.chunked(2).forEach { rowModules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowModules.forEach { mod ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { mod.onClick() }
                                    .testTag("admin_module_${mod.title}"),
                                colors = CardDefaults.cardColors(containerColor = WhitePure),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = mod.color.copy(alpha = 0.15f),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(mod.icon, contentDescription = null, tint = mod.color, modifier = Modifier.size(22.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = mod.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BlackDark,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                        if (rowModules.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
