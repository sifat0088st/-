package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
fun ProductDetailScreen(
    productId: Long,
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToCheckout: () -> Unit
) {
    val productState = viewModel.repository.getProductById(productId).collectAsState(initial = null)
    val reviews by viewModel.repository.getReviewsForProduct(productId).collectAsState(initial = emptyList())
    val wishlistItems by viewModel.wishlistItems.collectAsState()

    val product = productState.value

    var selectedColor by remember { mutableStateOf("") }
    var selectedSize by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) }
    var showReviewModal by remember { mutableStateOf(false) }

    var reviewerName by remember { mutableStateOf("") }
    var reviewerRating by remember { mutableStateOf(5) }
    var reviewerComment by remember { mutableStateOf("") }

    val isWishlisted = remember(product, wishlistItems) {
        product != null && wishlistItems.any { it.productId == product.id }
    }

    LaunchedEffect(product) {
        if (product != null) {
            val colors = product.colorsCsv.split(",").map { it.trim() }
            if (colors.isNotEmpty() && selectedColor.isEmpty()) {
                selectedColor = colors.first()
            }
            val sizes = product.sizesCsv.split(",").map { it.trim() }
            if (sizes.isNotEmpty() && selectedSize.isEmpty()) {
                selectedSize = sizes.first()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.categoryName ?: "পণ্য বিবরণ", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (product != null) {
                        IconButton(onClick = { viewModel.toggleWishlist(product) }) {
                            Icon(
                                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (isWishlisted) PrimaryRed else Color.White
                            )
                        }
                    }
                    IconButton(onClick = onNavigateToCart) {
                        Icon(Icons.Filled.ShoppingBag, contentDescription = "Cart", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryRed,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (product != null) {
                Surface(
                    shadowElevation = 8.dp,
                    color = WhitePure,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Add to Cart
                        OutlinedButton(
                            onClick = {
                                viewModel.addToCart(product, selectedColor, selectedSize, quantity)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("add_to_cart_detail_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(PrimaryRed)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryRed)
                        ) {
                            Icon(Icons.Filled.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("কার্টে যোগ করুন", fontWeight = FontWeight.Bold)
                        }

                        // Buy Now
                        Button(
                            onClick = {
                                viewModel.addToCart(product, selectedColor, selectedSize, quantity)
                                onNavigateToCheckout()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("buy_now_detail_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                        ) {
                            Icon(Icons.Filled.FlashOn, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("এখনই কিনুন", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (product == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryRed)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Product Main Image View
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    LocalOrAsyncImage(
                        imagePath = product.imageUrls.split(",").firstOrNull() ?: "",
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize()
                    )

                    if (product.discountPrice != null && product.discountPrice < product.price) {
                        Surface(
                            color = PrimaryRed,
                            shape = RoundedCornerShape(bottomEnd = 12.dp),
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            val percent = (((product.price - product.discountPrice) / product.price) * 100).toInt()
                            Text(
                                text = "$percent% ছাড়",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SKU: ${product.sku}",
                            fontSize = 12.sp,
                            color = GrayText,
                            fontWeight = FontWeight.Medium
                        )

                        StatusBadge(if (product.stockQuantity > 0) "In Stock (${product.stockQuantity})" else "Out of Stock")
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = product.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StarRatingView(rating = product.rating, reviewCount = product.reviewCount)
                        Text(
                            text = "ব্র্যান্ড: ${product.brandName}",
                            fontSize = 12.sp,
                            color = PrimaryRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Price Banner
                    Surface(
                        color = PrimaryRedLight,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("মূল্য (Price):", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryRedDark)
                            FormattedPrice(
                                price = product.price,
                                discountPrice = product.discountPrice,
                                fontSize = 22,
                                isBold = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Color Variant Selector
                    val colorsList = product.colorsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    if (colorsList.isNotEmpty()) {
                        Text("রং সিলেক্ট করুন (Select Color):", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(colorsList) { col ->
                                FilterChip(
                                    selected = selectedColor == col,
                                    onClick = { selectedColor = col },
                                    label = { Text(col) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryRed,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Size Variant Selector
                    val sizesList = product.sizesCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    if (sizesList.isNotEmpty()) {
                        Text("সাইজ সিলেক্ট করুন (Select Size):", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(sizesList) { sz ->
                                FilterChip(
                                    selected = selectedSize == sz,
                                    onClick = { selectedSize = sz },
                                    label = { Text(sz) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryRed,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Quantity Counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("পরিমাণ (Quantity):", fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .border(1.dp, GrayBorder, RoundedCornerShape(8.dp))
                                .padding(2.dp)
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Filled.Remove, contentDescription = "Decrease")
                            }

                            Text(
                                text = "$quantity",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            IconButton(
                                onClick = { if (quantity < product.stockQuantity) quantity++ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Increase")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = GrayBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Description Section
                    Text("পণ্যের বিবরণ (Product Description)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.description,
                        fontSize = 14.sp,
                        color = BlackDark,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = GrayBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Reviews Section Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("গ্রাহকদের রিভিউ (${reviews.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        TextButton(onClick = { showReviewModal = true }) {
                            Icon(Icons.Filled.RateReview, contentDescription = null, modifier = Modifier.size(16.dp), tint = PrimaryRed)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("রিভিউ লিখুন", color = PrimaryRed, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (reviews.isEmpty()) {
                        Text("এখনো কোনো রিভিউ দেওয়া হয়নি। প্রথম রিভিউটি লিখুন!", fontSize = 13.sp, color = GrayText)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            reviews.forEach { rev ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = GrayBackground)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(rev.customerName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text(rev.date, fontSize = 11.sp, color = GrayText)
                                        }
                                        StarRatingView(rating = rev.rating.toFloat(), starSize = 14.dp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(rev.comment, fontSize = 13.sp, color = BlackDark)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Write Review Dialog Modal
    if (showReviewModal) {
        AlertDialog(
            onDismissRequest = { showReviewModal = false },
            title = { Text("রিভিউ লিখুন (Write Review)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = reviewerName,
                        onValueChange = { reviewerName = it },
                        label = { Text("আপনার নাম") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("রেটিং দিন:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (1..5).forEach { star ->
                            IconButton(onClick = { reviewerRating = star }) {
                                Icon(
                                    imageVector = if (star <= reviewerRating) Icons.Filled.Star else Icons.Filled.StarBorder,
                                    contentDescription = null,
                                    tint = AccentGold
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = reviewerComment,
                        onValueChange = { reviewerComment = it },
                        label = { Text("আপনার মন্তব্য") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reviewerName.isNotBlank() && reviewerComment.isNotBlank()) {
                            viewModel.submitReview(productId, reviewerName, reviewerRating, reviewerComment)
                            showReviewModal = false
                            reviewerName = ""
                            reviewerComment = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                ) {
                    Text("জমাদান করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReviewModal = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}
