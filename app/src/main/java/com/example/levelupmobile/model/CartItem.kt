package com.example.levelupmobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val id: Long,
    val name: String,
    val image: String,
    val price: Double,
    val quantity: Int
)