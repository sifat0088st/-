package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun AdminSettingsScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.storeSettings.collectAsState()

    var storeName by remember { mutableStateOf("তিতাস ফ্যাশন") }
    var phone by remember { mutableStateOf("+880 1711223344") }
    var email by remember { mutableStateOf("contact@titasfashion.com") }
    var address by remember { mutableStateOf("ধানমন্ডি, ঢাকা-১২০৫, বাংলাদেশ") }
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(settings) {
        if (settings != null) {
            storeName = settings!!.storeName
            phone = settings!!.phone
            email = settings!!.email
            address = settings!!.address
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ওয়েবসাইট সেটিংস (Settings)", fontWeight = FontWeight.Bold) },
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
                    Text("সাধারণ তথ্য (Store Info)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    OutlinedTextField(
                        value = storeName,
                        onValueChange = { storeName = it },
                        label = { Text("ওয়েবসাইটের নাম *") },
                        modifier = Modifier.fillMaxWidth().testTag("setting_store_name_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("অফিসিয়াল ফোন নম্বর *") },
                        modifier = Modifier.fillMaxWidth().testTag("setting_phone_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("অফিসিয়াল ইমেইল *") },
                        modifier = Modifier.fillMaxWidth().testTag("setting_email_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("শো-রুম / অফিস ঠিকানা *") },
                        modifier = Modifier.fillMaxWidth().testTag("setting_address_input"),
                        minLines = 2
                    )

                    Button(
                        onClick = {
                            if (settings != null) {
                                viewModel.saveSettings(
                                    settings!!.copy(
                                        storeName = storeName,
                                        phone = phone,
                                        email = email,
                                        address = address
                                    )
                                )
                                isSaved = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_store_settings_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("সেটিংস আপডেট করুন")
                    }

                    if (isSaved) {
                        Text("ওয়েবসাইট তথ্য সফলভাবে আপডেট হয়েছে!", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
