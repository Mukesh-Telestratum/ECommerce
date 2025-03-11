package com.codemines.ecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codemines.ecommerceapp.network.RetrofitInstance
import com.codemines.ecommerceapp.repository.ProductRepository
import com.codemines.ecommerceapp.ui.ProductScreen
import com.codemines.ecommerceapp.ui.theme.ECommerceAppTheme
import com.codemines.ecommerceapp.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = ProductRepository(RetrofitInstance.api)
        val viewModel = ProductViewModel(repository)
        enableEdgeToEdge()
        setContent {
            ECommerceAppTheme {
//                    ProductScreen(viewModel)
                ProductScreen(viewModel = viewModel, onBackClick = { finish() })  // Handle back navigation
            }
        }
    }
}
