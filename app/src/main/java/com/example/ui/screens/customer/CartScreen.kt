package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.ui.components.FormattedPrice
import com.example.ui.components.LocalOrAsyncImage
import com.example.ui.theme.*
import com.example.ui.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onNavigateToProducts: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val couponError by viewModel.couponError.collectAsState()

    var couponCodeInput by remember { mutableStateOf("") }

    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val discount = if (appliedCoupon != null) {
        if (appliedCoupon!!.discountType == "PERCENTAGE") {
            (subtotal * appliedCoupon!!.discountValue / 100.0)
        } else {
            appliedCoupon!!.discountValue
        }
    } else 0.0

    val grandTotal = (subtotal - discount).coerceAtLeast(0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("শপিং কার্ট (Shopping Cart)", fontWeight = FontWeight.Bold) },
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
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    shadowElevation = 8.dp,
                    color = WhitePure,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("মোট মুল্য (Total):", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("৳${grandTotal.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryRed)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onNavigateToCheckout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("proceed_to_checkout_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                        ) {
                            Text("অর্ডার সম্পন্ন করুন (Proceed to Checkout)", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.RemoveShoppingCart, contentDescription = null, modifier = Modifier.size(80.dp), tint = GrayText)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("আপনার কার্ট টি খালি!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BlackDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("পছন্দের পোশাকগুলো কার্টে যোগ করুন", fontSize = 14.sp, color = GrayText)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavigateToProducts,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Text("কেনাকাটা শুরু করুন")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cart Items List
                items(cartItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WhitePure),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LocalOrAsyncImage(
                                imagePath = item.productImage,
                                contentDescription = item.productTitle,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "রং: ${item.selectedColor} | সাইজ: ${item.selectedSize}",
                                    fontSize = 11.sp,
                                    color = GrayText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "৳${item.price.toInt()} x ${item.quantity} = ৳${(item.price * item.quantity).toInt()}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryRed
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                IconButton(
                                    onClick = { viewModel.removeFromCart(item.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .border(1.dp, GrayBorder, RoundedCornerShape(6.dp))
                                ) {
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.id, item.quantity - 1) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.Remove, contentDescription = "-", modifier = Modifier.size(14.dp))
                                    }

                                    Text(
                                        text = "${item.quantity}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp)
                                    )

                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.id, item.quantity + 1) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.Add, contentDescription = "+", modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Coupon Entry Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WhitePure),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("কুপন কোড ব্যবহার করুন (Coupon Code)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = couponCodeInput,
                                    onValueChange = { couponCodeInput = it },
                                    placeholder = { Text("যেমন: TITAS10, EID2026", fontSize = 12.sp) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("coupon_input_field"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        if (couponCodeInput.isNotBlank()) {
                                            viewModel.applyCoupon(couponCodeInput, subtotal)
                                        }
                                    },
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("apply_coupon_button"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                                ) {
                                    Text("প্রয়োগ")
                                }
                            }

                            if (couponError != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(couponError!!, color = Color.Red, fontSize = 11.sp)
                            }

                            if (appliedCoupon != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("কুপন (${appliedCoupon!!.code}) সফলভাবে যুক্ত হয়েছে!", color = SuccessGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    TextButton(onClick = { viewModel.removeCoupon() }) {
                                        Text("মুছে ফেলুন", color = Color.Red, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Order Summary Breakdown
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WhitePure),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("অর্ডার সামারি (Order Summary)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("সাবটোটাল (Subtotal)", color = GrayText)
                                Text("৳${subtotal.toInt()}", fontWeight = FontWeight.SemiBold)
                            }

                            if (discount > 0) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("কুপন ডিসকাউন্ট (Discount)", color = SuccessGreen)
                                    Text("-৳${discount.toInt()}", color = SuccessGreen, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = GrayBorder)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("সর্বমোট (Subtotal Net)", fontWeight = FontWeight.Bold)
                                Text("৳${grandTotal.toInt()}", fontWeight = FontWeight.Bold, color = PrimaryRed)
                            }
                        }
                    }
                }
            }
        }
    }
}
