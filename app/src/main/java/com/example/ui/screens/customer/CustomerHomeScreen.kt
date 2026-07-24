package com.example.ui.screens.customer

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ProductEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    viewModel: StoreViewModel,
    onNavigateToProducts: (catId: Long, query: String, flash: Boolean) -> Unit,
    onNavigateToProductDetail: (Long) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToOrderTracking: () -> Unit,
    onNavigateToContactSupport: () -> Unit,
    onSwitchToAdmin: () -> Unit
) {
    val banners by viewModel.activeBanners.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val featuredProducts by viewModel.featuredProducts.collectAsState()
    val flashSaleProducts by viewModel.flashSaleProducts.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val settings by viewModel.storeSettings.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val wishListIds = remember(wishlistItems) { wishlistItems.map { it.productId }.toSet() }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WhitePure)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Geometric Logo Badge
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(PrimaryRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .border(2.dp, Color.White, RoundedCornerShape(2.dp))
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "তিতাস ",
                                color = BlackDark,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "ফ্যাশন",
                                color = PrimaryRed,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Admin Panel Switch Button
                        OutlinedButton(
                            onClick = onSwitchToAdmin,
                            shape = RoundedCornerShape(20.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(PrimaryRed)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryRed),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("admin_panel_switch_button")
                        ) {
                            Icon(Icons.Filled.AdminPanelSettings, contentDescription = "Admin", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("এডমিন", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Wishlist Button
                        IconButton(onClick = onNavigateToWishlist) {
                            BadgedBox(badge = {
                                if (wishlistItems.isNotEmpty()) {
                                    Badge(containerColor = PrimaryRed, contentColor = Color.White) {
                                        Text("${wishlistItems.size}")
                                    }
                                }
                            }) {
                                Icon(Icons.Filled.Favorite, contentDescription = "Wishlist", tint = BlackDark)
                            }
                        }

                        // Cart Button
                        IconButton(onClick = onNavigateToCart) {
                            BadgedBox(badge = {
                                val totalCartQty = cartItems.sumOf { it.quantity }
                                if (totalCartQty > 0) {
                                    Badge(containerColor = PrimaryRed, contentColor = Color.White) {
                                        Text("$totalCartQty")
                                    }
                                }
                            }) {
                                Icon(Icons.Filled.ShoppingBag, contentDescription = "Cart", tint = BlackDark)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("শাড়ি, পাঞ্জাবী, কুর্তি বা SKU সার্চ করুন...", fontSize = 13.sp, color = GrayText) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = PrimaryRed) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("home_search_input"),
                    shape = RoundedCornerShape(25.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Slate100,
                        unfocusedContainerColor = Slate100,
                        focusedBorderColor = PrimaryRed,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )

                // Live Search Dropdown Suggestion Bar
                if (searchQuery.isNotBlank()) {
                    val filteredSearch = allProducts.filter {
                        it.title.contains(searchQuery, ignoreCase = true) ||
                        it.categoryName.contains(searchQuery, ignoreCase = true) ||
                        it.sku.contains(searchQuery, ignoreCase = true)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = WhitePure),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            if (filteredSearch.isEmpty()) {
                                Text("কোনো পণ্য পাওয়া যায়নি", modifier = Modifier.padding(8.dp), color = GrayText)
                            } else {
                                filteredSearch.take(4).forEach { prod ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onNavigateToProductDetail(prod.id)
                                                searchQuery = ""
                                            }
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        LocalOrAsyncImage(
                                            imagePath = prod.imageUrls.split(",").firstOrNull() ?: "",
                                            contentDescription = null,
                                            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(4.dp))
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(prod.title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                            FormattedPrice(price = prod.price, discountPrice = prod.discountPrice, fontSize = 12)
                                        }
                                        Icon(Icons.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(12.dp), tint = GrayText)
                                    }
                                    HorizontalDivider(color = GrayBorder)
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Banners Slider
            if (banners.isNotEmpty()) {
                val banner = banners.first()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { onNavigateToProducts(0L, "", false) },
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryRed),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LocalOrAsyncImage(
                            imagePath = banner.imageUrl,
                            contentDescription = banner.title,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    androidx.compose.ui.graphics.Brush.horizontalGradient(
                                        colors = listOf(PrimaryRed, Color.Transparent)
                                    )
                                )
                                .padding(20.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                                Text(
                                    text = "ফ্ল্যাশ সেল",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = banner.title,
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    lineHeight = 24.sp
                                )
                                Text(
                                    text = banner.subtitle,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { onNavigateToProducts(0L, "", false) },
                                    colors = ButtonDefaults.buttonColors(containerColor = WhitePure, contentColor = PrimaryRed),
                                    shape = RoundedCornerShape(50.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                                ) {
                                    Text("কেনাকাটা করুন", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Service Bar (Track order, Contact, Refund)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    onClick = onNavigateToOrderTracking,
                    shape = RoundedCornerShape(16.dp),
                    color = PrimaryRedLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryRed.copy(alpha = 0.15f)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.LocalShipping, contentDescription = null, tint = PrimaryRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("অর্ডার ট্র্যাকিং", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryRedDark)
                    }
                }

                Surface(
                    onClick = onNavigateToContactSupport,
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF0FDF4),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.15f)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.SupportAgent, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("সাপোর্ট সেন্টার", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categories Section
            SectionHeader(
                title = "ক্যাটাগরি সমূহ (Categories)",
                actionText = "সব দেখুন",
                onActionClick = { onNavigateToProducts(0L, "", false) }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(categories) { cat ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onNavigateToProducts(cat.id, "", false) }
                            .testTag("category_item_${cat.id}")
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = WhitePure,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                            modifier = Modifier.size(58.dp),
                            shadowElevation = 1.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(PrimaryRedLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Checkroom,
                                        contentDescription = cat.name,
                                        tint = PrimaryRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = cat.name.split(" ").firstOrNull() ?: cat.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlackDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flash Sale Section
            if (flashSaleProducts.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryRed),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Bolt, contentDescription = "Flash", tint = WhitePure, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = settings?.flashSaleTitle ?: "ঈদ ফ্ল্যাশ সেল (Flash Sale)",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "২৮% ছাড়",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(flashSaleProducts) { product ->
                                ProductCard(
                                    product = product,
                                    isWishlisted = wishListIds.contains(product.id),
                                    onProductClick = onNavigateToProductDetail,
                                    onWishlistToggle = { viewModel.toggleWishlist(it) },
                                    onAddToCart = { viewModel.addToCart(it, it.colorsCsv.split(",").firstOrNull() ?: "Standard", it.sizesCsv.split(",").firstOrNull() ?: "Free Size") },
                                    modifier = Modifier.width(170.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Featured Collection
            SectionHeader(
                title = "সেরা কালেকশন (Featured Products)",
                actionText = "সব দেখুন",
                onActionClick = { onNavigateToProducts(0L, "", false) }
            )

            // 2-Column Product Grid
            val gridProducts = featuredProducts.ifEmpty { allProducts }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                gridProducts.chunked(2).forEach { rowProducts ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowProducts.forEach { product ->
                            ProductCard(
                                product = product,
                                isWishlisted = wishListIds.contains(product.id),
                                onProductClick = onNavigateToProductDetail,
                                onWishlistToggle = { viewModel.toggleWishlist(it) },
                                onAddToCart = { viewModel.addToCart(it, it.colorsCsv.split(",").firstOrNull() ?: "Standard", it.sizesCsv.split(",").firstOrNull() ?: "Free Size") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
