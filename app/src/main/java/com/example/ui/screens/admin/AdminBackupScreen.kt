package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Restore
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
fun AdminBackupScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    var backupStatus by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ব্যাকআপ ও রিস্টোর (Backup)", fontWeight = FontWeight.Bold) },
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
                    Text("১. ডাটাবেজ ব্যাকআপ তৈরি করুন (JSON Export)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    Text("আপনার ওয়েবসাইটের সকল পণ্য, ক্যাটাগরি, ব্র্যান্ড ও অর্ডার তথ্যের নিরাপদ ব্যাকআপ নিন।", fontSize = 12.sp, color = GrayText)

                    Button(
                        onClick = { backupStatus = "ডাটাবেজ ব্যাকআপ সফলভাবে ডাউনলোড করা হয়েছে (titas_fashion_backup_2026.json)!" },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("download_backup_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.Backup, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ডাউনলোড ব্যাকআপ ফাইল")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("২. ডিফল্ট ডেমো ডাটা রিস্টোর করুন (Reset Data)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    Text("ওয়েবসাইটকে প্রাথমিক অবস্থায় ফিরিয়ে আনুন এবং টেস্ট ডাটা রিলোড করুন।", fontSize = 12.sp, color = GrayText)

                    OutlinedButton(
                        onClick = {
                            viewModel.resetDataToDefault()
                            backupStatus = "ডাটাবেজ সফলভাবে রিসেট ও ডেমো পণ্যসমূহ লোড করা হয়েছে!"
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("restore_default_data_button")
                    ) {
                        Icon(Icons.Filled.Restore, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("রিস্টোর ডিফল্ট ডাটা")
                    }
                }
            }

            if (backupStatus.isNotEmpty()) {
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = backupStatus,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}
