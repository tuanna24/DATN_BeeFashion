package fpl.md19.beefashion.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.models.AddressModel
import fpl.md19.beefashion.requests.AddressRequest
import fpl.md19.beefashion.screens.data.FakeAddressData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AddressViewModel(
    private val apiService: ApiService? = null

) : ViewModel() {
    private val _addresses = MutableStateFlow<List<AddressModel>>(emptyList())
    val addresses: StateFlow<List<AddressModel>> = _addresses.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _deleteStatus = MutableStateFlow<DeleteStatus>(DeleteStatus.Idle)
    val deleteStatus: StateFlow<DeleteStatus> = _deleteStatus
    private val _createStatus = MutableStateFlow<CreateStatus>(CreateStatus.Idle)
    val createStatus: StateFlow<CreateStatus> = _createStatus

    init {
        fetchUserIdAndAddresses()
    }

    fun fetchUserIdAndAddresses() {
        viewModelScope.launch {
            try {
                Log.d("AddressViewModel", "Fetching user ID from session...")

                val userId = UserSesion.currentUser?.id // Lấy userId từ session
                if (!userId.isNullOrBlank()) {
                    Log.d("AddressViewModel", "User ID retrieved: $userId")
                    fetchAddresses(userId) // Gọi API lấy danh sách địa chỉ
                } else {
                    Log.e("AddressViewModel", "User ID is null or blank")
                }
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error fetching user ID", e)
            }
        }
    }



    fun fetchAddresses(customerId: String) {
        if (customerId.isBlank()) {
            Log.e("AddressViewModel", "User ID is blank, skipping API call")
            return
        }

        if (_isLoading.value) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("AddressViewModel", "Fetching addresses for userId: $customerId")

                val useFakeData = false
                if (useFakeData) {
                    delay(500)
                    _addresses.value = FakeAddressData.getFakeAddresses()
                    Log.d("AddressViewModel", "Loaded fake addresses: ${_addresses.value.size}")
                } else {
                    apiService?.let { service ->
                        Log.d("AddressViewModel", "Calling API: getAllAddresses for userId: $customerId")

                        val response = service.getAllAddresses(customerId)
                        Log.d("AddressViewModel", "Response Code: ${response.code()}")
                        Log.d("AddressViewModel", "Response Body: ${response.body()}")

                        if (response.isSuccessful) {
                            response.body()?.let { addresses ->
                                _addresses.value = addresses
                                Log.d("AddressViewModel", "Loaded addresses from API: ${addresses.size}")
                            }
                        } else {
                            Log.e("AddressViewModel", "Failed to fetch addresses: ${response.code()}")
                        }
                    } ?: Log.e("AddressViewModel", "ApiService is null")
                }
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error fetching addresses", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun deleteAddress(address: AddressModel) {
        val userId = UserSesion.currentUser?.id
        if (userId == null) {
            Log.e("AddressViewModel", "User ID is null. Cannot delete address.")
            return
        }

        val addressId = address.id
        Log.d("AddressViewModel", "Deleting address - userId: $userId, addressId: $addressId")

        viewModelScope.launch {
            try {
                _deleteStatus.value = DeleteStatus.Loading

                apiService?.let { service ->
                    val response = service.deleteAddress(userId, addressId)

                    Log.d("AddressViewModel", "Delete Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")

                    if (response.isSuccessful) {
                        _addresses.value = _addresses.value.filter { it.id != addressId }  // Xóa trực tiếp trong danh sách
                        _deleteStatus.value = DeleteStatus.Success
                        Log.d("AddressViewModel", "Address deleted successfully!")
                    } else {
                        _deleteStatus.value = DeleteStatus.Error("Failed: ${response.code()}")
                        Log.e("AddressViewModel", "Delete failed: ${response.code()}")
                    }
                } ?: run {
                    _deleteStatus.value = DeleteStatus.Error("API Service not available")
                    Log.e("AddressViewModel", "ApiService is null")
                }
            } catch (e: Exception) {
                _deleteStatus.value = DeleteStatus.Error(e.localizedMessage ?: "Unknown error")
                Log.e("AddressViewModel", "Exception during address deletion", e)
            } finally {
                _deleteStatus.value = DeleteStatus.Idle  // Không cần delay
            }
        }
    }

    fun createAddress(addressRequest: AddressRequest) {
        val userId = UserSesion.currentUser?.id
        if (userId.isNullOrBlank()) {
            Log.e("AddressViewModel", "User ID is null or blank. Cannot create address.")
            _createStatus.value = CreateStatus.Error("Không thể tạo địa chỉ: User ID không hợp lệ.")
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _createStatus.value = CreateStatus.Loading
                Log.d("AddressViewModel", "Creating new address for userId: $userId")

                if (apiService == null) {
                    Log.e("AddressViewModel", "ApiService is null - Không thể gọi API")
                    _createStatus.value = CreateStatus.Error("Dịch vụ API không khả dụng. Vui lòng thử lại sau.")
                    return@launch
                }

                val response = apiService.createAddress(userId, addressRequest)
                Log.d("AddressViewModel", "Create Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    fetchAddresses(userId) // Cập nhật danh sách địa chỉ sau khi tạo
                    _createStatus.value = CreateStatus.Success
                } else {
                    _createStatus.value = CreateStatus.Error("Lỗi khi tạo địa chỉ: ${response.code()}")
                }
            } catch (e: Exception) {
                _createStatus.value = CreateStatus.Error("Lỗi ngoại lệ: ${e.localizedMessage}")
                Log.e("AddressViewModel", "Lỗi khi tạo địa chỉ", e)
            } finally {
                _isLoading.value = false
                delay(3000)
                _createStatus.value = CreateStatus.Idle
            }
        }
    }


    sealed class CreateStatus {
        object Idle : CreateStatus()
        object Loading : CreateStatus()
        object Success : CreateStatus()
        data class Error(val message: String) : CreateStatus()
    }

    sealed class DeleteStatus {
        object Idle : DeleteStatus()
        object Loading : DeleteStatus()
        object Success : DeleteStatus()
        data class Error(val message: String) : DeleteStatus()
    }
}
class AddressViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            return AddressViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

