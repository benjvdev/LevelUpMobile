package com.example.levelupmobile.repository

import androidx.room.*
import com.example.levelupmobile.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    // insertar un item si aún no existe
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: CartItem)

    // actualizar un item para cambiar la cantidad
    @Update
    suspend fun update(item: CartItem)

    // borrar un item del carrito
    @Delete
    suspend fun delete(item: CartItem)

    // obtener un solo item por su codigo
    @Query("SELECT * FROM cart_items WHERE id = :id LIMIT 1")
    suspend fun getItemById(id: Long): CartItem?

    // obtener todos los items del carrito
    // Flow<> para que la ui se actualice automatico
    @Query("SELECT * FROM cart_items")
    fun getAllItems(): Flow<List<CartItem>>
}