package fpl.md19.beefashion.viewModels

import android.net.http.HttpException
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.Products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class ProductsViewModels : ViewModel() {
    private val _products = mutableStateOf<List<Products>>(emptyList())
    val products : State<List<Products>> = _products

    val totalQuantities: State<Int> = derivedStateOf {
        _products.value.sumOf { it.quantities.sum() }
    }

    private var _errorMessage = mutableStateOf<String?>(null)
    val errMessage : State<String?> = _errorMessage

    private val _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch() {
            _loading.value = true
            _errorMessage.value = null
            try {
                val apiService = HttpRequest.getInstance()
                val response = apiService.getProducts()
                if (response.isSuccessful) {
                    _products.value = (response.body() ?: emptyList())
                    Log.d("data", _products.value.toString())
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