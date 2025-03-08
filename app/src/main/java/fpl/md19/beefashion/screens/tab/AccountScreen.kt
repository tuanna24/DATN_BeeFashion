package fpl.md19.beefashion.screens.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.R
import fpl.md19.beefashion.components.LogOutComponent
import fpl.md19.beefashion.viewModels.LoginViewModel


@Composable
fun AccountScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val showLogoutDialog = remember { mutableStateOf(false) }
    // Kiểm tra trạng thái đăng nhập dựa vào UserSession và theo dõi sự thay đổi
    var isLoggedIn by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Đảm bảo thông tin đăng nhập được tải khi component được tạo và khi có thay đổi
    LaunchedEffect(Unit) {
        loginViewModel.loadRememberedCredentials(context)
        isLoggedIn = UserSesion.currentUser != null
    }

    // Quan trọng: Theo dõi thay đổi của UserSession để cập nhật trạng thái đăng nhập
    val lifecycleOwner by rememberUpdatedState(newValue = LocalLifecycleOwner.current) // Dùng rememberUpdatedState để lấy lifecycleOwner

    DisposableEffect(lifecycleOwner) { // Truyền lifecycleOwner vào DisposableEffect
        val checkLoginStatus = {
            isLoggedIn = UserSesion.currentUser != null
        }

        val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                loginViewModel.loadRememberedCredentials(context)
                checkLoginStatus()
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, top = 30.dp, end = 25.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = if (isLoggedIn) "Tài khoản" else "Đăng nhập",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            if (isLoggedIn) {
                // Hiển thị các item chỉ khi người dùng đã đăng nhập
                item { Divider() }
                item { AccountItem(R.drawable.ic_orders, "Đơn hàng", navController, "MyOderScreen") }
                item { Divider(thickness = 8.dp, color = Color.LightGray) }
                item { AccountItem(R.drawable.ic_details, "Thông tin", navController, "MyDetailsScreen") }
                item { AccountItem(R.drawable.ic_address, "Địa chỉ", navController, "AddressScreen/{customerId}") }
                item { AccountItem(R.drawable.ic_notifications, "Thông báo", navController, "NotificationsScreen") }
                item { Divider(thickness = 8.dp, color = Color.LightGray) }
                item { AccountItem(R.drawable.ic_help, "Trợ giúp", navController, "HelpScreen") }
                item { Divider(thickness = 8.dp, color = Color.LightGray) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { LogoutItem(onClick = { showLogoutDialog.value = true }) }
            } else {
                // Hiển thị nút đăng nhập khi người dùng chưa đăng nhập
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { LoginItem(navController) }
            }
        }
    }

    // Hiển thị LogOutComponent khi showLogoutDialog là true
    LogOutComponent(
        onConfirm = {
            loginViewModel.logout(context = navController.context)
            UserSesion.currentUser = null
            showLogoutDialog.value = false
            navController.navigate("LoginScreen") {
                popUpTo("HomeScreen") { inclusive = true }
            }
        },
        onDismiss = {
            showLogoutDialog.value = false
        },
        isVisible = showLogoutDialog.value
    )
}

@Composable
fun AccountItem(imageRes: Int, title: String, navController: NavController, route: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(route) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "Arrow",
            modifier = Modifier.size(24.dp)
        )
    }
    Divider()
}

// Thêm Composable cho nút đăng nhập
@Composable
fun LoginItem(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("LoginScreen") }
                .background(color = Color(0xFF3498DB), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.login), // Bạn cần thêm icon đăng nhập
                contentDescription = "Login",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Đăng nhập",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LogoutItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logout),
            contentDescription = "Logout",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Đăng xuất", color = Color.Red, fontSize = 16.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAccountScreen() {
    val navController = rememberNavController()
    AccountScreen(navController)
}