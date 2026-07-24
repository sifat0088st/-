package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun OrderSuccessScreen(
    orderNumber: String,
    onNavigateToHome: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onNavigateToInvoice: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = WhitePure) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { onNavigateToInvoice(orderNumber) },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("view_invoice_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.ReceiptLong, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ইনভয়েস ডাউনলোড / প্রিন্ট করুন")
                    }

                    OutlinedButton(
                        onClick = onNavigateToTracking,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.LocalShipping, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("অর্ডার ট্র্যাক করুন")
                    }

                    TextButton(
                        onClick = onNavigateToHome,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("হোম পেজে ফিরে যান", color = PrimaryRed)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.size(90.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Success",
                            tint = SuccessGreen,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                Text(
                    text = "অভিনন্দন! আপনার অর্ডারটি সফল হয়েছে!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackDark
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = PrimaryRedLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("আপনার অর্ডার নম্বর (Order ID):", fontSize = 13.sp, color = PrimaryRedDark)
                        Text(
                            text = orderNumber,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryRed
                        )
                    }
                }

                Text(
                    text = "তিতাস ফ্যাশন থেকে কেনাকাটা করার জন্য আপনাকে ধন্যবাদ। শীঘ্রই আমাদের প্রতিনিধি আপনার সাথে ফোনে যোগাযোগ করবে।",
                    fontSize = 13.sp,
                    color = GrayText,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
