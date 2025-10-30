package com.example.levelupmobile.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.levelupmobile.model.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class LevelUpAppDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
}