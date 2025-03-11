package com.codemines.ecommerceapp.repository

import com.codemines.ecommerceapp.network.ApiService


class ProductRepository(private val apiService: ApiService) {
    suspend fun getProductDetails() = apiService.getProductDetails()
}
