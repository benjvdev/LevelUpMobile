package com.example.levelupmobile.repository

import com.example.levelupmobile.model.CartItem
import com.example.levelupmobile.model.Product
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    suspend fun addProductToCart(product: Product) {
        val existingItem = cartDao.getItemByCode(product.code)

        if (existingItem == null) {
            val newItem = CartItem(
                code = product.code,
                name = product.name,
                image = product.image,
                price = product.price,
                quantity = 1
            )
            cartDao.insert(newItem)
        } else {
            val updatedItem = existingItem.copy(
                quantity = existingItem.quantity + 1
            )
            cartDao.update(updatedItem)
        }
    }

    fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllItems()
    }

    suspend fun deleteItem(item: CartItem) {
        cartDao.delete(item)
    }

    suspend fun updateQuantity(item: CartItem, newQuantity: Int) {
        if (newQuantity <= 0) {
            cartDao.delete(item)
        } else {
            cartDao.update(item.copy(quantity = newQuantity))
        }
    }
}