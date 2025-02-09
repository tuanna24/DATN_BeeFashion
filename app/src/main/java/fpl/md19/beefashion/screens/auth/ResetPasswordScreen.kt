package fpl.md19.beefashion.screens.auth


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpl.md19.beefashion.R

@Composable
fun ResetPasswordScreen(
    onBackClick: () -> Unit = {}
) {
    // State to hold input values
    val password = remember { mutableStateOf("") }
    val password2 = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val passwordVisible2 = remember { mutableStateOf(false) }

    // Check if all fields are filled
    val isFormValid = password.value.isNotEmpty() && password2.value.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Text(
            text = "Đặt lại mật khẩu",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Đặt mật khẩu mới cho tài khoản của bạn để bạn có thể đăng nhập và truy cập tất cả các tính năng.",
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
            // Password cũ TextField
            Column {
                Text(
                    text = "Mật khẩu mới",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
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
                                modifier = Modifier.size(24.dp) // Tùy chỉnh kích thước icon
                            )
                        }
                    }
                )
            }

            // Password mới TextField
            Column {
                Text(
                    text = "Nhập lại mật khẩu mới",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = password2.value,
                    onValueChange = { password2.value = it },
                    placeholder = { Text("Nhập mật khẩu của bạn") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black
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
                                modifier = Modifier.size(24.dp) // Tùy chỉnh kích thước icon
                            )
                        }
                    }
                )
            }

            // Send Code Button
            Button(
                onClick = { },
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
                    text = "Tiếp tục",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordScreen()
}