package fpl.md19.beefashion.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.AddressModel
import fpl.md19.beefashion.requests.AddressRequest
import fpl.md19.beefashion.screens.data.FakeAddressData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressViewModel() : ViewModel() {
    private val apiService: ApiService = HttpRequest.getInstance()
    private val _addresses = MutableStateFlow<List<AddressModel>>(emptyList())
    val addresses: StateFlow<List<AddressModel>> = _addresses.asStateFlow()

    private val _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    private val _deleteStatus = MutableStateFlow<DeleteStatus>(DeleteStatus.Idle)
    val deleteStatus: StateFlow<DeleteStatus> = _deleteStatus

    val _createStatus = MutableStateFlow<CreateStatus>(CreateStatus.Idle)
    val createStatus: StateFlow<CreateStatus> = _createStatus

    val _updateStatus = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
    val updateStatus: StateFlow<UpdateStatus> = _updateStatus

    val _selectedAddress = MutableStateFlow<AddressModel?>(null)
    val selectedAddress: StateFlow<AddressModel?> = _selectedAddress

    val _selectedAddress1 = mutableStateOf<String?>(null)
    val selectedAddress1: State<String?> = _selectedAddress1

    init {
        fetchUserIdAndAddresses()
    }

    fun fetchUserIdAndAddresses() {
        viewModelScope.launch {
            try {
                Log.d("AddressViewModel", "Fetching user ID from session...")

                val userId = UserSesion.currentUser?.id
                if (!userId.isNullOrBlank()) {
                    Log.d("AddressViewModel", "User ID retrieved: $userId")
                    fetchAddresses(userId)
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
        viewModelScope.launch {
            try {
                _loading.value = true
                Log.d("AddressViewModel", "Fetching addresses for userId: $customerId")

                val useFakeData = false
                if (useFakeData) {
                    delay(500)
                    _addresses.value = FakeAddressData.getFakeAddresses()
                    Log.d("AddressViewModel", "Loaded fake addresses: ${_addresses.value.size}")
                } else {
                    apiService?.let { service ->
                        Log.d(
                            "AddressViewModel",
                            "Calling API: getAllAddresses for userId: $customerId"
                        )

                        val response = service.getAllAddresses(customerId)
                        Log.d("AddressViewModel", "Response Code: ${response.code()}")

                        if (response.isSuccessful) {
                            response.body()?.let { addresses ->
                                _addresses.value = addresses
                                Log.d(
                                    "AddressViewModel",
                                    "Loaded addresses from API: ${addresses.size}"
                                )
                            }
                        } else {
                            Log.e(
                                "AddressViewModel",
                                "Failed to fetch addresses: ${response.code()}"
                            )
                        }
                    } ?: Log.e("AddressViewModel", "ApiService is null")
                }
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error fetching addresses", e)
            } finally {
               // _isLoading.value = false
                _loading.value = false
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

                    Log.d(
                        "AddressViewModel",
                        "Delete Response Code: ${response.code()}, Body: ${
                            response.errorBody()?.string()
                        }"
                    )

                    if (response.isSuccessful) {
                        _addresses.value =
                            _addresses.value.filter { it.id != addressId }  // Xóa trực tiếp trong danh sách
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
        if (userId.isNullOrBlank()) return

        viewModelScope.launch {
            try {
                // Reset trạng thái trước khi gọi API
                _createStatus.value = CreateStatus.Idle
                _createStatus.value = CreateStatus.Loading

                val response = apiService?.createAddress(userId, addressRequest)
                if (response?.isSuccessful == true) {
                    response.body()?.let { newAddress ->
                        _addresses.value = _addresses.value + newAddress
                    }
                    _createStatus.value = CreateStatus.Success
                } else {
                    _createStatus.value = CreateStatus.Error("Lỗi khi thêm địa chỉ")
                }
            } catch (e: Exception) {
                _createStatus.value = CreateStatus.Error("Lỗi ngoại lệ: ${e.localizedMessage}")
            }
        }
    }

    fun updateAddress(customerId: String, addressId: String, addressRequest: AddressRequest) {
        val userId = UserSesion.currentUser?.id ?: return

        viewModelScope.launch {
            try {
                _updateStatus.value = UpdateStatus.Loading

                val response = apiService.updateAddress(userId, addressId, addressRequest)
                if (response.isSuccessful) {
                    response.body()?.let { updatedAddress ->
                        _addresses.value = _addresses.value.map {
                            if (it.id == addressId) updatedAddress else it
                        }
                        // Fetch lại dữ liệu từ server để đảm bảo đồng bộ
                        fetchAddresses(customerId)
                        // Cập nhật selectedAddress để UpdateScreen nhận dữ liệu mới ngay
                        //_selectedAddress.value = updatedAddress
                    }
//                    delay(300)
                    _updateStatus.value = UpdateStatus.Success
                } else {
                    _updateStatus.value = UpdateStatus.Error("Lỗi khi cập nhật địa chỉ")
                }
            } catch (e: Exception) {
                _updateStatus.value = UpdateStatus.Error("Lỗi ngoại lệ: ${e.localizedMessage}")
            }
        }
    }

    fun getAddressById(addressId: String): AddressModel? {
        return _addresses.value.find { it.id == addressId }
    }

    fun setSelectedAddress(address: AddressModel) {
        _selectedAddress.value = address
    }

    fun setSelectedAddress1(address: String) {
        _selectedAddress1.value = address
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

    sealed class UpdateStatus {
        object Idle : UpdateStatus()
        object Loading : UpdateStatus()
        object Success : UpdateStatus()
        data class Error(val message: String) : UpdateStatus()
    }
}


