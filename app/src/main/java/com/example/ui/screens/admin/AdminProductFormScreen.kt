package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ProductEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductFormScreen(
    productId: Long,
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val existingProductState = viewModel.repository.getProductById(productId).collectAsState(initial = null)
    val categories by viewModel.allCategories.collectAsState()
    val brands by viewModel.allBrands.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("TF-PROD-" + (100..999).random()) }
    var priceStr by remember { mutableStateOf("3500") }
    var discountPriceStr by remember { mutableStateOf("2800") }
    var selectedCategoryId by remember { mutableStateOf(1L) }
    var selectedBrandId by remember { mutableStateOf(1L) }
    var stockQuantityStr by remember { mutableStateOf("10") }
    var colorsCsv by remember { mutableStateOf("Crimson,Maroon,Black,Gold") }
    var sizesCsv by remember { mutableStateOf("S,M,L,XL,Free Size") }
    var imageUrls by remember { mutableStateOf("img_product_saree_1784912517093") }
    var isFeatured by remember { mutableStateOf(true) }
    var isFlashSale by remember { mutableStateOf(false) }

    val existingProduct = existingProductState.value

    LaunchedEffect(existingProduct) {
        if (existingProduct != null && productId > 0) {
            title = existingProduct.title
            description = existingProduct.description
            sku = existingProduct.sku
            priceStr = existingProduct.price.toInt().toString()
            discountPriceStr = existingProduct.discountPrice?.toInt()?.toString() ?: ""
            selectedCategoryId = existingProduct.categoryId
            selectedBrandId = existingProduct.brandId
            stockQuantityStr = existingProduct.stockQuantity.toString()
            colorsCsv = existingProduct.colorsCsv
            sizesCsv = existingProduct.sizesCsv
            imageUrls = existingProduct.imageUrls
            isFeatured = existingProduct.isFeatured
            isFlashSale = existingProduct.isFlashSale
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId > 0) "পণ্য এডিট করুন" else "নতুন পণ্য যোগ করুন", fontWeight = FontWeight.Bold) },
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
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = WhitePure) {
                Box(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                val catName = categories.find { it.id == selectedCategoryId }?.name ?: "General"
                                val brandName = brands.find { it.id == selectedBrandId }?.name ?: "Titas Signature"
                                val p = ProductEntity(
                                    id = if (productId > 0) productId else 0L,
                                    title = title,
                                    description = description,
                                    sku = sku,
                                    price = priceStr.toDoubleOrNull() ?: 1000.0,
                                    discountPrice = discountPriceStr.toDoubleOrNull(),
                                    categoryId = selectedCategoryId,
                                    categoryName = catName,
                                    brandId = selectedBrandId,
                                    brandName = brandName,
                                    imageUrls = imageUrls,
                                    stockQuantity = stockQuantityStr.toIntOrNull() ?: 10,
                                    colorsCsv = colorsCsv,
                                    sizesCsv = sizesCsv,
                                    isFeatured = isFeatured,
                                    isFlashSale = isFlashSale
                                )
                                viewModel.saveProduct(p)
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("save_product_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("পণ্য সংরক্ষণ করুন (Save Product)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("পণ্যের নাম (Product Title) *") },
                modifier = Modifier.fillMaxWidth().testTag("product_title_input"),
                singleLine = true
            )

            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU কোড *") },
                modifier = Modifier.fillMaxWidth().testTag("product_sku_input"),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("মূল্য (Price) *") },
                    modifier = Modifier.weight(1f).testTag("product_price_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = discountPriceStr,
                    onValueChange = { discountPriceStr = it },
                    label = { Text("ডিসকাউন্ট মূল্য") },
                    modifier = Modifier.weight(1f).testTag("product_discount_price_input"),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = stockQuantityStr,
                onValueChange = { stockQuantityStr = it },
                label = { Text("স্টক পরিমাণ (Stock Qty)") },
                modifier = Modifier.fillMaxWidth().testTag("product_stock_input"),
                singleLine = true
            )

            OutlinedTextField(
                value = colorsCsv,
                onValueChange = { colorsCsv = it },
                label = { Text("উপলব্ধ রং (Comma separated colors)") },
                modifier = Modifier.fillMaxWidth().testTag("product_colors_input"),
                singleLine = true
            )

            OutlinedTextField(
                value = sizesCsv,
                onValueChange = { sizesCsv = it },
                label = { Text("উপলব্ধ সাইজ (Comma separated sizes)") },
                modifier = Modifier.fillMaxWidth().testTag("product_sizes_input"),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("পণ্যের বিস্তারিত বিবরণ") },
                modifier = Modifier.fillMaxWidth().testTag("product_description_input"),
                minLines = 3
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                Text("ফিচার্ড পণ্য হিসেবে হোমপেজে দেখান")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isFlashSale, onCheckedChange = { isFlashSale = it })
                Text("ফ্ল্যাশ সেলে অন্তর্ভুক্ত করুন")
            }
        }
    }
}
