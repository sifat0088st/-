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
import com.example.data.local.entities.BrandEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBrandScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val brands by viewModel.allBrands.collectAsState()
    var newBrandName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ব্র্যান্ড ব্যবস্থাপনা (Brands)", fontWeight = FontWeight.Bold) },
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
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newBrandName,
                        onValueChange = { newBrandName = it },
                        label = { Text("নতুন ব্র্যান্ড নাম") },
                        modifier = Modifier.weight(1f).testTag("new_brand_name_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (newBrandName.isNotBlank()) {
                                viewModel.saveBrand(BrandEntity(name = newBrandName.trim()))
                                newBrandName = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                        modifier = Modifier.testTag("add_brand_button")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text("যোগ করুন")
                    }
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(brands) { brand ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WhitePure)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(brand.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            IconButton(onClick = { viewModel.deleteBrand(brand.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = PrimaryRed)
                            }
                        }
                    }
                }
            }
        }
    }
}
