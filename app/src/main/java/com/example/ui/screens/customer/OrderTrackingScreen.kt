package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.OrderEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.StoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToInvoice: (String) -> Unit
) {
    var searchOrderNumber by remember { mutableStateOf("TF-2026-8891") }
    var searchedOrder by remember { mutableStateOf<OrderEntity?>(null) }
    var isSearching by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val found = viewModel.repository.getOrderByNumber("TF-2026-8891")
        if (found != null) {
            searchedOrder = found
            hasSearched = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("অর্ডার ট্র্যাকিং (Order Tracking)", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("অর্ডার আইডি দিয়ে ট্র্যাক করুন", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = searchOrderNumber,
                            onValueChange = { searchOrderNumber = it },
                            placeholder = { Text("যেমন: TF-2026-8891", fontSize = 13.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("tracking_order_id_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (searchOrderNumber.isNotBlank()) {
                                    isSearching = true
                                    coroutineScope.launch {
                                        searchedOrder = viewModel.repository.getOrderByNumber(searchOrderNumber.trim())
                                        hasSearched = true
                                        isSearching = false
                                    }
                                }
                            },
                            modifier = Modifier.height(50.dp).testTag("search_tracking_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                        ) {
                            Text("সার্চ")
                        }
                    }
                }
            }

            if (hasSearched) {
                val order = searchedOrder
                if (order == null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Text(
                            text = "দুঃখিত, এই অর্ডার নম্বরের কোনো তথ্য পাওয়া যায়নি।",
                            modifier = Modifier.padding(16.dp),
                            color = PrimaryRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WhitePure),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("অর্ডার নম্বর: ${order.orderNumber}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("ট্র্যাকিং নম্বর: ${order.trackingNumber.ifEmpty { "N/A" }}", fontSize = 12.sp, color = GrayText)
                                }
                                StatusBadge(order.orderStatus)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = GrayBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("স্ট্যাটাস টাইমলাইন (Tracking Timeline)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))

                            val steps = listOf("Pending", "Processing", "Shipped", "Delivered")
                            val currentIdx = steps.indexOf(order.orderStatus).let { if (it < 0) 0 else it }

                            steps.forEachIndexed { index, stepName ->
                                val isCompleted = index <= currentIdx
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isCompleted) SuccessGreen else GrayBorder,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            if (isCompleted) {
                                                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            } else {
                                                Text("${index + 1}", fontSize = 10.sp, color = Color.White)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = when(stepName) {
                                                "Pending" -> "অর্ডার গৃহীত হয়েছে (Pending)"
                                                "Processing" -> "প্যাকিং ও প্রসেসিং চলছে (Processing)"
                                                "Shipped" -> "কুরিয়ারে পাঠানো হয়েছে (Shipped)"
                                                "Delivered" -> "ডেলিভারি সম্পন্ন (Delivered)"
                                                else -> stepName
                                            },
                                            fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isCompleted) BlackDark else GrayText,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = { onNavigateToInvoice(order.orderNumber) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Filled.Receipt, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("ইনভয়েস দেখুন")
                            }
                        }
                    }
                }
            }
        }
    }
}
