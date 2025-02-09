package fpl.md19.beefashion.screens.accounts

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
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
            .background(Color.White)
    ) {
        // Top Bar
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Thông Báo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Quay lại"
                    )
                }
            },
            actions = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Chuông thông báo",
                    modifier = Modifier.padding(end = 16.dp)
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                navigationIconContentColor = Color.Black,
                actionIconContentColor = Color.Black
            )
        )

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
            NotificationSettingItem(
                title = "Rung",
                checked = vibrate,
                onCheckedChange = { vibrate = it }
            )
            NotificationSettingItem(
                title = "Ưu Đãi Đặc Biệt",
                checked = specialOffers,
                onCheckedChange = { specialOffers = it }
            )
            NotificationSettingItem(
                title = "Khuyến Mãi & Giảm Giá",
                checked = promoDiscounts,
                onCheckedChange = { promoDiscounts = it }
            )
            NotificationSettingItem(
                title = "Thanh Toán",
                checked = payments,
                onCheckedChange = { payments = it }
            )
            NotificationSettingItem(
                title = "Hoàn Tiền",
                checked = cashback,
                onCheckedChange = { cashback = it }
            )
            NotificationSettingItem(
                title = "Cập Nhật Ứng Dụng",
                checked = appUpdates,
                onCheckedChange = { appUpdates = it }
            )
            NotificationSettingItem(
                title = "Dịch Vụ Mới Có Sẵn",
                checked = newService,
                onCheckedChange = { newService = it }
            )
            NotificationSettingItem(
                title = "Mẹo Mới Có Sẵn",
                checked = newTips,
                onCheckedChange = { newTips = it }
            )
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
    NotificationsScreen()
}
