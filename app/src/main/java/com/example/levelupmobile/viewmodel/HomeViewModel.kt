package com.example.levelupmobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupmobile.remote.Product
import com.example.levelupmobile.remote.RetrofitClient
import com.example.levelupmobile.repository.AppDatabase
import com.example.levelupmobile.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.levelupmobile.repository.CartRepository
import kotlinx.coroutines.delay

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductRepository
    private val cartRepository: CartRepository

    //estado para guardar la lista de productos
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    //estado para controlar la notificacion de añadir al carro
    private val _showAddedToCartOverlay = MutableStateFlow(false)
    val showAddedToCartOverlay = _showAddedToCartOverlay.asStateFlow()

    //bloque init para cargar los productos apenas se cree el ViewModel
    init {
        val apiService = RetrofitClient.getInstance(application)
        repository = ProductRepository(apiService)

        val cartDao = AppDatabase.getInstance(application).cartDao()
        cartRepository = CartRepository(cartDao)
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _products.value = repository.getProducts()
        }
    }
    fun onAddToCartClicked(product: Product) {
        viewModelScope.launch {
            cartRepository.addProductToCart(product)

            //mostrar y ocultar notificacion
            _showAddedToCartOverlay.value = true
            delay(1200L)
            _showAddedToCartOverlay.value = false

        }
    }
}