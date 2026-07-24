package com.example.ui.screens.customer

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSupportScreen(
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.storeSettings.collectAsState()

    var messageSubject by remember { mutableStateOf("") }
    var messageBody by remember { mutableStateOf("") }
    var isSent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("কাস্টমার সাপোর্ট (Support)", fontWeight = FontWeight.Bold) },
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
            // Direct Contact Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("যোগাযোগের ঠিকানা (Contact Details)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Phone, contentDescription = null, tint = PrimaryRed)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(settings?.phone ?: "+880 1711-223344", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Email, contentDescription = null, tint = PrimaryRed)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(settings?.email ?: "info@titasfashion.com", fontSize = 14.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = PrimaryRed)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(settings?.address ?: "ধানমন্ডি প্লাজা, ঢাকা", fontSize = 13.sp)
                    }
                }
            }

            // Message Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("সরাসরি বার্তা পাঠান (Send Message)", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                    if (isSent) {
                        Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp)) {
                            Text(
                                "আপনার বার্তাটি আমরা পেয়েছি! অতিসত্বর উত্তর দেওয়া হবে।",
                                modifier = Modifier.padding(12.dp),
                                color = SuccessGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        OutlinedTextField(
                            value = messageSubject,
                            onValueChange = { messageSubject = it },
                            label = { Text("বিষয় (Subject)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = messageBody,
                            onValueChange = { messageBody = it },
                            label = { Text("আপনার প্রশ্ন বা সমস্যা") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Button(
                            onClick = {
                                if (messageSubject.isNotBlank() && messageBody.isNotBlank()) {
                                    isSent = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("বার্তা পাঠান")
                        }
                    }
                }
            }

            // Frequently Asked Questions
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("সাধারণ প্রশ্নাবলী (FAQ)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("প্রশ্ন: ডেলিভারি পেতে কতদিন সময় লাগে?", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("উত্তর: ঢাকার ভেতরে ২৪-৪৮ ঘণ্টা এবং ঢাকার বাইরে ২-৩ কর্মদিবস।", fontSize = 12.sp, color = GrayText)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("প্রশ্ন: ক্যাশ অন ডেলিভারি কি এভেলেবল?", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("উত্তর: হ্যাঁ, সারাদেশে ক্যাশ অন ডেলিভারি সুবিধা রয়েছে।", fontSize = 12.sp, color = GrayText)
                }
            }
        }
    }
}
