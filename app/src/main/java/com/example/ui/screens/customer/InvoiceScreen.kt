package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(
    orderNumber: String,
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit
) {
    var orderState by remember { mutableStateOf<com.example.data.local.entities.OrderEntity?>(null) }
    var orderItemsState by remember { mutableStateOf<List<com.example.data.local.entities.OrderItemEntity>>(emptyList()) }
    val settings by viewModel.storeSettings.collectAsState()
    var isPrinted by remember { mutableStateOf(false) }

    LaunchedEffect(orderNumber) {
        val ord = viewModel.repository.getOrderByNumber(orderNumber)
        if (ord != null) {
            orderState = ord
            viewModel.repository.getOrderItems(ord.id).collect { items ->
                orderItemsState = items
            }
        }
    }

    val order = orderState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("অফিসিয়াল ইনভয়েস (Invoice)", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isPrinted = true }) {
                        Icon(Icons.Filled.Print, contentDescription = "Print", tint = Color.White)
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
        if (order == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryRed)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WhitePure),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // Store Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = settings?.storeName ?: "তিতাস ফ্যাশন",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryRed
                                )
                                Text(
                                    text = settings?.address ?: "ধানমন্ডি, ঢাকা-১২০৫",
                                    fontSize = 12.sp,
                                    color = GrayText
                                )
                                Text(
                                    text = "ফোন: ${settings?.phone ?: "+880 1711223344"}",
                                    fontSize = 12.sp,
                                    color = GrayText
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Surface(color = PrimaryRed, shape = RoundedCornerShape(4.dp)) {
                                    Text(
                                        text = "INVOICE",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("#${order.orderNumber}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = GrayBorder)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Customer Details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("প্রাপকের তথ্য (Billed To):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryRed)
                                Text(order.customerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(order.customerPhone, fontSize = 12.sp)
                                Text(order.shippingAddress, fontSize = 12.sp, color = GrayText)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("অর্ডার তারিখ:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryRed)
                                Text("2026-07-24", fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                StatusBadge(order.orderStatus)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Items Table Header
                        Surface(color = PrimaryRedLight, shape = RoundedCornerShape(6.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("পণ্য (Item)", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(2f))
                                Text("পরিমাণ", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                Text("মোট", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Items Table Body
                        orderItemsState.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(2f)) {
                                    Text(item.productTitle, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text("রং: ${item.selectedColor}, সাইজ: ${item.selectedSize}", fontSize = 10.sp, color = GrayText)
                                }
                                Text("x${item.quantity}", fontSize = 13.sp, modifier = Modifier.weight(1f))
                                Text("৳${item.totalPrice.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            }
                            HorizontalDivider(color = GrayBorder.copy(alpha = 0.5f))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Totals Summary
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(modifier = Modifier.width(200.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("সাবটোটাল:", fontSize = 13.sp)
                                Text("৳${order.subtotal.toInt()}", fontSize = 13.sp)
                            }
                            Row(modifier = Modifier.width(200.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("ডেলিভারি ফি:", fontSize = 13.sp)
                                Text("৳${order.shippingFee.toInt()}", fontSize = 13.sp)
                            }
                            if (order.discountAmount > 0) {
                                Row(modifier = Modifier.width(200.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("ডিসকাউন্ট:", fontSize = 13.sp, color = SuccessGreen)
                                    Text("-৳${order.discountAmount.toInt()}", fontSize = 13.sp, color = SuccessGreen)
                                }
                            }
                            HorizontalDivider(modifier = Modifier.width(200.dp), color = GrayBorder)
                            Row(modifier = Modifier.width(200.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("সর্বমোট:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("৳${order.totalAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryRed)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "পেমেন্ট মাধ্যম: ${order.paymentMethod} | ডেলিভারি এলাকা: ${order.deliveryType}",
                            fontSize = 11.sp,
                            color = GrayText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (isPrinted) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))) {
                        Text(
                            text = "ইনভয়েস ডাউনলোড সম্পন্ন হয়েছে! (PDF simulation saved)",
                            modifier = Modifier.padding(12.dp),
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
