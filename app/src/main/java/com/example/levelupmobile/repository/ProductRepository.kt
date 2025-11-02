package com.example.levelupmobile.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.levelupmobile.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(private val context: Context) {

    //Lee el JSON de los assets y lo convierte en una lista de productos.
    //Usamos Dispatchers.IO porque es una operación de lectura de archivo (I/O).
    suspend fun getProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            try {
                //leer el archivo JSON de los assets
                val jsonString = context.assets.open("products.json")
                    .bufferedReader()
                    .use { it.readText() }

                //define el tipo de lista (para que Gson sepa que es una lista de Product)
                val listType = object : TypeToken<List<Product>>() {}.type

                //usa gson para convertir el string JSON en la lista de objetos
                Gson().fromJson(jsonString, listType)

            } catch (e: Exception) {
                Log.e("ProductRepository", "Error al leer products.json", e)
                emptyList() // devuelve una lista vacia en caso de fallar
            }
        }
    }
}