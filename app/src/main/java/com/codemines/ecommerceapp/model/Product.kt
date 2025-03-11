package com.codemines.ecommerceapp.model

data class Product(
    val name: String,
    val sku : String,
    val brand_name: String,
    val price: Double,
    val description: String,
    val image: String,  // Single image
    val images: List<String> // List of images
)
