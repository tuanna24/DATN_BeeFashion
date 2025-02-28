package fpl.md19.beefashion.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.Categories
import kotlinx.coroutines.launch
import java.io.IOException

class CategoriesViewModels : ViewModel() {
    private val _categories = mutableStateOf<List<Categories>>(emptyList())
    val categories : State<List<Categories>> = _categories

    private val _loading = mutableStateOf(true)
    val loading : State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage : State<String?> = _errorMessage

    init {
        loadingCategorie()
    }

    private fun loadingCategorie() {
        viewModelScope.launch() {
            _loading.value = true
            _errorMessage.value = null
            try {
                val apiService = HttpRequest.getInstance()
                val response = apiService.getCategories()
                if (response.isSuccessful) {
                    _categories.value = (response.body() ?: emptyList())
                    Log.d("data", _categories.value.toString())
                } else {
                    _errorMessage.value = "Lỗi API: ${response.code()} - ${response.message()}"
                }
            } catch (e: IOException) {
                _errorMessage.value = "Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng!"
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi không xác định: ${e.message}"
            } finally {
                _loading.value = false
            }
        }

    }
}