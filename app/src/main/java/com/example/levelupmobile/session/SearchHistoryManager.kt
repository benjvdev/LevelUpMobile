package com.example.levelupmobile.session

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryManager(context: Context) {
    private val prefs = context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "HISTORY_LIST"

    fun getHistory(): List<String> {
        val json = prefs.getString(key, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addSearch(query: String) {
        if (query.isBlank()) return

        //obtenemos la lista actual, quitamos si ya existe para ponerlo primero
        val currentList = getHistory().toMutableList()
        currentList.remove(query)
        currentList.add(0, query) // Lo pone al principio

        //limitamos a 10 items
        if (currentList.size > 10) {
            currentList.removeAt(currentList.lastIndex)
        }

        saveList(currentList)
    }

    fun removeSearch(query: String) {
        val currentList = getHistory().toMutableList()
        currentList.remove(query)
        saveList(currentList)
    }

    private fun saveList(list: List<String>) {
        val json = gson.toJson(list)
        prefs.edit().putString(key, json).apply()
    }
}