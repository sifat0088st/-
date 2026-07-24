package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
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
fun AdminPaymentScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    var isCodEnabled by remember { mutableStateOf(true) }
    var bkashNumber by remember { mutableStateOf("01711223344") }
    var nagadNumber by remember { mutableStateOf("01711223344") }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("পেমেন্ট গেটওয়ে সেটিং", fontWeight = FontWeight.Bold) },
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
                    Text("পেমেন্ট চ্যানেল সেটিংস (Payment Gateways)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ক্যাশ অন ডেলিভারি (COD) সক্রিয় রাখুন", fontWeight = FontWeight.SemiBold)
                        Switch(checked = isCodEnabled, onCheckedChange = { isCodEnabled = it })
                    }

                    OutlinedTextField(
                        value = bkashNumber,
                        onValueChange = { bkashNumber = it },
                        label = { Text("বিকাশ পার্সোনাল / মার্চেন্ট নম্বর") },
                        modifier = Modifier.fillMaxWidth().testTag("bkash_number_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = nagadNumber,
                        onValueChange = { nagadNumber = it },
                        label = { Text("নগদ পার্সোনাল / মার্চেন্ট নম্বর") },
                        modifier = Modifier.fillMaxWidth().testTag("nagad_number_input"),
                        singleLine = true
                    )

                    Button(
                        onClick = { isSaved = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_payment_settings_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.Payment, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("পেমেন্ট সেটিংস সেভ করুন")
                    }

                    if (isSaved) {
                        Text("পেমেন্ট সেটিংস সফলভাবে আপডেট হয়েছে!", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
