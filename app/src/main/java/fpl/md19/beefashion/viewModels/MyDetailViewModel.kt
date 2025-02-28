package fpl.md19.beefashion.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.UserModel
import fpl.md19.beefashion.requests.UpdateUserRequest
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

class MyDetailViewModel : ViewModel() {
    private val apiService: ApiService = HttpRequest.getInstance()

    fun updateProfile(updatedUser: UpdateUserRequest, onSuccess: (UserModel) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Get current user ID from session
                val userId = UserSesion.currentUser?.id ?: return@launch onError("User ID not found")

                val response = apiService.EditProfile(
                    id = userId,  // Pass the user ID from session
                    email = updatedUser.email.toRequestBody(),
                    fullName = updatedUser.fullName.toRequestBody(),
                    phone = updatedUser.phone?.toRequestBody(),
                    gender = updatedUser.gender?.toRequestBody(),
                    dateOfBirth = updatedUser.dateOfBirth?.toRequestBody(),
                    file = updatedUser.file
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Update the current user in session with new data
                        UserSesion.currentUser = it
                        onSuccess(it)
                    } ?: onError("Empty response body")
                } else {
                    onError("Server error: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.localizedMessage}")
            }
        }
    }
}
