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

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CartRepository
    val cartItems: StateFlow<List<CartItem>>

    init {
        val cartDao = AppDatabase.getInstance(application).cartDao()
        repository = CartRepository(cartDao)

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
                repository.deleteItem(item)
            }
        }
    }
}