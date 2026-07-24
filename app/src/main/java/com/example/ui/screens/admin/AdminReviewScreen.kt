package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StarRatingView
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReviewScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val reviews by viewModel.allReviews.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("গ্রাহক রিভিউ মডারেশন", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(reviews) { rev ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WhitePure),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(rev.customerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(if (rev.isApproved) "Approved" else "Pending Approval", fontSize = 11.sp, color = if (rev.isApproved) SuccessGreen else WarningOrange, fontWeight = FontWeight.Bold)
                        }

                        StarRatingView(rating = rev.rating.toFloat(), starSize = 14.dp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(rev.comment, fontSize = 13.sp, color = BlackDark)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            if (!rev.isApproved) {
                                Button(
                                    onClick = { viewModel.setReviewApproval(rev.id, true) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("অনুমোদন দিন")
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { viewModel.setReviewApproval(rev.id, false) },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("হাইড করুন")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
