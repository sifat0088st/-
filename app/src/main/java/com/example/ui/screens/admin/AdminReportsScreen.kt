package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
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
fun AdminReportsScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val orderCount by viewModel.orderCount.collectAsState()
    val productCount by viewModel.productCount.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("রিপোর্ট ও এনালাইটিক্স (Reports)", fontWeight = FontWeight.Bold) },
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
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("বিক্রি ও আর্থিক রিপোর্ট (Sales Report)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("মোট অর্জিত রেভিনিউ:", fontSize = 13.sp)
                        Text("৳${(totalRevenue ?: 0.0).toInt()}", fontWeight = FontWeight.Bold, color = PrimaryRed, fontSize = 15.sp)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("মোট ডেলিভারিকৃত অর্ডার:", fontSize = 13.sp)
                        Text("$orderCount টি", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("মোট একটিভ প্রোডাক্ট ক্যাটালগ:", fontSize = 13.sp)
                        Text("$productCount টি", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("বিক্রির গ্রাফ (Sales Performance Visualizer):", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    // Simulated Visual Bar Chart Component
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        listOf("Jan" to 0.4f, "Feb" to 0.6f, "Mar" to 0.8f, "Apr" to 0.5f, "May" to 0.9f, "Jun" to 0.7f, "Jul" to 1.0f).forEach { (month, heightPct) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .fillMaxHeight(heightPct)
                                        .background(PrimaryRed, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(month, fontSize = 10.sp, color = GrayText)
                            }
                        }
                    }
                }
            }
        }
    }
}
