package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.BannerEntity
import com.example.ui.components.LocalOrAsyncImage
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBannerScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val banners by viewModel.allBanners.collectAsState()
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("img_hero_banner1_1784912503553") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ব্যানার ও অফার (Banners)", fontWeight = FontWeight.Bold) },
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
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("নতুন ব্যানার যোগ করুন", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("শিরোনাম") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("উপ-শিরোনাম") }, modifier = Modifier.fillMaxWidth())

                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                viewModel.saveBanner(BannerEntity(title = title, subtitle = subtitle, imageUrl = imageUrl))
                                title = ""
                                subtitle = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("add_banner_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Text("ব্যানার সেভ করুন")
                    }
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(banners) { b ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = WhitePure)) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            LocalOrAsyncImage(imagePath = b.imageUrl, contentDescription = null, modifier = Modifier.size(60.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(b.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(b.subtitle, fontSize = 12.sp, color = GrayText)
                            }
                            IconButton(onClick = { viewModel.deleteBanner(b.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = null, tint = PrimaryRed)
                            }
                        }
                    }
                }
            }
        }
    }
}
