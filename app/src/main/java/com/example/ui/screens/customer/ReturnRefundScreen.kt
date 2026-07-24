package com.example.ui.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentReturn
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
import com.example.ui.viewmodel.StoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnRefundScreen(
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit
) {
    var orderNumber by remember { mutableStateOf("TF-2026-8891") }
    var phone by remember { mutableStateOf("01819283746") }
    var reason by remember { mutableStateOf("সাইজে সমস্যা / কালার পছন্দ হয়নি") }
    var refundMethod by remember { mutableStateOf("bKash") }
    var accountNumber by remember { mutableStateOf("01819283746") }

    var isSubmitting by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("রিটার্ন ও রিফান্ড রিকুয়েস্ট", fontWeight = FontWeight.Bold) },
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
                colors = CardDefaults.cardColors(containerColor = PrimaryRedLight),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AssignmentReturn, contentDescription = null, tint = PrimaryRed, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("৭ দিনের গ্যারান্টিযুক্ত রিটার্ন পলিসি", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryRedDark)
                        Text("পণ্য হাতে পাওয়ার ৭ দিনের মধ্যে যেকোনো সমস্যায় রিটার্ন বা পরিবর্তন সম্ভব।", fontSize = 11.sp, color = BlackDark)
                    }
                }
            }

            if (isSubmitted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("আপনার রিটার্ন রিকোয়েস্ট সফলভাবে জমা হয়েছে!", fontWeight = FontWeight.Bold, color = SuccessGreen, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("২৪ ঘণ্টার মধ্যে আমাদের সাপোর্ট টিম আপনার সাথে যোগাযোগ করবে।", fontSize = 13.sp, color = BlackDark)
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WhitePure),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("আবেদন ফর্ম (Return Request Form)", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        OutlinedTextField(
                            value = orderNumber,
                            onValueChange = { orderNumber = it },
                            label = { Text("অর্ডার নম্বর *") },
                            modifier = Modifier.fillMaxWidth().testTag("return_order_id_input"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("মোবাইল নম্বর *") },
                            modifier = Modifier.fillMaxWidth().testTag("return_phone_input"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = reason,
                            onValueChange = { reason = it },
                            label = { Text("রিটার্নের কারণ *") },
                            modifier = Modifier.fillMaxWidth().testTag("return_reason_input"),
                            minLines = 2
                        )

                        OutlinedTextField(
                            value = refundMethod,
                            onValueChange = { refundMethod = it },
                            label = { Text("রিফান্ড গ্রহণের মাধ্যম (যেমন: bKash/Nagad)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = accountNumber,
                            onValueChange = { accountNumber = it },
                            label = { Text("বিকাশ/নগদ অ্যাকাউন্ট নম্বর *") },
                            modifier = Modifier.fillMaxWidth().testTag("return_account_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (orderNumber.isNotBlank() && phone.isNotBlank()) {
                                    isSubmitting = true
                                    coroutineScope.launch {
                                        viewModel.submitReturnRequest(orderNumber, phone, reason, refundMethod, accountNumber)
                                        isSubmitting = false
                                        isSubmitted = true
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_return_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text("আবেদন জমা দিন")
                            }
                        }
                    }
                }
            }
        }
    }
}
