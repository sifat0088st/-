package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.CouponEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCouponScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val coupons by viewModel.allCoupons.collectAsState()

    var code by remember { mutableStateOf("") }
    var valueStr by remember { mutableStateOf("10") }
    var minOrderStr by remember { mutableStateOf("1000") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("কুপন ও ডিসকাউন্ট (Coupons)", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("নতুন কুপন কোড তৈরি করুন", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("কুপন কোড (যেমন: TITAS15)") }, modifier = Modifier.fillMaxWidth().testTag("coupon_code_input"))
                    OutlinedTextField(value = valueStr, onValueChange = { valueStr = it }, label = { Text("ছাড়ের শতকরা (%)") }, modifier = Modifier.fillMaxWidth().testTag("coupon_value_input"))
                    OutlinedTextField(value = minOrderStr, onValueChange = { minOrderStr = it }, label = { Text("সর্বনিম্ন অর্ডারের পরিমাণ (৳)") }, modifier = Modifier.fillMaxWidth())

                    Button(
                        onClick = {
                            if (code.isNotBlank()) {
                                viewModel.saveCoupon(
                                    CouponEntity(
                                        code = code.trim().uppercase(),
                                        discountType = "PERCENTAGE",
                                        discountValue = valueStr.toDoubleOrNull() ?: 10.0,
                                        minOrderAmount = minOrderStr.toDoubleOrNull() ?: 1000.0,
                                        expiryDate = "2026-12-31"
                                    )
                                )
                                code = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("add_coupon_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Text("কুপন সেভ করুন")
                    }
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(coupons) { c ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = WhitePure)) {
                        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(c.code, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryRed)
                                Text("ছাড়: ${c.discountValue.toInt()}% | মনিমাল অর্ডার: ৳${c.minOrderAmount.toInt()}", fontSize = 12.sp, color = GrayText)
                            }
                            IconButton(onClick = { viewModel.deleteCoupon(c.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = null, tint = PrimaryRed)
                            }
                        }
                    }
                }
            }
        }
    }
}
