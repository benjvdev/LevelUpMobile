package com.example.levelupmobile.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


// para que Room no permita dos usuarios con el mismo email.
@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val address: String,
    val email: String,
    val password: String // contraseña en texto plano
)