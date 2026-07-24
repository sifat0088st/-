package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.StoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit,
    onOrderSuccess: (String) -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val settings by viewModel.storeSettings.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("সাইফুল ইসলাম") }
    var phone by remember { mutableStateOf("01819283746") }
    var email by remember { mutableStateOf("saiful@gmail.com") }
    var address by remember { mutableStateOf("বাসা #৪৫, রোড #২, ধানমন্ডি") }
    var district by remember { mutableStateOf("Dhaka") }
    var deliveryType by remember { mutableStateOf("Inside Dhaka") } // "Inside Dhaka" or "Outside Dhaka"
    var paymentMethod by remember { mutableStateOf("Cash on Delivery") } // "Cash on Delivery", "bKash", "Nagad"
    var note by remember { mutableStateOf("") }
    var isPlacingOrder by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val insideFee = settings?.insideDhakaFee ?: 80.0
    val outsideFee = settings?.outsideDhakaFee ?: 150.0
    val shippingFee = if (deliveryType == "Inside Dhaka") insideFee else outsideFee

    val discount = if (appliedCoupon != null) {
        if (appliedCoupon!!.discountType == "PERCENTAGE") {
            (subtotal * appliedCoupon!!.discountValue / 100.0)
        } else {
            appliedCoupon!!.discountValue
        }
    } else 0.0

    val totalAmount = (subtotal + shippingFee - discount).coerceAtLeast(0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("চেকআউট (Checkout)", fontWeight = FontWeight.Bold) },
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
                        Text("সর্বমোট পরিশোধযোগ্য (Grand Total):", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("৳${totalAmount.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryRed)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank() && phone.isNotBlank() && address.isNotBlank()) {
                                isPlacingOrder = true
                                coroutineScope.launch {
                                    val orderNum = viewModel.placeOrder(
                                        customerName = name,
                                        customerPhone = phone,
                                        customerEmail = email,
                                        shippingAddress = address,
                                        district = district,
                                        deliveryType = deliveryType,
                                        paymentMethod = paymentMethod,
                                        note = note
                                    )
                                    isPlacingOrder = false
                                    if (orderNum.isNotEmpty()) {
                                        onOrderSuccess(orderNum)
                                    }
                                }
                            }
                        },
                        enabled = !isPlacingOrder && name.isNotBlank() && phone.isNotBlank() && address.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("place_order_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        if (isPlacingOrder) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("অর্ডার কনফার্ম করুন (Confirm Order)", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
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
            // Customer Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("১. আপনার তথ্য (Customer Info)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("আপনার নাম *") },
                        modifier = Modifier.fillMaxWidth().testTag("checkout_name_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("মোবাইল নম্বর *") },
                        modifier = Modifier.fillMaxWidth().testTag("checkout_phone_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("ইমেইল (ঐচ্ছিক)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("ডেলিভারি ঠিকানা *") },
                        modifier = Modifier.fillMaxWidth().testTag("checkout_address_input"),
                        minLines = 2
                    )
                }
            }

            // Shipping Location Selector
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("২. ডেলিভারি এলাকা (Delivery Zone)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, if (deliveryType == "Inside Dhaka") PrimaryRed else GrayBorder, RoundedCornerShape(8.dp))
                            .clickable { deliveryType = "Inside Dhaka"; district = "Dhaka" }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = deliveryType == "Inside Dhaka",
                            onClick = { deliveryType = "Inside Dhaka"; district = "Dhaka" },
                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryRed)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("ঢাকার ভেতরে (Inside Dhaka)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("ডেলিভারি চার্জ: ৳${insideFee.toInt()}", fontSize = 12.sp, color = GrayText)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, if (deliveryType == "Outside Dhaka") PrimaryRed else GrayBorder, RoundedCornerShape(8.dp))
                            .clickable { deliveryType = "Outside Dhaka"; district = "Chittagong" }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = deliveryType == "Outside Dhaka",
                            onClick = { deliveryType = "Outside Dhaka"; district = "Chittagong" },
                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryRed)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("ঢাকার বাইরে (Outside Dhaka)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("ডেলিভারি চার্জ: ৳${outsideFee.toInt()}", fontSize = 12.sp, color = GrayText)
                        }
                    }
                }
            }

            // Payment Gateways
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("৩. পেমেন্ট পদ্ধতি (Payment Method)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)
                    Spacer(modifier = Modifier.height(10.dp))

                    listOf(
                        "Cash on Delivery" to "ক্যাশ অন ডেলিভারি (পণ্য হাতে পেয়ে মূল্য পরিশোধ)",
                        "bKash" to "বিকাশ মারফত পেমেন্ট (bKash Personal: 01711223344)",
                        "Nagad" to "নগদ মারফত পেমেন্ট (Nagad Personal: 01711223344)"
                    ).forEach { (method, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, if (paymentMethod == method) PrimaryRed else GrayBorder, RoundedCornerShape(8.dp))
                                .clickable { paymentMethod = method }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = paymentMethod == method,
                                onClick = { paymentMethod = method },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimaryRed)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Price Breakdown Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PrimaryRedLight),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("মূল্য হিসাব (Payment Breakdown)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryRedDark)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("পণ্যের মোট মুল্য:", fontSize = 13.sp)
                        Text("৳${subtotal.toInt()}", fontSize = 13.sp)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("ডেলিভারি চার্জ:", fontSize = 13.sp)
                        Text("৳${shippingFee.toInt()}", fontSize = 13.sp)
                    }
                    if (discount > 0) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("ডিসকাউন্ট:", fontSize = 13.sp, color = SuccessGreen)
                            Text("-৳${discount.toInt()}", fontSize = 13.sp, color = SuccessGreen)
                        }
                    }
                    HorizontalDivider(color = PrimaryRed.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("সর্বমোট পরিশোধযোগ্য:", fontWeight = FontWeight.Bold)
                        Text("৳${totalAmount.toInt()}", fontWeight = FontWeight.Bold, color = PrimaryRed)
                    }
                }
            }
        }
    }
}
