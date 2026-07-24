package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.components.FormattedPrice
import com.example.ui.components.LocalOrAsyncImage
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductListScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToProductForm: (Long) -> Unit
) {
    val products by viewModel.allProducts.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filtered = products.filter {
        searchQuery.isBlank() ||
        it.title.contains(searchQuery, ignoreCase = true) ||
        it.sku.contains(searchQuery, ignoreCase = true) ||
        it.categoryName.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("পণ্য ব্যবস্থাপনা (Products)", fontWeight = FontWeight.Bold) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToProductForm(0L) },
                containerColor = PrimaryRed,
                contentColor = Color.White,
                modifier = Modifier.testTag("admin_new_product_fab")
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add New")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("পণ্য বা SKU দিয়ে সার্চ করুন...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .testTag("admin_product_search_input"),
                shape = RoundedCornerShape(20.dp),
                singleLine = true
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WhitePure),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LocalOrAsyncImage(
                                imagePath = product.imageUrls.split(",").firstOrNull() ?: "",
                                contentDescription = product.title,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                                Text("SKU: ${product.sku} | ${product.categoryName}", fontSize = 11.sp, color = GrayText)
                                Spacer(modifier = Modifier.height(2.dp))
                                FormattedPrice(price = product.price, discountPrice = product.discountPrice, fontSize = 13)
                                Text("স্টক: ${product.stockQuantity} টি", fontSize = 11.sp, color = if (product.stockQuantity <= 3) WarningOrange else SuccessGreen)
                            }

                            Row {
                                IconButton(
                                    onClick = { onNavigateToProductForm(product.id) },
                                    modifier = Modifier.testTag("admin_edit_product_${product.id}")
                                ) {
                                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = StatusBlue)
                                }

                                IconButton(
                                    onClick = { viewModel.deleteProduct(product.id) },
                                    modifier = Modifier.testTag("admin_delete_product_${product.id}")
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = PrimaryRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
