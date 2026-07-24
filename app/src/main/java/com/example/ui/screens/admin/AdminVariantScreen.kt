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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AdminVariantScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    var colorsList by remember { mutableStateOf(mutableStateListOf("Crimson Red", "Deep Maroon", "Midnight Black", "Pure White", "Royal Blue", "Golden Yellow", "Emerald Green")) }
    var sizesList by remember { mutableStateOf(mutableStateListOf("38", "40", "42", "44", "46", "S", "M", "L", "XL", "XXL", "Free Size")) }

    var newColor by remember { mutableStateOf("") }
    var newSize by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ভেরিয়েন্ট ম্যানেজমেন্ট (Variants)", fontWeight = FontWeight.Bold) },
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
            // Color Variants Manager
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("১. কালার ভেরিয়েন্ট (Color Variants)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newColor,
                            onValueChange = { newColor = it },
                            placeholder = { Text("যেমন: Olive Green") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newColor.isNotBlank()) {
                                    colorsList.add(newColor.trim())
                                    newColor = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                        ) {
                            Text("যোগ")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        colorsList.forEach { col ->
                            InputChip(
                                selected = true,
                                onClick = { colorsList.remove(col) },
                                label = { Text(col) },
                                trailingIcon = { Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            )
                        }
                    }
                }
            }

            // Size Variants Manager
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("২. সাইজ ভেরিয়েন্ট (Size Variants)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newSize,
                            onValueChange = { newSize = it },
                            placeholder = { Text("যেমন: 48 (Plus Size)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newSize.isNotBlank()) {
                                    sizesList.add(newSize.trim())
                                    newSize = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                        ) {
                            Text("যোগ")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        sizesList.forEach { sz ->
                            InputChip(
                                selected = true,
                                onClick = { sizesList.remove(sz) },
                                label = { Text(sz) },
                                trailingIcon = { Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            )
                        }
                    }
                }
            }
        }
    }
}
