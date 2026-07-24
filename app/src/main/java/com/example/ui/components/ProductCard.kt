package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ProductEntity
import com.example.ui.theme.*

@Composable
fun ProductCard(
    product: ProductEntity,
    isWishlisted: Boolean = false,
    onProductClick: (Long) -> Unit,
    onWishlistToggle: (ProductEntity) -> Unit,
    onAddToCart: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstImage = product.imageUrls.split(",").firstOrNull() ?: ""
    val hasDiscount = product.discountPrice != null && product.discountPrice < product.price
    val discountPercent = if (hasDiscount) {
        (((product.price - product.discountPrice!!) / product.price) * 100).toInt()
    } else 0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProductClick(product.id) }
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = WhitePure),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Slate100)
            ) {
                LocalOrAsyncImage(
                    imagePath = firstImage,
                    contentDescription = product.title,
                    modifier = Modifier.fillMaxSize()
                )

                // Discount Badge
                if (hasDiscount) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryRed)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "-$discountPercent%",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Low Stock Badge
                if (product.stockQuantity in 1..3) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.BottomStart)
                            .clip(RoundedCornerShape(8.dp))
                            .background(WarningOrange)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Only ${product.stockQuantity} Left",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Wishlist Toggle Icon
                IconButton(
                    onClick = { onWishlistToggle(product) },
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                        .size(34.dp)
                        .background(Color.White.copy(alpha = 0.85f), CircleShape)
                        .testTag("wishlist_button_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) PrimaryRed else GrayText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = product.categoryName,
                    fontSize = 11.sp,
                    color = GrayText,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                StarRatingView(rating = product.rating, reviewCount = product.reviewCount)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormattedPrice(
                        price = product.price,
                        discountPrice = product.discountPrice,
                        fontSize = 14
                    )

                    IconButton(
                        onClick = { onAddToCart(product) },
                        modifier = Modifier
                            .size(34.dp)
                            .background(PrimaryRed, CircleShape)
                            .testTag("add_to_cart_${product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingBag,
                            contentDescription = "Add to Cart",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
