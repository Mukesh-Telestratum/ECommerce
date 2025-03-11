package com.codemines.ecommerceapp.network


import com.codemines.ecommerceapp.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("rest/V1/productdetails/6701/253620?lang=en&store=KWD")
    suspend fun getProductDetails(): Response<ProductResponse>
}

data class ProductResponse(
    val status: Int,
    val message: String,
    val data: Product
)
