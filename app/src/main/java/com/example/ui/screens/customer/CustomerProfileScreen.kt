package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun CustomerProfileScreen(
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToReturnRefund: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onNavigateToInvoice: (String) -> Unit,
    onSwitchToAdmin: () -> Unit
) {
    val orders by viewModel.repository.allOrders.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("মাই অ্যাকাউন্ট (My Account)", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WhitePure),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryRedLight,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = PrimaryRed, modifier = Modifier.size(32.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("সাইফুল ইসলাম", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("01819283746", fontSize = 13.sp, color = GrayText)
                            Text("saiful@gmail.com", fontSize = 12.sp, color = GrayText)
                        }

                        Button(
                            onClick = onSwitchToAdmin,
                            colors = ButtonDefaults.buttonColors(containerColor = BlackDark),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_panel_profile_button")
                        ) {
                            Text("এডমিন মোড", fontSize = 11.sp)
                        }
                    }
                }
            }

            // Quick Menu Items
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WhitePure),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column {
                        ListItem(
                            headlineContent = { Text("মাই উইশলিস্ট") },
                            leadingContent = { Icon(Icons.Filled.Favorite, contentDescription = null, tint = PrimaryRed) },
                            modifier = Modifier.clickable { onNavigateToWishlist() }
                        )
                        HorizontalDivider(color = GrayBorder)
                        ListItem(
                            headlineContent = { Text("অর্ডার ট্র্যাকিং") },
                            leadingContent = { Icon(Icons.Filled.LocalShipping, contentDescription = null, tint = PrimaryRed) },
                            modifier = Modifier.clickable { onNavigateToTracking() }
                        )
                        HorizontalDivider(color = GrayBorder)
                        ListItem(
                            headlineContent = { Text("রিটার্ন ও রিফান্ড রিকোয়েস্ট") },
                            leadingContent = { Icon(Icons.Filled.AssignmentReturn, contentDescription = null, tint = PrimaryRed) },
                            modifier = Modifier.clickable { onNavigateToReturnRefund() }
                        )
                    }
                }
            }

            // Recent Orders Header
            item {
                Text("আমার সাম্প্রতিক অর্ডারসমূহ (${orders.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            items(orders) { ord ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WhitePure),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("মোট মূল্য: ৳${ord.totalAmount.toInt()} (${ord.paymentMethod})", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("ঠিকানা: ${ord.shippingAddress}", fontSize = 12.sp, color = GrayText)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            OutlinedButton(
                                onClick = { onNavigateToInvoice(ord.orderNumber) },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Icon(Icons.Filled.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ইনভয়েস", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
