package com.codemines.ecommerceapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemines.ecommerceapp.model.Product
import com.codemines.ecommerceapp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    fun fetchProductDetails() {
        viewModelScope.launch {
            val response = repository.getProductDetails()
            if (response.isSuccessful) {
                _product.value = response.body()?.data
            }
        }
    }
}
