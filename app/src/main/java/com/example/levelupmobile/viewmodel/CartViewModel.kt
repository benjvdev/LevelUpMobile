package com.example.levelupmobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupmobile.model.CartItem
import com.example.levelupmobile.repository.AppDatabase
import com.example.levelupmobile.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel @JvmOverloads constructor(
    application: Application,
    // permitimos inyectar el repositorio para los tests
    cartRepository: CartRepository? = null
) : AndroidViewModel(application) {

    private val repository: CartRepository
    val cartItems: StateFlow<List<CartItem>>

    init {
        // si nos pasan un repositorio (test) lo usamos, si no, creamos el real con la base de datos
        if (cartRepository != null) {
            repository = cartRepository
        } else {
            val cartDao = AppDatabase.getInstance(application).cartDao()
            repository = CartRepository(cartDao)
        }

        // convertimos el flujo de la base de datos en un estado para la ui
        cartItems = repository.getCartItems()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun onQuantityChanged(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity > 0) {
                repository.updateQuantity(item, newQuantity)
            } else {
                // si la cantidad es 0 o menos, borramos el item
                repository.deleteItem(item)
            }
        }
    }
}