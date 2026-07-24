package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DashboardCustomize
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
fun AdminHomepageBuilderScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    var showHeroBanner by remember { mutableStateOf(true) }
    var showFlashSale by remember { mutableStateOf(true) }
    var showFeaturedProducts by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("হোমপেজ লেআউট বিল্ডার", fontWeight = FontWeight.Bold) },
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
                    Text("হোমপেজের সেকশন সমূহ নিয়ন্ত্রণ করুন", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("হিরো ব্যানার স্লাইডার দেখান", fontWeight = FontWeight.SemiBold)
                        Switch(checked = showHeroBanner, onCheckedChange = { showHeroBanner = it })
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("ঈদ ফ্ল্যাশ সেল সেকশন দেখান", fontWeight = FontWeight.SemiBold)
                        Switch(checked = showFlashSale, onCheckedChange = { showFlashSale = it })
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("সেরা কালেকশন গ্রিড দেখান", fontWeight = FontWeight.SemiBold)
                        Switch(checked = showFeaturedProducts, onCheckedChange = { showFeaturedProducts = it })
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { isSaved = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_homepage_layout_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.DashboardCustomize, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("লেআউট সেভ করুন")
                    }

                    if (isSaved) {
                        Text("হোমপেজ লেআউট কনফিগারেশন আপডেট হয়েছে!", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
