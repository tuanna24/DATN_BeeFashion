package fpl.md19.beefashion.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.UserModel
import fpl.md19.beefashion.requests.UpdateUserRequest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class MyDetailViewModel : ViewModel() {
    private val apiService: ApiService = HttpRequest.getInstance()

    fun updateProfile(updatedUser: UpdateUserRequest, onSuccess: (UserModel) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val userId = UserSesion.currentUser?.id?: return@launch onError("User ID not found")

                // Log thông tin dữ liệu trước khi gửi request
                Log.d("MyDetailViewModel", "Updating profile for userId: $userId")
                Log.d("MyDetailViewModel", "Email: ${updatedUser.email}")
                Log.d("MyDetailViewModel", "Full Name: ${updatedUser.fullName}")
                Log.d("MyDetailViewModel", "Phone: ${updatedUser.phone}")
                Log.d("MyDetailViewModel", "Gender: ${updatedUser.gender}")
                Log.d("MyDetailViewModel", "Date of Birth: ${updatedUser.dateOfBirth}")

                // Kiểm tra và log chi tiết thông tin ảnh
                updatedUser.image?.let { imagePart ->
                    val headers = imagePart.headers
                    Log.d("MyDetailViewModel", "Image Headers: $headers")

                    val contentType = imagePart.body.contentType()
                    val contentLength = imagePart.body.contentLength()
                    Log.d("MyDetailViewModel", "Image Content-Type: $contentType")
                    Log.d("MyDetailViewModel", "Image Content-Length: $contentLength")
                } ?: Log.d("MyDetailViewModel", "Image is NULL")

                // Gửi request cập nhật thông tin người dùng
                val response = apiService.EditProfile(
                    id = userId,
                    email = updatedUser.email.toRequestBody(),
                    fullName = updatedUser.fullName.toRequestBody(),
                    phone = updatedUser.phone?.toRequestBody(),
                    gender = updatedUser.gender?.toRequestBody(),
                    dateOfBirth = updatedUser.dateOfBirth?.toRequestBody(),
                    image = updatedUser.image
                )

                // Xử lý phản hồi từ server
                if (response.isSuccessful) {
                    response.body()?.let {
                        UserSesion.currentUser = it
                        onSuccess(it)
                    } ?: onError("Empty response body")
                } else {
                    Log.e("MyDetailViewModel", "Server error: ${response.message()}")
                    onError("Server error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("MyDetailViewModel", "Error updating profile", e)
                onError("Error: ${e.localizedMessage}")
            }
        }
    }
}
