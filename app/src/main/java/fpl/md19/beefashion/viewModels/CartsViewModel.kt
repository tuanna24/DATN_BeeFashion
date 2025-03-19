package fpl.md19.beefashion.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.Carts
import fpl.md19.beefashion.models.Products
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<Products>>(emptyList())
    val cartItems: StateFlow<List<Products>> = _cartItems

    private var _errorMessage = mutableStateOf<String?>(null)
    val errMessage : State<String?> = _errorMessage

    private val _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    fun fetchCart(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val apiService = HttpRequest.getInstance()
                val response = apiService.getCart(userId)
                if (response.isSuccessful) {
                    _cartItems.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Lỗi API: ${response.code()} - ${response.message()}"
                }
            }catch (e: Exception) {
                _errorMessage.value = "Lỗi không xác định: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun addToCart(userId: String, productId: String, carts: Carts) {
        viewModelScope.launch {
            try {
                val apiService = HttpRequest.getInstance()
                val response = apiService.addProductToCart(userId, productId, carts)
                if (response.isSuccessful) {
                    fetchCart(userId)
                } else {
                    _errorMessage.value = "Lỗi API: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi không xác định: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
