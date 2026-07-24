package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSeoScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    var metaTitle by remember { mutableStateOf("তিতাস ফ্যাশন - এক্সক্লুসিভ শাড়ি, পাঞ্জাবী ও লেডিস পোশাক") }
    var metaDescription by remember { mutableStateOf("বাংলাদেশের প্রিমিয়াম কোয়ালিটি শাড়ি, পাঞ্জাবী ও ফ্যাশন কালেকশনের বিশ্বস্ত অনলাইন শপ।") }
    var metaKeywords by remember { mutableStateOf("titas fashion, saree, panjabi, kurti, eid collection, bd fashion") }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SEO সেটিংস", fontWeight = FontWeight.Bold) },
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
                    Text("সার্চ ইঞ্জিন অপটিমাইজেশন (Search Engine Optimization)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    OutlinedTextField(
                        value = metaTitle,
                        onValueChange = { metaTitle = it },
                        label = { Text("মেটা টাইটেল (Meta Title)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = metaDescription,
                        onValueChange = { metaDescription = it },
                        label = { Text("মেটা ডেসক্রিপশন (Meta Description)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    OutlinedTextField(
                        value = metaKeywords,
                        onValueChange = { metaKeywords = it },
                        label = { Text("মেটা কিওয়ার্ডস (Meta Keywords)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Button(
                        onClick = { isSaved = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SEO সেটিংস সেভ করুন")
                    }

                    if (isSaved) {
                        Text("SEO মেটাডাটা সফলভাবে কনফিগার করা হয়েছে!", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
