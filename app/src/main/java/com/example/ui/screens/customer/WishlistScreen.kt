package com.example.ui.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FormattedPrice
import com.example.ui.components.LocalOrAsyncImage
import com.example.ui.theme.*
import com.example.ui.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToProductDetail: (Long) -> Unit
) {
    val wishlistItems by viewModel.wishlistItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("আমার উইশলিস্ট (${wishlistItems.size})", fontWeight = FontWeight.Bold) },
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
        if (wishlistItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.FavoriteBorder, contentDescription = null, modifier = Modifier.size(72.dp), tint = GrayText)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("আপনার উইশলিস্ট টি খালি!", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GrayText)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(wishlistItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WhitePure),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column {
                            Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                                LocalOrAsyncImage(
                                    imagePath = item.productImage,
                                    contentDescription = item.productTitle,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(item.productTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                                Spacer(modifier = Modifier.height(4.dp))
                                FormattedPrice(price = item.price, fontSize = 14)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { onNavigateToProductDetail(item.productId) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("পণ্যটি দেখুন", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
