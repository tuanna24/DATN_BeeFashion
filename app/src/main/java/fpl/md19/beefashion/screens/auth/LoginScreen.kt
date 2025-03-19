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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import fpl.md19.beefashion.viewModels.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    // State để lưu giá trị đầu vào
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isRememberMeChecked by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Lấy trạng thái thông báo từ ViewModel
    val loginMessage by viewModel.loginMessage.collectAsState()

    // Kiểm tra xem tất cả các trường đã được điền chưa
    val isFormValid = email.isNotEmpty() && password.isNotEmpty()

    // Tải thông tin đăng nhập đã lưu (nếu có)
    LaunchedEffect(Unit) {
        viewModel.loadRememberedCredentials(context) {
            // Điều hướng đến HomeScreen nếu đăng nhập tự động thành công
            navController.navigate("HomeScreen") {
                popUpTo("LoginScreen") { inclusive = true }
            }
        }
        email = viewModel.rememberedEmail
        password = viewModel.rememberedPassword
        isRememberMeChecked = viewModel.isRemembered
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .padding(top = 40.dp)
    ) {
        Text(
            text = "Đăng nhập vào tài khoản",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "Rất vui khi được gặp lại bạn.",
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
            // Email TextField
            Column {
                Text(
                    text = "Email",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Nhập địa chỉ email của bạn") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black
                    )
                )
            }

            // Password TextField
            Column {
                Text(
                    text = "Mật khẩu",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Nhập mật khẩu của bạn") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible) {
                                        R.drawable.visibility // Icon khi đang hiện mật khẩu
                                    } else {
                                        R.drawable.invisible // Icon khi đang ẩn mật khẩu
                                    }
                                ),
                                contentDescription = if (passwordVisible) {
                                    "Ẩn mật khẩu"
                                } else {
                                    "Hiển thị mật khẩu"
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            }
        }

        // Row chứa Nhớ mật khẩu và Quên mật khẩu
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Remember Password
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { isRememberMeChecked = !isRememberMeChecked }
            ) {
                Checkbox(
                    checked = isRememberMeChecked,
                    onCheckedChange = { isRememberMeChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Gray
                    )
                )
                Text(
                    text = "Nhớ mật khẩu",
                    fontSize = 14.sp,
                    color = if (isRememberMeChecked) Color.Black else Color.Gray
                )
            }

            // Forgot Password
            Text(
                text = "Quên mật khẩu?",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.clickable { navController.navigate("ForgotPasswordScreen") }
            )
        }

        // Login Button
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.login(context, email, password, isRememberMeChecked)
                } else {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color.Black else Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Đăng nhập",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // Hiển thị thông báo nếu có
        LaunchedEffect(loginMessage) {
            loginMessage?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                if (message == "Đăng nhập thành công!") {
                    navController.navigate("HomeScreen") {
                        popUpTo("LoginScreen") { inclusive = true }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        // Don't have account
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bạn chưa có tài khoản? ",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = "Đăng ký",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("SignUpScreen")
                }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}