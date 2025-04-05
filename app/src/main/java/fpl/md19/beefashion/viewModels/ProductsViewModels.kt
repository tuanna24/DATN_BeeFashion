package fpl.md19.beefashion.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.Products
import kotlinx.coroutines.launch

class ProductsViewModels : ViewModel() {
    // Danh sách sản phẩm gốc
    private val _originalProducts = mutableStateOf<List<Products>>(emptyList())

    // Danh sách sản phẩm hiển thị (có thể đã lọc)
    private val _products = mutableStateOf<List<Products>>(emptyList())
    val products: State<List<Products>> = _products

    val totalQuantities: State<Int> = derivedStateOf {
        _products.value.sumOf { it.quantities.sum() }
    }

    private var _errorMessage = mutableStateOf<String?>(null)
    val errMessage: State<String?> = _errorMessage

    private val _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val apiService = HttpRequest.getInstance()
                val customerID = UserSesion.currentUser?.id
                var response = apiService.getProducts()

                if (!customerID.isNullOrBlank()) {
                    response = apiService.getProductsWithCustomerID(customerID)
                }

                if (response.isSuccessful) {
                    val productList = response.body() ?: emptyList()
                    _originalProducts.value = productList // Lưu danh sách gốc
                    _products.value = productList // Cập nhật danh sách hiển thị
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

    fun filterProductsByPrice(priceRange: ClosedFloatingPointRange<Float>) {
        viewModelScope.launch {
            val filtered = _originalProducts.value.filter { product ->
                val totalQuantity = (product.quantities ?: emptyList()).sum()
                val price = when (val priceValue = product.price) {
                    is Int -> priceValue.toFloat()
                    else -> 0f
                }
                val priceInRange = price in priceRange
                priceInRange && totalQuantity > 0
            }
            _products.value = filtered // Cập nhật danh sách hiển thị
        }
    }

    fun resetProducts() {
        _products.value = _originalProducts.value // Khôi phục từ danh sách gốc
    }
}