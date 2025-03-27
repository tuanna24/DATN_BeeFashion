package fpl.md19.beefashion.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import fpl.md19.beefashion.viewModels.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Tải thông tin đăng nhập đã lưu (nếu có)
        loginViewModel.loadRememberedCredentials(context) {
            // Callback khi tự động đăng nhập thành công
            // Không cần delay ở đây vì sẽ delay chung bên dưới
            navController.navigate("HomeScreen") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }

        // Delay 3 giây một lần duy nhất trước khi điều hướng
        delay(3000)

        // Kiểm tra trạng thái đăng nhập và điều hướng
        if (loginViewModel.loginResponse.value?.isSuccessful == true || loginViewModel.isRemembered) {
            navController.navigate("HomeScreen") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        } else {
            navController.navigate("WelcomeScreen1") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center), // Căn giữa cả cột trong box
            horizontalAlignment = Alignment.CenterHorizontally, // Căn giữa theo chiều ngang
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .offset(y = 80.dp)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    val navController = rememberNavController()
    WelcomeScreen(navController)
}