package fpl.md19.beefashion.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import fpl.md19.beefashion.viewModels.AuthState
import fpl.md19.beefashion.viewModels.AuthViewModel

@Composable
fun SignUpScreen(navController: NavController, authViewModel: AuthViewModel) {
    // State to hold input values
    val fullName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val password2 = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val passwordVisible2 = remember { mutableStateOf(false) }

    // Error states
    val emailError = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    val passwordLengthError = remember { mutableStateOf("") }

    // Check if all fields are filled
    val isFormValid = fullName.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty() && password2.value.isNotEmpty()

    // Email validation using regex
    fun validateEmail(): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
        return email.value.matches(emailRegex)
    }

    // Password length validation
    fun validatePasswordLength(): Boolean {
        return password.value.length >= 8
    }

    // Password matching validation
    fun validatePasswords() {
        passwordError.value = if (password2.value.isNotEmpty() && password.value != password2.value) {
            "Mật khẩu không khớp"
        } else {
            ""
        }
    }

    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                navController.navigate("HomeScreen")
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            is AuthState.Loading -> {
                Toast.makeText(context, "Đang xử lý...", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .padding(top = 40.dp)
    ) {
        Text(
            text = "Tạo tài khoản",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "Hãy tạo tài khoản của bạn.",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Full Name TextField
            Column {
                Text(
                    text = "Họ và tên",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = fullName.value,
                    onValueChange = { fullName.value = it },
                    placeholder = { Text("Nhập họ và tên của bạn") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black
                    )
                )
            }

            // Email TextField
            Column {
                Text(
                    text = "Email",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        emailError.value = if (!validateEmail()) "Email không hợp lệ" else ""
                    },
                    placeholder = { Text("Nhập địa chỉ email của bạn") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black
                    )
                )
                if (emailError.value.isNotEmpty()) {
                    Text(
                        text = emailError.value,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Password TextField
            Column {
                Text(
                    text = "Mật khẩu mới",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        passwordLengthError.value = if (!validatePasswordLength()) "Mật khẩu phải ít nhất 8 ký tự" else ""
                    },
                    placeholder = { Text("Nhập mật khẩu của bạn") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black
                    ),
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible.value = !passwordVisible.value }
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible.value) {
                                        R.drawable.visibility // Icon khi đang hiện mật khẩu
                                    } else {
                                        R.drawable.invisible // Icon khi đang ẩn mật khẩu
                                    }
                                ),
                                contentDescription = if (passwordVisible.value) {
                                    "Ẩn mật khẩu"
                                } else {
                                    "Hiển thị mật khẩu"
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
                if (passwordLengthError.value.isNotEmpty()) {
                    Text(
                        text = passwordLengthError.value,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Password Confirm TextField
            Column {
                Text(
                    text = "Nhập lại mật khẩu mới",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = password2.value,
                    onValueChange = {
                        password2.value = it
                        validatePasswords()
                    },
                    placeholder = { Text("Nhập mật khẩu của bạn") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = passwordError.value.isNotEmpty(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = if (passwordError.value.isNotEmpty()) Color.Red else Color.LightGray,
                        focusedBorderColor = if (passwordError.value.isNotEmpty()) Color.Red else Color.Black
                    ),
                    visualTransformation = if (passwordVisible2.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible2.value = !passwordVisible2.value }
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible2.value) {
                                        R.drawable.visibility // Icon khi đang hiện mật khẩu
                                    } else {
                                        R.drawable.invisible // Icon khi đang ẩn mật khẩu
                                    }
                                ),
                                contentDescription = if (passwordVisible2.value) {
                                    "Ẩn mật khẩu"
                                } else {
                                    "Hiển thị mật khẩu"
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
                if (passwordError.value.isNotEmpty()) {
                    Text(
                        text = passwordError.value,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Create Account Button
        Button(
            onClick = {
                if (isFormValid && validateEmail() && validatePasswordLength() && password.value == password2.value) {
                    authViewModel.signup(email.value, password.value)
                }
            },
            enabled = authState.value != AuthState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color.Black else Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Tạo tài khoản",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Already have an account
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Đã có tài khoản? ",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = "Đăng nhập",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.clickable { navController.navigate("LoginScreen") }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    val mockAuthViewModel = AuthViewModel()
    SignUpScreen(navController, mockAuthViewModel)
}
