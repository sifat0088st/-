package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
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
fun AdminRolePermissionScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit,
    onSwitchToCustomerStore: () -> Unit
) {
    var selectedRole by remember { mutableStateOf("Super Admin (Full Access)") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("রোল ও পারমিশন (Role & Access)", fontWeight = FontWeight.Bold) },
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
                    Text("এডমিন রোল ও অ্যাক্সেস লেভেল", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryRed)

                    listOf(
                        "Super Admin (Full Access)" to "সকল প্রোডাক্ট, অর্ডার, থিম, SEO ও ব্যাকআপ এক্সেস",
                        "Store Manager" to "শুধুমাত্র প্রোডাক্ট ও অর্ডার পরিচালনা",
                        "Customer Support" to "শুধুমাত্র অর্ডার ট্র্যাকিং ও কাস্টমার রিভিউ উত্তর"
                    ).forEach { (roleTitle, desc) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedRole == roleTitle,
                                onClick = { selectedRole = roleTitle },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimaryRed)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(roleTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(desc, fontSize = 11.sp, color = GrayText)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onSwitchToCustomerStore,
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("exit_admin_role_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = BlackDark)
                    ) {
                        Icon(Icons.Filled.AdminPanelSettings, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("কাস্টমার স্টোর শপিং মোডে ফিরে যান")
                    }
                }
            }
        }
    }
}
