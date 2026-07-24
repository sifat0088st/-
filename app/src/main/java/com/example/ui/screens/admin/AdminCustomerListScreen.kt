package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCustomerListScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val orders by viewModel.allOrders.collectAsState()

    val customers = remember(orders) {
        orders.groupBy { it.customerPhone }.map { (phone, orderList) ->
            Triple(orderList.first().customerName, phone, orderList)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("গ্রাহক তালিকা (Customers)", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(customers) { (name, phone, userOrders) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WhitePure),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryRedLight,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = PrimaryRed)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(phone, fontSize = 12.sp, color = GrayText)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("${userOrders.size} টি অর্ডার", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryRed)
                            Text("৳${userOrders.sumOf { it.totalAmount }.toInt()}", fontSize = 11.sp, color = GrayText)
                        }
                    }
                }
            }
        }
    }
}
