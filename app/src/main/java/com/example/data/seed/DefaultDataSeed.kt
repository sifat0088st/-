package com.example.data.seed

import com.example.data.local.AppDatabase
import com.example.data.local.entities.*
import kotlinx.coroutines.flow.firstOrNull

object DefaultDataSeed {

    suspend fun seedIfEmpty(database: AppDatabase) {
        val productDao = database.productDao()
        val categoryDao = database.categoryDao()
        val brandDao = database.brandDao()
        val bannerDao = database.bannerDao()
        val couponDao = database.couponDao()
        val settingsDao = database.settingsDao()
        val orderDao = database.orderDao()

        // Check if settings exist
        if (settingsDao.getSettings().firstOrNull() == null) {
            settingsDao.insertOrUpdateSettings(
                StoreSettingsEntity(
                    id = 1,
                    storeName = "তিতাস ফ্যাশন",
                    phone = "+880 1711-223344",
                    email = "support@titasfashion.com",
                    address = "Level-4, Shop #402, Dhanmondi Plaza, Dhaka-1205",
                    insideDhakaFee = 80.0,
                    outsideDhakaFee = 150.0,
                    bkashNumber = "01711223344",
                    nagadNumber = "01711223344",
                    codEnabled = true,
                    metaTitle = "তিতাস ফ্যাশন | Exclusive Bengali Ethnic Wear",
                    metaDescription = "Shop premium sarees, panjabis, salwar kameez, and trendy fashion directly from Titas Fashion.",
                    metaKeywords = "fashion, saree, panjabi, clothing, bangladesh, titas fashion",
                    flashSaleActive = true,
                    flashSaleTitle = "Eid Festival Flash Sale",
                    flashSaleDiscountPercent = 25
                )
            )
        }

        // Seed Categories if empty
        val categoriesCount = categoryDao.getAllCategories().firstOrNull()?.size ?: 0
        if (categoriesCount == 0) {
            categoryDao.insertCategory(CategoryEntity(id = 1, name = "Saree (শাড়ি)", iconName = "Saree", productCount = 8))
            categoryDao.insertCategory(CategoryEntity(id = 2, name = "Panjabi (পাঞ্জাবী)", iconName = "Panjabi", productCount = 6))
            categoryDao.insertCategory(CategoryEntity(id = 3, name = "Salwar Kameez (শালওয়ার কামিজ)", iconName = "Dress", productCount = 5))
            categoryDao.insertCategory(CategoryEntity(id = 4, name = "Western & Kurtis (কুর্তি)", iconName = "Kurti", productCount = 4))
            categoryDao.insertCategory(CategoryEntity(id = 5, name = "Kids Fashion (বাচ্চাদের পোশাক)", iconName = "Kids", productCount = 3))
            categoryDao.insertCategory(CategoryEntity(id = 6, name = "Jewelry & Accessories", iconName = "Ring", productCount = 2))
        }

        // Seed Brands if empty
        val brandsCount = brandDao.getAllBrands().firstOrNull()?.size ?: 0
        if (brandsCount == 0) {
            brandDao.insertBrand(BrandEntity(id = 1, name = "Titas Signature", description = "Premium handloom and silk collection by Titas Fashion"))
            brandDao.insertBrand(BrandEntity(id = 2, name = "Jamdani Craft", description = "Authentic Dhakai Jamdani weavers"))
            brandDao.insertBrand(BrandEntity(id = 3, name = "Heritage Cotton", description = "100% pure organic Tangail cotton"))
            brandDao.insertBrand(BrandEntity(id = 4, name = "Royal Silk", description = "Rajshahi Raj silk & Katan sarees"))
        }

        // Seed Banners if empty
        val bannersCount = bannerDao.getAllBanners().firstOrNull()?.size ?: 0
        if (bannersCount == 0) {
            bannerDao.insertBanner(
                BannerEntity(
                    id = 1,
                    title = "ঈদ স্পেশাল নতুন কালেকশন",
                    subtitle = "প্রিমিয়াম শাড়ি ও পাঞ্জাবীতে ২৫% পর্যন্ত ছাড়",
                    imageUrl = "img_hero_banner1_1784912503553",
                    targetCategory = "All"
                )
            )
            bannerDao.insertBanner(
                BannerEntity(
                    id = 2,
                    title = "রয়েল জামদানি ও রাজশাহী সিল্ক",
                    subtitle = "হাতে তৈরি ঐতিহ্যবাহী শাড়ি সমগ্র",
                    imageUrl = "img_product_saree_1784912517093",
                    targetCategory = "Saree (শাড়ি)"
                )
            )
        }

        // Seed Coupons if empty
        val couponsCount = couponDao.getAllCoupons().firstOrNull()?.size ?: 0
        if (couponsCount == 0) {
            couponDao.insertCoupon(CouponEntity(id = 1, code = "TITAS10", discountType = "PERCENTAGE", discountValue = 10.0, minOrderAmount = 1000.0))
            couponDao.insertCoupon(CouponEntity(id = 2, code = "EID2026", discountType = "FIXED", discountValue = 300.0, minOrderAmount = 2500.0))
            couponDao.insertCoupon(CouponEntity(id = 3, code = "FIRSTBUY", discountType = "FIXED", discountValue = 150.0, minOrderAmount = 500.0))
        }

        // Seed Products if empty
        val productsCount = productDao.getProductCount().firstOrNull() ?: 0
        if (productsCount == 0) {
            productDao.insertProduct(
                ProductEntity(
                    id = 1,
                    title = "রয়েল কাতান সিল্ক শাড়ি (Crimson Royal Katan Silk Saree)",
                    description = "প্রিমিয়াম কোয়ালিটির ট্র্যাডিশনাল রাজকীয় লাল কাতান সিল্ক শাড়ি। গোল্ডেন জারি সুতোর নিখুঁত বুনন নকশা। বিবাহ, উৎসব ও বিশেষ অনুষ্ঠানের জন্য আদর্শ। জ্যাকেটের ব্লাউজ পিস সহ।",
                    sku = "TF-SAREE-001",
                    price = 4850.0,
                    discountPrice = 3890.0,
                    categoryId = 1,
                    categoryName = "Saree (শাড়ি)",
                    brandId = 1,
                    brandName = "Titas Signature",
                    imageUrls = "img_product_saree_1784912517093",
                    isFeatured = true,
                    isFlashSale = true,
                    stockQuantity = 12,
                    colorsCsv = "Crimson Red,Maroon,Royal Blue,Golden",
                    sizesCsv = "Free Size",
                    rating = 4.9f,
                    reviewCount = 28
                )
            )

            productDao.insertProduct(
                ProductEntity(
                    id = 2,
                    title = "ডিজাইনার প্রিমিয়াম কটন পাঞ্জাবী (Embroidered Cotton Panjabi)",
                    description = "অভিজাত ও আরামদায়ক ১০০% পিওর কটন ফেব্রিকের এক্সক্লুসিভ পাঞ্জাবী। কলার ও প্ল্যাটেকে নিখুঁত সুতোর সূক্ষ্ম এমব্রয়ডারি ওয়ার্ক। আধুনিক ফিটিং ও মার্জিত লুক।",
                    sku = "TF-PANJ-002",
                    price = 2850.0,
                    discountPrice = 2250.0,
                    categoryId = 2,
                    categoryName = "Panjabi (পাঞ্জাবী)",
                    brandId = 1,
                    brandName = "Titas Signature",
                    imageUrls = "img_product_panjabi_1784912531316",
                    isFeatured = true,
                    isFlashSale = true,
                    stockQuantity = 15,
                    colorsCsv = "Maroon,Navy Blue,White,Black",
                    sizesCsv = "M,L,XL,XXL",
                    rating = 4.8f,
                    reviewCount = 19
                )
            )

            productDao.insertProduct(
                ProductEntity(
                    id = 3,
                    title = "ঢাকাই অরিজিনাল জামদানি শাড়ি (Original Dhakai Jamdani Saree)",
                    description = "ঐতিহ্যবাহী তাঁতিদের হাতে বোনা আদি ঢাকাই জামদানি শাড়ি। সূক্ষ্ম সুতোর কাজ ও চমৎকার লাল-সাদা কম্বিনেশন। অত্যন্ত হালকা ও পরা আরামদায়ক।",
                    sku = "TF-JAM-003",
                    price = 6500.0,
                    discountPrice = 5400.0,
                    categoryId = 1,
                    categoryName = "Saree (শাড়ি)",
                    brandId = 2,
                    brandName = "Jamdani Craft",
                    imageUrls = "img_product_saree_1784912517093",
                    isFeatured = true,
                    isFlashSale = false,
                    stockQuantity = 5,
                    colorsCsv = "Red & White,Black & Gold,Pastel Green",
                    sizesCsv = "Free Size",
                    rating = 5.0f,
                    reviewCount = 42
                )
            )

            productDao.insertProduct(
                ProductEntity(
                    id = 4,
                    title = "প্রিমিয়াম ৩-পিস শালওয়ার কামিজ (3-Piece Designer Salwar Kameez)",
                    description = "ডিজাইনার ৩-পিস সেটে রয়েছে এমব্রয়ডার্ড কামিজ, সেমি-স্টিচড শালওয়ার এবং ম্যাচিং নেট ওড়না। আধুনিক পার্টি ওয়্যার গর্জিয়াস আউটফিট।",
                    sku = "TF-SK-004",
                    price = 3950.0,
                    discountPrice = 3200.0,
                    categoryId = 3,
                    categoryName = "Salwar Kameez (শালওয়ার কামিজ)",
                    brandId = 1,
                    brandName = "Titas Signature",
                    imageUrls = "img_hero_banner1_1784912503553",
                    isFeatured = false,
                    isFlashSale = true,
                    stockQuantity = 8,
                    colorsCsv = "Red,Emerald,Plum,Gold",
                    sizesCsv = "S,M,L,XL",
                    rating = 4.7f,
                    reviewCount = 14
                )
            )

            productDao.insertProduct(
                ProductEntity(
                    id = 5,
                    title = "প্রিন্টেড লেডিস ট্র্যাডিশনাল কুর্তি (Floral Designer Kurti)",
                    description = "ক্যাজুয়াল ও ডেলি ওয়্যারের জন্য স্টাইলিশ ১০০% সফট কটন প্রিন্টেড কুর্তি। আরামদায়ক ও আকর্ষণীয় প্যাটার্ন।",
                    sku = "TF-KUR-005",
                    price = 1450.0,
                    discountPrice = 1150.0,
                    categoryId = 4,
                    categoryName = "Western & Kurtis (কুর্তি)",
                    brandId = 3,
                    brandName = "Heritage Cotton",
                    imageUrls = "img_product_saree_1784912517093",
                    isFeatured = false,
                    isFlashSale = false,
                    stockQuantity = 20,
                    colorsCsv = "Red,White,Mustard",
                    sizesCsv = "S,M,L,XL",
                    rating = 4.6f,
                    reviewCount = 9
                )
            )

            productDao.insertProduct(
                ProductEntity(
                    id = 6,
                    title = "রাজশাহী সিল্ক শাড়ি (Rajshahi Pure Silk Saree)",
                    description = "রাজশাহীর খাঁটি রেশম গুটি সিল্ক শাড়ি। অত্যন্ত মসৃণ টেক্সচার ও মনমুগ্ধকর ট্র্যাডিশনাল আচল নকশা।",
                    sku = "TF-SLK-006",
                    price = 5200.0,
                    discountPrice = null,
                    categoryId = 1,
                    categoryName = "Saree (শাড়ি)",
                    brandId = 4,
                    brandName = "Royal Silk",
                    imageUrls = "img_product_saree_1784912517093",
                    isFeatured = true,
                    isFlashSale = false,
                    stockQuantity = 2, // Low stock for alert demonstration!
                    colorsCsv = "Crimson,Gold,Black",
                    sizesCsv = "Free Size",
                    rating = 4.9f,
                    reviewCount = 31
                )
            )
        }

        // Seed Sample Orders if empty
        val ordersCount = orderDao.getOrderCount().firstOrNull() ?: 0
        if (ordersCount == 0) {
            val orderId = orderDao.insertOrder(
                OrderEntity(
                    id = 1,
                    orderNumber = "TF-2026-8891",
                    customerName = "সাইফুল ইসলাম (Saiful Islam)",
                    customerPhone = "01819283746",
                    customerEmail = "saiful@gmail.com",
                    shippingAddress = "বাসা #৪৫, রোড #২, ধানমন্ডি, ঢাকা",
                    district = "Dhaka",
                    deliveryType = "Inside Dhaka",
                    paymentMethod = "bKash",
                    subtotal = 6140.0,
                    shippingFee = 80.0,
                    discountAmount = 300.0,
                    totalAmount = 5920.0,
                    orderStatus = "Processing",
                    trackingNumber = "TITAS-TRK-9812",
                    note = "অনুগ্রহ করে আজ বিকালের মধ্যে ডেলিভারি করবেন।"
                )
            )
            orderDao.insertOrderItems(
                listOf(
                    OrderItemEntity(
                        orderId = orderId,
                        productId = 1,
                        productTitle = "রয়েল কাতান সিল্ক শাড়ি",
                        productImage = "img_product_saree_1784912517093",
                        selectedColor = "Crimson Red",
                        selectedSize = "Free Size",
                        unitPrice = 3890.0,
                        quantity = 1,
                        totalPrice = 3890.0
                    ),
                    OrderItemEntity(
                        orderId = orderId,
                        productId = 2,
                        productTitle = "ডিজাইনার প্রিমিয়াম কটন পাঞ্জাবী",
                        productImage = "img_product_panjabi_1784912531316",
                        selectedColor = "Maroon",
                        selectedSize = "L",
                        unitPrice = 2250.0,
                        quantity = 1,
                        totalPrice = 2250.0
                    )
                )
            )
        }
    }
}
