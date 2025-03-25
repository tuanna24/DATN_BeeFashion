package fpl.md19.beefashion.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    onBackClick: () -> Unit = {}
) {
    var generalNotifications by remember { mutableStateOf(true) }
    var sound by remember { mutableStateOf(true) }
    var vibrate by remember { mutableStateOf(false) }
    var specialOffers by remember { mutableStateOf(true) }
    var promoDiscounts by remember { mutableStateOf(false) }
    var payments by remember { mutableStateOf(false) }
    var cashback by remember { mutableStateOf(true) }
    var appUpdates by remember { mutableStateOf(false) }
    var newService by remember { mutableStateOf(true) }
    var newTips by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Thông báo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(25.dp))

        Divider(color = Color.LightGray, thickness = 1.dp)

        // Settings List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            NotificationSettingItem(
                title = "Thông Báo Chung",
                checked = generalNotifications,
                onCheckedChange = { generalNotifications = it }
            )
            NotificationSettingItem(
                title = "Âm Thanh",
                checked = sound,
                onCheckedChange = { sound = it }
            )
//            NotificationSettingItem(
//                title = "Rung",
//                checked = vibrate,
//                onCheckedChange = { vibrate = it }
//            )
//            NotificationSettingItem(
//                title = "Ưu Đãi Đặc Biệt",
//                checked = specialOffers,
//                onCheckedChange = { specialOffers = it }
//            )
//            NotificationSettingItem(
//                title = "Khuyến Mãi & Giảm Giá",
//                checked = promoDiscounts,
//                onCheckedChange = { promoDiscounts = it }
//            )
            NotificationSettingItem(
                title = "Thanh Toán",
                checked = payments,
                onCheckedChange = { payments = it }
            )
            NotificationSettingItem(
                title = "Mua hàng",
                checked = cashback,
                onCheckedChange = { cashback = it }
            )
//            NotificationSettingItem(
//                title = "Cập Nhật Ứng Dụng",
//                checked = appUpdates,
//                onCheckedChange = { appUpdates = it }
//            )
//            NotificationSettingItem(
//                title = "Dịch Vụ Mới Có Sẵn",
//                checked = newService,
//                onCheckedChange = { newService = it }
//            )
//            NotificationSettingItem(
//                title = "Mẹo Mới Có Sẵn",
//                checked = newTips,
//                onCheckedChange = { newTips = it }
//            )
        }
    }
}

@Composable
private fun NotificationSettingItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color.Black,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
    Divider(color = Color.LightGray, thickness = 0.5.dp)
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    val navController = rememberNavController()
    NotificationsScreen(navController)
}
