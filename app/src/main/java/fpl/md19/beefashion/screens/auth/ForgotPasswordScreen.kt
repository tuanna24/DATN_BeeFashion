package fpl.md19.beefashion.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.viewModels.AuthState
import fpl.md19.beefashion.viewModels.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit = {}
) {
    // State to hold input values
    val email = remember { mutableStateOf("") }

    // Check if all fields are filled
    val isFormValid = email.value.isNotEmpty()

    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect (authState.value) {
        when (authState.value) {
            is AuthState.Success -> {
                // Hiển thị thông báo thành công
                Toast.makeText(context, (authState.value as AuthState.Success).message, Toast.LENGTH_SHORT).show()
            }
            is AuthState.Error -> {
                // Hiển thị thông báo lỗi
                Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            is AuthState.Loading -> {
                // Thông báo đang xử lý
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
            text = "Quên mật khẩu",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Nhập email của bạn để xác minh. Chúng tôi sẽ gửi mã gồm 4 chữ số đến email của bạn.",
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
                    value = email.value,
                    onValueChange = { email.value = it },
                    placeholder = { Text("Nhập địa chỉ email của bạn") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black
                    )
                )
            }

            // Send Code Button
            Button(
                onClick = { authViewModel.sendPasswordResetEmail(email.value)  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color.Black else Color.LightGray
                ),
                enabled = authState.value != AuthState.Loading,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Gửi mã",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    val navController = rememberNavController()
    val mockAuthViewModel = AuthViewModel()
    ForgotPasswordScreen(navController, mockAuthViewModel)
}