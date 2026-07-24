package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
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
fun AdminFlashSaleScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.storeSettings.collectAsState()

    var flashTitle by remember { mutableStateOf("ঈদ ফ্ল্যাশ সেল (Eid Special Offer)") }
    var isEnabled by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(settings) {
        if (settings != null) {
            flashTitle = settings!!.flashSaleTitle
            isEnabled = settings!!.flashSaleActive
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ফ্ল্যাশ সেল সেটিংস (Flash Sale)", fontWeight = FontWeight.Bold) },
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
                    Text("ফ্ল্যাশ সেল ক্যাম্পেইন কাস্টমাইজ", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    OutlinedTextField(
                        value = flashTitle,
                        onValueChange = { flashTitle = it },
                        label = { Text("ক্যাম্পেইনের টাইটেল") },
                        modifier = Modifier.fillMaxWidth().testTag("flash_sale_title_input"),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("হোমপেজে ফ্ল্যাশ সেল সেকশন চালু রাখুন", fontWeight = FontWeight.SemiBold)
                        Switch(checked = isEnabled, onCheckedChange = { isEnabled = it })
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (settings != null) {
                                viewModel.saveSettings(settings!!.copy(flashSaleTitle = flashTitle, flashSaleActive = isEnabled))
                                isSaved = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_flash_sale_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.Bolt, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("সেটিংস সেভ করুন")
                    }

                    if (isSaved) {
                        Text("ফ্ল্যাশ সেল সেটিংস সফলভাবে আপডেট করা হয়েছে!", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
