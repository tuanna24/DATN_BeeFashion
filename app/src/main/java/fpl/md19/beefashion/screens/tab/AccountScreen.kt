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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import fpl.md19.beefashion.components.LogOutComponent
import fpl.md19.beefashion.viewModels.AuthViewModel


@Composable
fun AccountScreen (navController: NavController,  authViewModel: AuthViewModel) {
    var showLogoutDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Tài khoản",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_notifications),
                contentDescription = "Notification Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            item { Divider() }
            item { AccountItem(R.drawable.ic_orders, "Đơn hàng", navController, "MyOderScreen") }
            item { Divider(thickness = 8.dp, color = Color.LightGray) }
            item { AccountItem(R.drawable.ic_details, "Thông tin", navController, "MyDetailsScreen") }
            item { AccountItem(R.drawable.ic_address, "Địa chỉ", navController, "AddressScreen") }
            item { AccountItem(R.drawable.ic_notifications, "Thông báo", navController, "NotificationsScreen") }
            item { Divider(thickness = 8.dp, color = Color.LightGray) }
            item { AccountItem(R.drawable.ic_help, "Trợ giúp", navController, "HelpScreen") }
            item { Divider(thickness = 8.dp, color = Color.LightGray) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { LogoutItem(onClick = { showLogoutDialog.value = true }) }
        }
    }
    // Hiển thị LogOutComponent khi showLogoutDialog là true
    LogOutComponent(
        onConfirm = {
            // Handle logout logic here
            authViewModel.signout()
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

@Composable
fun LogoutItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {  onClick()  }
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
@Preview (showBackground = true, showSystemUi = true)
@Composable
fun PreviewAccountScreen () {
    val navController = rememberNavController()
    val mockAuthViewModel = AuthViewModel()
    AccountScreen(navController, mockAuthViewModel)
}