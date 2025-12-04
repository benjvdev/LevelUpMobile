package com.example.levelupmobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupmobile.remote.Product
import com.example.levelupmobile.remote.RetrofitClient
import com.example.levelupmobile.repository.AppDatabase
import com.example.levelupmobile.repository.CartRepository
import com.example.levelupmobile.repository.ProductRepository
import com.example.levelupmobile.session.SearchHistoryManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val searchResults: List<Product> = emptyList(),
    val history: List<String> = emptyList(),
    val isSearching: Boolean = false,
    val isLoading: Boolean = false
)

class SearchViewModel @JvmOverloads constructor(
    application: Application,
    productRepo: ProductRepository? = null,
    cartRepo: CartRepository? = null
) : AndroidViewModel(application) {

    private val repository: ProductRepository
    private val cartRepository: CartRepository
    private val historyManager: SearchHistoryManager

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _showAddedToCartOverlay = MutableStateFlow(false)
    val showAddedToCartOverlay = _showAddedToCartOverlay.asStateFlow()

    init {
        val apiService = RetrofitClient.getInstance(application)
        repository = productRepo ?: ProductRepository(apiService)
        historyManager = SearchHistoryManager(application)

        if (cartRepo != null) {
            cartRepository = cartRepo
        } else {
            val cartDao = AppDatabase.getInstance(application).cartDao()
            cartRepository = CartRepository(cartDao)
        }

        refreshHistory()
    }

    private fun refreshHistory() {
        _uiState.update { it.copy(history = historyManager.getHistory()) }
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        if (newQuery.isBlank()) {
            _uiState.update { it.copy(isSearching = false, searchResults = emptyList()) }
        }
    }

    fun onEnterEditMode() {
        _uiState.update { it.copy(isSearching = false) }
    }
    fun onSearch() {
        val query = _uiState.value.query
        if (query.isBlank()) return

        historyManager.addSearch(query)
        refreshHistory()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isSearching = true) }
            val results = repository.searchProducts(query)
            _uiState.update { it.copy(isLoading = false, searchResults = results) }
        }
    }

    fun onHistoryItemClicked(historyItem: String) {
        onQueryChanged(historyItem)
        onSearch()
    }

    fun onDeleteHistoryItem(historyItem: String) {
        historyManager.removeSearch(historyItem)
        refreshHistory()
    }

    fun onClearSearch() {
        onQueryChanged("")
    }

    fun onAddToCartClicked(product: Product) {
        viewModelScope.launch {
            cartRepository.addProductToCart(product)
            _showAddedToCartOverlay.value = true
            delay(1200L)
            _showAddedToCartOverlay.value = false
        }
    }
}