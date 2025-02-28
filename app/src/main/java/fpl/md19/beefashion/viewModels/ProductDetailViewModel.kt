import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.ProductDetails
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val apiService = HttpRequest.getInstance() // Lấy instance

    // LiveData để cập nhật giao diện khi có dữ liệu mới
    private val _productDetail = MutableLiveData<ProductDetails?>()
    val productDetail: LiveData<ProductDetails?> get() = _productDetail

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchProductDetails(productId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getProductDetails(productId)

                // Kiểm tra nếu danh sách không rỗng, lấy phần tử đầu tiên
                if (response != null) {
                    _productDetail.value = response // Lấy sản phẩm đầu tiên
                } else {
                    _errorMessage.value = "Không tìm thấy sản phẩm"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi: ${e.message}"
            }
        }
    }
}
