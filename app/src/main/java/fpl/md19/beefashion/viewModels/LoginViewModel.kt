package fpl.md19.beefashion.viewModels


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.UserModel
import fpl.md19.beefashion.requests.LoginRequest
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.requests.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _loginMessage = MutableStateFlow<String?>(null)
    val loginMessage: StateFlow<String?> = _loginMessage

    private val _registerMessage = MutableStateFlow<String?>(null)
    val registerMessage: StateFlow<String?> = _registerMessage

    private val _loginResponse = MutableStateFlow<Response<UserModel>?>(null)
    val loginResponse: StateFlow<Response<UserModel>?> = _loginResponse

    private val _registerResponse = MutableStateFlow<Response<UserModel>?>(null)
    val registerResponse: StateFlow<Response<UserModel>?> = _registerResponse

    var rememberedEmail: String = ""
        private set
    var rememberedPassword: String = ""
        private set
    var isRemembered: Boolean = false
        private set

    private val PREF_NAME = "login_preferences"
    private val PREF_EMAIL = "email"
    private val PREF_PASSWORD = "password"
    private val PREF_REMEMBER = "remember_me"

    fun login(context: Context, email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            try {
                _loginResponse.value = null
                _loginMessage.value = null

                val response = HttpRequest.getInstance().Login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _loginResponse.value = response
                    _loginMessage.value = "Đăng nhập thành công!"
                    UserSesion.currentUser = response.body()

                    if (rememberMe) {
                        saveCredentials(
                            context, email, password, userModel = UserSesion.currentUser!!
                        )
                    } else {
                        clearCredentials(context)
                    }
                } else {
                    if (response.code() == 404) {
                        _loginMessage.value = "Tài khoản hoặc mật khẩu không chính xác."
                    } else {
                        _loginMessage.value = "Đã xảy ra lỗi: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _loginMessage.value = "Không thể kết nối đến server: ${e.message}"
            }
        }
    }

    fun register(email: String, password: String, fullName: String) {
        viewModelScope.launch {
            try {
                _registerResponse.value = null
                _registerMessage.value = null
                val response =
                    HttpRequest.getInstance().Register(RegisterRequest(fullName, email, password))
                if (response.isSuccessful) {
                    _registerMessage.value = "Đăng ký thành công!"
                    UserSesion.currentUser = UserModel(
                        email = email,
                        fullName = fullName,
                        password = password
                    )
                } else {
                    if (response.code() == 409) {
                        _registerMessage.value = "Email này đã tồn tại!"
                    } else {
                        _registerMessage.value = "Đã xảy ra lỗi: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _registerMessage.value = "Không thể kết nối đến server: ${e.message}"
            }
        }
    }

    fun loadRememberedCredentials(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val userJson = sharedPreferences.getString("user", null)
        if (userJson != null) {
            val gson = Gson()
            val userModel = gson.fromJson(userJson, UserModel::class.java)

            rememberedEmail = sharedPreferences.getString(PREF_EMAIL, "") ?: ""
            rememberedPassword = sharedPreferences.getString(PREF_PASSWORD, "") ?: ""
            isRemembered = sharedPreferences.getBoolean(PREF_REMEMBER, false)
        } else {
            rememberedPassword = ""
            isRemembered = false
        }


    }

    private fun saveCredentials(
        context: Context, email: String, password: String, userModel: UserModel
    ) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        val gson = Gson()
        val userJson = gson.toJson(userModel)

        editor.putString("user", userJson)
        editor.putString(PREF_EMAIL, email)
        editor.putString(PREF_PASSWORD, password)
        editor.putBoolean(PREF_REMEMBER, true)
        editor.apply()

    }

    private fun clearCredentials(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("user").remove(PREF_EMAIL).remove(PREF_PASSWORD)
            .putBoolean(PREF_REMEMBER, false).apply()
    }

    fun logout(context: Context) {
        clearCredentials(context)


        _loginMessage.value = "Đăng xuất thành công!"
    }
}
