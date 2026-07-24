package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminThemeCustomizerScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedThemeColor by remember { mutableStateOf("Red + White + Black (Titas Signature)") }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("থিম কাস্টমাইজেশন", fontWeight = FontWeight.Bold) },
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
                    Text("ওয়েবসাইটের কালার থিম নির্ধারণ করুন", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    listOf(
                        "Red + White + Black (Titas Signature)" to Color(0xFFC8102E),
                        "Crimson Maroon & Gold" to Color(0xFF800000),
                        "Dark Charcoal & White" to Color(0xFF1E1E1E)
                    ).forEach { (themeName, col) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedThemeColor = themeName }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = selectedThemeColor == themeName, onClick = { selectedThemeColor = themeName })
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(col))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(themeName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { isSaved = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.Palette, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("থিম পরিবর্তন সংরক্ষণ করুন")
                    }

                    if (isSaved) {
                        Text("ব্র্যান্ড কালার থিম সফলভাবে সেট করা হয়েছে!", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
