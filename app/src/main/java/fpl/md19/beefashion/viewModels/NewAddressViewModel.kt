package fpl.md19.beefashion.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.AddressModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewAddressViewModel() : ViewModel() {
    private val apiService: ApiService = HttpRequest.getInstance()
    private val _provinces = MutableStateFlow<List<Province>>(emptyList())
    val provinces: StateFlow<List<Province>> = _provinces.asStateFlow()

    private val _districts = MutableStateFlow<List<District>>(emptyList())
    val districts: StateFlow<List<District>> = _districts.asStateFlow()

    private val _wards = MutableStateFlow<List<Ward>>(emptyList())
    val wards: StateFlow<List<Ward>> = _wards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedProvince = MutableStateFlow<Pair<String, String>?>(null)
    val selectedProvince: StateFlow<Pair<String, String>?> = _selectedProvince

    private val _selectedDistrict = MutableStateFlow<Pair<String, String>?>(null)
    val selectedDistrict: StateFlow<Pair<String, String>?> = _selectedDistrict

    private val _selectedWard = MutableStateFlow<String?>(null)
    val selectedWard: StateFlow<String?> = _selectedWard

    init {
        fetchProvinces()
    }
    fun setProvince(name: String, code: String) {
        if (_selectedProvince.value?.second != code) {
            _selectedProvince.value = name to code
            _selectedDistrict.value = null  // Reset quận/huyện khi đổi tỉnh
            _selectedWard.value = null      // Reset phường/xã khi đổi tỉnh
            fetchDistricts(code)            // Fetch districts mới
        }
    }

    fun setDistrict(name: String, code: String) {
        if (_selectedDistrict.value?.second != code) {
            _selectedDistrict.value = name to code
            _selectedWard.value = null      // Reset phường/xã khi đổi quận
            fetchWards(code)                // Fetch wards mới
        }
    }
    fun setWard(name: String) {
        _selectedWard.value = name
    }

    fun fetchProvinces() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService?.getProvinces()
                if (response != null) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _provinces.value = it
                        }
                    } else {
                        _error.value = "Không thể tải danh sách tỉnh/thành phố: ${response.code()}"
                        Log.e("NewAddressViewModel", "Failed to fetch provinces: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                _error.value = "Lỗi khi tải danh sách tỉnh/thành phố: ${e.message}"
                Log.e("NewAddressViewModel", "Error fetching provinces", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchDistricts(provinceCode: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService?.getDistricts(provinceCode)
                if (response != null) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            _districts.value = responseBody.districts ?: emptyList()
                        }
                    } else {
                        Log.e("NewAddressViewModel", "Failed to fetch districts: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("NewAddressViewModel", "Error fetching districts", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun fetchWards(districtCode: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService?.getWards(districtCode)
                if (response != null) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            _wards.value = responseBody.wards ?: emptyList()
                        }
                    } else {
                        _error.value = "Không thể tải danh sách phường/xã: ${response.code()}"
                        Log.e("NewAddressViewModel", "Failed to fetch wards: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                _error.value = "Lỗi khi tải danh sách phường/xã: ${e.message}"
                Log.e("NewAddressViewModel", "Error fetching wards", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setAddressData(address: AddressModel) {
        _selectedProvince.value = address.province to address.province
        _selectedDistrict.value = address.district to address.district
        _selectedWard.value = address.ward
    }

    fun clearError() {
        _error.value = null
    }

    sealed class CreateStatus {
        object Idle : CreateStatus()
        object Loading : CreateStatus()
        object Success : CreateStatus()
        data class Error(val message: String) : CreateStatus()
    }
}

data class Province(
    val code: Int,
    val name: String,
    val districts: List<District> = emptyList()
)

data class District(
    val code: Int,
    val name: String,
    val wards: List<Ward> = emptyList()
)

data class Ward(
    val code: Int,
    val name: String
)