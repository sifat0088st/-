package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.*

@Composable
fun LocalOrAsyncImage(
    imagePath: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val resId = when {
        imagePath.contains("img_hero_banner1") -> R.drawable.img_hero_banner1_1784912503553
        imagePath.contains("img_product_saree") -> R.drawable.img_product_saree_1784912517093
        imagePath.contains("img_product_panjabi") -> R.drawable.img_product_panjabi_1784912531316
        imagePath.contains("img_app_icon") -> R.drawable.img_app_icon_1784912487365
        else -> 0
    }

    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
        AsyncImage(
            model = imagePath,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        // Fallback default placeholder illustration
        Image(
            painter = painterResource(id = R.drawable.img_product_saree_1784912517093),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

@Composable
fun FormattedPrice(
    price: Double,
    discountPrice: Double? = null,
    fontSize: Int = 16,
    isBold: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (discountPrice != null && discountPrice < price) {
            Text(
                text = "৳${discountPrice.toInt()}",
                fontSize = fontSize.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = PrimaryRed
            )
            Text(
                text = "৳${price.toInt()}",
                fontSize = (fontSize - 2).sp,
                fontWeight = FontWeight.Normal,
                color = GrayText,
                style = androidx.compose.ui.text.TextStyle(
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                )
            )
        } else {
            Text(
                text = "৳${price.toInt()}",
                fontSize = fontSize.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = BlackDark
            )
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        "Pending" -> Color(0xFFFFF3E0) to WarningOrange
        "Processing" -> Color(0xFFE3F2FD) to StatusBlue
        "Shipped" -> Color(0xFFE8F5E9) to SuccessGreen
        "Delivered" -> Color(0xFFE8F5E9) to SuccessGreen
        "Cancelled" -> Color(0xFFFFEBEE) to PrimaryRed
        "Approved" -> Color(0xFFE8F5E9) to SuccessGreen
        "Rejected" -> Color(0xFFFFEBEE) to PrimaryRed
        else -> GrayBackground to GrayText
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun StarRatingView(rating: Float, reviewCount: Int = 0, starSize: Dp = 16.dp) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val isFilled = index < rating.toInt()
            Icon(
                imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = if (isFilled) AccentGold else GrayBorder,
                modifier = Modifier.size(starSize)
            )
        }
        if (reviewCount > 0) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "($reviewCount)",
                fontSize = 12.sp,
                color = GrayText
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BlackDark
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = GrayText
                )
            }
        }
        if (actionText != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Text(
                    text = actionText,
                    color = PrimaryRed,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
