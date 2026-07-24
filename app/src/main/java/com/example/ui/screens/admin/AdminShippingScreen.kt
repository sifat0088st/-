package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminShippingScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.storeSettings.collectAsState()

    var insideFeeStr by remember { mutableStateOf("80") }
    var outsideFeeStr by remember { mutableStateOf("150") }
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(settings) {
        if (settings != null) {
            insideFeeStr = settings!!.insideDhakaFee.toInt().toString()
            outsideFeeStr = settings!!.outsideDhakaFee.toInt().toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("শিপিং ও ডেলিভারি চার্জ", fontWeight = FontWeight.Bold) },
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
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("ডেলিভারি চার্জ নির্ধারণ করুন (Shipping Rates)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    OutlinedTextField(
                        value = insideFeeStr,
                        onValueChange = { insideFeeStr = it },
                        label = { Text("ঢাকার ভেতরে ডেলিভারি চার্জ (৳)") },
                        modifier = Modifier.fillMaxWidth().testTag("inside_dhaka_fee_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = outsideFeeStr,
                        onValueChange = { outsideFeeStr = it },
                        label = { Text("ঢাকার বাইরে ডেলিভারি চার্জ (৳)") },
                        modifier = Modifier.fillMaxWidth().testTag("outside_dhaka_fee_input"),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (settings != null) {
                                viewModel.saveSettings(
                                    settings!!.copy(
                                        insideDhakaFee = insideFeeStr.toDoubleOrNull() ?: 80.0,
                                        outsideDhakaFee = outsideFeeStr.toDoubleOrNull() ?: 150.0
                                    )
                                )
                                isSaved = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_shipping_fee_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.LocalShipping, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("চার্জ সংরক্ষণ করুন")
                    }

                    if (isSaved) {
                        Text("শিপিং চার্জ সফলভাবে আপডেট হয়েছে!", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
