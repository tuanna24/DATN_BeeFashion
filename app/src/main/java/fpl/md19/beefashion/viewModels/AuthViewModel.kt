package fpl.md19.beefashion.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthState()
    }

    fun checkAuthState(){
        if(auth.currentUser == null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun isValidEmail(email: String): Boolean {
        // Biểu thức chính quy để kiểm tra định dạng email
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        // Kiểm tra mật khẩu có ít nhất 8 ký tự không
        return password.length >= 8
    }

    fun login(email : String, password : String){
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email và mật khẩu không được để trống")
            return
        }

        // Kiểm tra định dạng email và độ dài mật khẩu
        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Email không đúng định dạng")
            return
        }

        if (!isValidPassword(password)) {
            _authState.value = AuthState.Error("Mật khẩu phải có ít nhất 8 ký tự")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Đã có lỗi xảy ra")
                }
            }
    }

    fun signup(email : String, password : String){
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email và mật khẩu không được để trống")
            return
        }

        // Kiểm tra định dạng email và độ dài mật khẩu
        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Email không đúng định dạng")
            return
        }

        if (!isValidPassword(password)) {
            _authState.value = AuthState.Error("Mật khẩu phải có ít nhất 8 ký tự")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Đã có lỗi xảy ra")
                }
            }
    }

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated

    }

}

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}