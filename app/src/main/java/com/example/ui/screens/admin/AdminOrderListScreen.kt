package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Receipt
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
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderListScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val orders by viewModel.allOrders.collectAsState()
    var filterStatus by remember { mutableStateOf("All") }

    val filteredOrders = orders.filter {
        filterStatus == "All" || it.orderStatus == filterStatus
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("অর্ডার ব্যবস্থাপনা (${orders.size})", fontWeight = FontWeight.Bold) },
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
            // Status Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All", "Pending", "Processing", "Shipped", "Delivered").forEach { st ->
                    FilterChip(
                        selected = filterStatus == st,
                        onClick = { filterStatus = st },
                        label = { Text(st, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredOrders) { ord ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WhitePure),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("#${ord.orderNumber}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)
                                StatusBadge(ord.orderStatus)
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text("গ্রাহক: ${ord.customerName} (${ord.customerPhone})", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("ঠিকানা: ${ord.shippingAddress}", fontSize = 12.sp, color = GrayText)
                            Text("মোট মুল্য: ৳${ord.totalAmount.toInt()} (${ord.paymentMethod})", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = GrayBorder)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Order Status Change Dropdown Selector
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("স্ট্যাটাস আপডেট করুন:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

                                var expanded by remember { mutableStateOf(false) }
                                Box {
                                    OutlinedButton(
                                        onClick = { expanded = true },
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(ord.orderStatus, fontSize = 12.sp)
                                    }

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        listOf("Pending", "Processing", "Shipped", "Delivered", "Cancelled").forEach { newStatus ->
                                            DropdownMenuItem(
                                                text = { Text(newStatus) },
                                                onClick = {
                                                    viewModel.updateOrderStatus(ord.id, newStatus)
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
