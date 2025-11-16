package com.example.levelupmobile.repository

import android.util.Log
import com.example.levelupmobile.remote.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.levelupmobile.remote.ApiService
class ProductRepository(private val apiService: ApiService) {

    suspend fun getProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            try {
                val products = apiService.getProducts()
                //filtrar los productos disponibles
                products.filter { it.disponible }
            } catch (e: Exception) {
                Log.e("ProductRepository", "Error al obtener productos", e)
                emptyList()
            }
        }
    }
}