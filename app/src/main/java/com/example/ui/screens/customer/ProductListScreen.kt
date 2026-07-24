package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ProductEntity
import com.example.ui.components.ProductCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: StoreViewModel,
    initialCatId: Long = 0L,
    initialQuery: String = "",
    isFlashSaleOnly: Boolean = false,
    onNavigateToProductDetail: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()

    var selectedCatId by remember { mutableStateOf(initialCatId) }
    var searchQuery by remember { mutableStateOf(initialQuery) }
    var inStockOnly by remember { mutableStateOf(false) }
    var selectedColorFilter by remember { mutableStateOf("All") }

    val wishListIds = remember(wishlistItems) { wishlistItems.map { it.productId }.toSet() }

    val filteredProducts = remember(allProducts, selectedCatId, searchQuery, inStockOnly, selectedColorFilter, isFlashSaleOnly) {
        allProducts.filter { product ->
            (selectedCatId == 0L || product.categoryId == selectedCatId) &&
            (!isFlashSaleOnly || product.isFlashSale) &&
            (!inStockOnly || product.stockQuantity > 0) &&
            (selectedColorFilter == "All" || product.colorsCsv.contains(selectedColorFilter, ignoreCase = true)) &&
            (searchQuery.isBlank() || product.title.contains(searchQuery, ignoreCase = true) || product.sku.contains(searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isFlashSaleOnly) "ফ্ল্যাশ সেল (Flash Sale)" else "পণ্য সমূহ (Products)", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryRed,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("খুঁজুন...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .testTag("product_list_search_input"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryRed
                ),
                singleLine = true
            )

            // Category Filter Pills
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCatId == 0L,
                        onClick = { selectedCatId = 0L },
                        label = { Text("সকল ক্যাটাগরি") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCatId == cat.id,
                        onClick = { selectedCatId = cat.id },
                        label = { Text(cat.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Products Grid
            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.SearchOff, contentDescription = null, modifier = Modifier.size(64.dp), tint = GrayText)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("কোনো পণ্য পাওয়া যায়নি", fontWeight = FontWeight.Bold, color = GrayText)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            isWishlisted = wishListIds.contains(product.id),
                            onProductClick = onNavigateToProductDetail,
                            onWishlistToggle = { viewModel.toggleWishlist(it) },
                            onAddToCart = { viewModel.addToCart(it, it.colorsCsv.split(",").firstOrNull() ?: "Standard", it.sizesCsv.split(",").firstOrNull() ?: "Free Size") }
                        )
                    }
                }
            }
        }
    }
}
