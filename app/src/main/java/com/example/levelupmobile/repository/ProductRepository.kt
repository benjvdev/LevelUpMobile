package com.example.levelupmobile.repository

import android.util.Log
import com.example.levelupmobile.remote.ApiService
import com.example.levelupmobile.remote.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    suspend fun searchProducts(query: String): List<Product> {
        return withContext(Dispatchers.IO) {
            try {
                val products = apiService.searchProducts(query)
                products.filter { it.disponible }
            } catch (e: Exception) {
                Log.e("ProductRepository", "Error buscando productos", e)
                emptyList()
            }
        }
    }
}