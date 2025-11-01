package com.example.levelupmobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val code: String,
    val name: String,
    val image: String,
    val price: String,
    val quantity: Int
)