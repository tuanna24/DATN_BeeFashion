package fpl.md19.beefashion.screens.tab

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.R
import fpl.md19.beefashion.components.LogOutComponent
import fpl.md19.beefashion.screens.adress.NotifiSharePre
import fpl.md19.beefashion.screens.adress.NotificationStatus.createNotificationChannel
import fpl.md19.beefashion.screens.adress.NotificationStatus.sendOrderStatusNotification
import fpl.md19.beefashion.screens.adress.NotificationStatus.sendOrderStatusNotification1
import fpl.md19.beefashion.viewModels.InvoiceViewModel
import fpl.md19.beefashion.viewModels.LoginViewModel
import java.time.OffsetDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AccountScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(),
    invoiceViewModel: InvoiceViewModel = viewModel(),
    viewModel: NotifiSharePre = viewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(UserSesion.currentUser != null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        loginViewModel.loadRememberedCredentials(context) {
            isLoggedIn = UserSesion.currentUser != null
        }
        isLoggedIn = UserSesion.currentUser != null
    }

    val myOrders by invoiceViewModel.invoices.observeAsState(emptyList())
    val sortedOrders = myOrders.sortedByDescending { order ->
        try {
            OffsetDateTime.parse(order.createdAt)
        } catch (e: Exception) {
            OffsetDateTime.MIN
        }
    }
    LaunchedEffect(Unit) {
        invoiceViewModel.getCustomerInvoices()
    }

    var lastOrderStatusMap by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    LaunchedEffect(sortedOrders) {
        sortedOrders.forEach { order ->
            val orderId = order.id?.take(8) ?: return@forEach
            val currentStatus = order.status ?: return@forEach

            val previousStatus = lastOrderStatusMap[orderId]

            if (previousStatus != currentStatus) {
                if (viewModel.shouldNotify(orderId, currentStatus, context)) {
                    createNotificationChannel(context)
                    sendOrderStatusNotification1(context, orderId, currentStatus)

                    Toast.makeText(context, "Đơn $orderId cập nhật trạng thái: $currentStatus", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Cập nhật map lưu trạng thái sau khi xử lý
        lastOrderStatusMap = sortedOrders.mapNotNull { order ->
            val orderId = order.id?.take(8)
            val status = order.status
            if (orderId != null && status != null) {
                orderId to status
            } else null
        }.toMap()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Nền nhẹ nhàng
            .padding(15.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tài khoản",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoggedIn) {
                item { UserProfileItem(navController, "MyDetailsScreen") }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { AccountItem(R.drawable.ic_orders, "Đơn hàng", navController, "myOderScreen") }
                item { AccountItem(R.drawable.ic_address, "Địa chỉ", navController, "AddressScreen/{customerId}") }
                item { AccountItem(R.drawable.ic_notifications, "Thông báo", navController, "NotificationsScreen") }
                item { AccountItem(R.drawable.ic_help, "Trợ giúp", navController, "HelpScreen") }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { LogoutItem(onClick = { showLogoutDialog = true }) }
            } else {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { LoginItem(navController) }
            }
        }
    }

    if (isLoggedIn) {
        LogOutComponent(
            onConfirm = {
                loginViewModel.logout(context = navController.context)
                UserSesion.currentUser = null
                showLogoutDialog = false
                isLoggedIn = false
                navController.navigate("LoginScreen") {
                    popUpTo("HomeScreen") { inclusive = true }
                }
            },
            onDismiss = { showLogoutDialog = false },
            isVisible = showLogoutDialog
        )
    }
}

@Composable
fun UserProfileItem(navController: NavController, route: String) {
    val currentUser = UserSesion.currentUser
    val fullName = currentUser?.fullName ?: "Không có tên"
    val avatarUrl = currentUser?.image

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(route) }
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = avatarUrl ?: R.drawable.ic_details, // URL hoặc resource mặc định
                contentDescription = "Avatar",
                placeholder = painterResource(id = R.drawable.ic_details), // Hiển thị khi đang tải
                error = painterResource(id = R.drawable.ic_details), // Hiển thị khi lỗi
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = fullName,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "Arrow",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
fun AccountItem(imageRes: Int, title: String, navController: NavController, route: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(route) }
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "Arrow",
                modifier = Modifier.size(24.dp),
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa các mục
}

@Composable
fun LogoutItem(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logout),
                contentDescription = "Logout",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Đăng xuất",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
    }
}

@Composable
fun LoginItem(navController: NavController) {
    Button(
        onClick = { navController.navigate("LoginScreen") },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Login",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Đăng nhập",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAccountScreen() {
    val navController = rememberNavController()
    AccountScreen(navController)
}