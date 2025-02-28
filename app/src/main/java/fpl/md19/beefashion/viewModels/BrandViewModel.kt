package fpl.md19.beefashion.viewModels

import android.net.http.HttpException
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.Brand
import fpl.md19.beefashion.models.Products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class BrandViewModel : ViewModel() {
    private val _brands = mutableStateOf<List<Brand>>(emptyList())
    val brand : State<List<Brand>> = _brands

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
                val response = apiService.getBrands()
                if (response.isSuccessful) {
                    _brands.value = (response.body() ?: emptyList())
                    Log.d("data", _brands.value.toString())
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