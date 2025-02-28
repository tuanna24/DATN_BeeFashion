package fpl.md19.beefashion.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.Products
import fpl.md19.beefashion.models.Sizes
import kotlinx.coroutines.launch

class SizesViewModel : ViewModel() {
    private val _sizes = mutableStateOf<List<Sizes>>(emptyList())
    val sizes : State<List<Sizes>> = _sizes

    private var _errorMessage = mutableStateOf<String?>(null)
    val errMessage : State<String?> = _errorMessage

    private val _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    init {
        getSizes()
    }

    private fun getSizes() {
        viewModelScope.launch() {
            _loading.value = true
            _errorMessage.value = null
            try {
                val apiService = HttpRequest.getInstance()
                val response = apiService.getSizes()
                if (response.isSuccessful) {
                    _sizes.value = (response.body() ?: emptyList())
                    Log.d("data", _sizes.value.toString())
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