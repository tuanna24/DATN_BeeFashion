package fpl.md19.beefashion.screens.payment

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.R
import fpl.md19.beefashion.api.Zalopay.AppInfo.APP_ID
import fpl.md19.beefashion.api.Zalopay.CreateOrder
import fpl.md19.beefashion.models.MyOder
import fpl.md19.beefashion.models.OrderItem
import fpl.md19.beefashion.viewModels.AddressViewModel
import fpl.md19.beefashion.viewModels.CartViewModel
import fpl.md19.beefashion.viewModels.InvoiceViewModel
import kotlinx.coroutines.launch
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.NumberFormat
import java.util.Locale

class SuccessScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setContent(){
            val navController = rememberNavController()
            SuccessScreen(navController)
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}

@Composable
fun PaymentScreen(
    navController: NavController,
    addressViewModel: AddressViewModel = viewModel(),
    invoiceViewModel: InvoiceViewModel = viewModel(),
    orderItems: List<OrderItem>
) {
    val selectedAddress = UserSesion.userSelectedAddress
    var selectedMethod by remember { mutableStateOf("cod") }
    val context = LocalContext.current

    val activity = context as? ComponentActivity ?: return

    val vatPercent = 0
    val shippingFee = 3000
    val subTotal = orderItems.sumOf { it.productPrice * it.quantity }

    val total = subTotal

    val orderItems = UserSesion.userOrderItems

    val cartViewModel: CartViewModel = viewModel()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(15.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                ), // Giảm từ 15.dp, 10.dp xuống 10.dp, 6.dp
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Text(
                text = "Thanh toán",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(20.dp))
        }

        // Địa chỉ giao hàng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(10.dp)
                .clickable {
                    navController.navigate("AddressScreen/${UserSesion.currentUser!!.id}/true")
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = selectedAddress?.name ?: "Tên người nhận",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = selectedAddress?.phoneNumber ?: "Số điện thoại",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = selectedAddress?.detail ?: "Chưa có địa chỉ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Edit Address",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp)) // Giảm từ 10.dp xuống 6.dp

        // Sản phẩm
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)) // Giảm từ 10.dp xuống 8.dp
                .padding(10.dp) // Giảm từ 15.dp xuống 10.dp
        ) {
            LazyColumn {
                item {
                    Text(
                        text = "Sản phẩm",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                items(orderItems.size) { index ->
                    PaymentItem(
                        name = orderItems[index].productName,
                        imageLink = orderItems[index].productImage,
                        size = orderItems[index].sizeName,
                        quantity = orderItems[index].quantity,
                        price = orderItems[index].productPrice
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp)) // Giảm từ 10.dp xuống 6.dp

        // Phương thức thanh toán
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)) // Giảm từ 10.dp xuống 8.dp
                .padding(4.dp) // Giảm từ 6.dp xuống 4.dp
        ) {
            Column(
                modifier = Modifier.padding(6.dp) // Giảm từ 10.dp xuống 6.dp
            ) {
                Text(
                    text = "Phương thức thanh toán",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp // Giảm từ 16.sp xuống 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp)) // Giảm từ 12.dp xuống 8.dp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable {
                            selectedMethod = "cod"
                            Toast.makeText(
                                context,
                                "Bạn đã chọn thanh toán khi nhận hàng!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Thanh toán khi nhận hàng",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    RadioButton(
                        selected = selectedMethod == "cod",
                        onClick = {
                            selectedMethod = "cod"
                            Toast.makeText(
                                context,
                                "Bạn đã chọn thanh toán khi nhận hàng!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedMethod = "zalopay"
                            Toast.makeText(
                                context,
                                "Bạn đã chọn thanh toán bằng ZaloPay!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Thanh toán bằng ZaloPay",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    RadioButton(
                        selected = selectedMethod == "zalopay",
                        onClick = {
                            selectedMethod = "zalopay"
                            Toast.makeText(
                                context,
                                "Bạn đã chọn thanh toán bằng ZaloPay!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Giảm từ 12.dp xuống 8.dp
            }
        }

        Spacer(modifier = Modifier.height(6.dp)) // Giảm từ 10.dp xuống 6.dp

        // Chi tiết thanh toán
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)) // Giảm từ 10.dp xuống 8.dp
                .padding(4.dp) // Giảm từ 6.dp xuống 4.dp
        ) {
            Column(
                modifier = Modifier.padding(6.dp) // Giảm từ 10.dp xuống 6.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tổng tiền hàng",
                        color = Color.Gray,
                        fontSize = 12.sp // Giảm từ mặc định xuống 12.sp
                    )
                    Text(
                        text = formatCurrency(subTotal),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp // Giảm từ mặc định xuống 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp)) // Giảm từ 12.dp xuống 8.dp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Phí vận chuyển",
                        color = Color.Gray,
                        fontSize = 12.sp // Giảm từ mặc định xuống 12.sp
                    )
                    Text(
                        text = formatCurrency(shippingFee),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp // Giảm từ mặc định xuống 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp)) // Giảm từ 12.dp xuống 8.dp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Giảm giá vận chuyển",
                        color = Color.Gray,
                        fontSize = 12.sp // Giảm từ mặc định xuống 12.sp
                    )
                    Text(
                        text = "-${formatCurrency(shippingFee)}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        fontSize = 12.sp // Giảm từ mặc định xuống 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp)) // Giảm từ 12.dp xuống 8.dp
                Divider()
                Spacer(modifier = Modifier.height(8.dp)) // Giảm từ 12.dp xuống 8.dp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tổng cộng",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp // Giảm từ mặc định xuống 14.sp
                    )
                    Text(
                        text = formatCurrency(total),
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        fontSize = 14.sp // Giảm từ mặc định xuống 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Nút đặt hàng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp), // Giảm từ 15.dp xuống 10.dp
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Tổng thanh toán",
                    fontSize = 12.sp, // Giảm từ 14.sp xuống 12.sp
                    color = Color.Gray
                )
                Text(
                    text = formatCurrency(total),
                    fontSize = 16.sp, // Giảm từ 18.sp xuống 16.sp
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
            Button(
                onClick = {
                    if (selectedMethod == "cod") {
                        invoiceViewModel.newCustomerInvoices(
                            MyOder(
                                customerID = UserSesion.currentUser!!.id,
                                addressID = selectedAddress!!.id,
                                paidStatus = false,
                                invoiceItemDTOs = UserSesion.userOrderItems,
                                paymentMethod = selectedMethod,
                                total = total,
                            )
                        )
                        Toast.makeText(context, "Bạn đã đặt hàng thành công!", Toast.LENGTH_SHORT).show()
                        navController.navigate("successScreen")
                        NotificationUtils.showOrderSuccessNotification(context)
                    } else {
                        Log.d("ZaloPay", "Button clicked")

                        try {
                            ZaloPaySDK.init(APP_ID, Environment.SANDBOX)
                            Log.d("ZaloPayInit", "ZaloPay SDK initialized successfully")
                        } catch (e: Exception) {
                            Log.e("ZaloPayInitError", "Error initializing ZaloPay SDK: ${e.message}")
                            e.printStackTrace()
                            return@Button
                        }

                        fun createNotificationChannel(context: Context) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val channel = NotificationChannel(
                                    "beeslearn_notifications",
                                    "BeesLearn Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                                ).apply {
                                    description = "Thông báo từ ứng dụng BeeFashion!"
                                }
                                val manager =
                                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                manager.createNotificationChannel(channel)
                            }
                        }

                        // Gửi thông báo
                        fun sendNotification(context: Context, title: String, message: String) {
                            val channelId = "beeslearn_notifications"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val permission = Manifest.permission.POST_NOTIFICATIONS
                                if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(
                                        (context as Activity),
                                        arrayOf(permission),
                                        1
                                    )
                                    return
                                }
                            }
                            val notification = NotificationCompat.Builder(context, channelId)
                                .setSmallIcon(R.drawable.logo)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .build()
                            NotificationManagerCompat.from(context).notify(0, notification)
                        }

                        val orderApi = CreateOrder()
                        activity.lifecycleScope.launch {
                            try {
                                val data = orderApi.createOrder(subTotal.toString())
                                println("Amount ${subTotal}")
                                println("Zalopay $data")
                                val code: String = data.getString("return_code")
                                println("ZaloPay Order created successfully: $code")

                                if (code == "1") {
                                    println("test")
                                    val token = data.getString("zp_trans_token")
                                    ZaloPaySDK
                                        .getInstance()
                                        .payOrder(
                                            activity, token, "demozpdk://app",
                                            object : PayOrderListener {
                                                override fun onPaymentSucceeded(
                                                    payUrl: String?,
                                                    transToken: String?,
                                                    appTransID: String?
                                                ) {
                                                    Toast.makeText(context, "Thanh toán thành công", Toast.LENGTH_SHORT).show()
                                                    println("ZaloPay Payment succeeded: payUrl=$payUrl, transToken=$transToken, appTransID=$appTransID")
//                                            navController.navigate("homeScreen") {
//                                                popUpTo("currentScreen") { inclusive = true } // Xóa màn hình hiện tại khỏi stack
//                                            }
                                                    sendNotification(
                                                        context,
                                                        "BeesFashion",
                                                        "Bạn đã thanh toán thành công đơn hàng ${" "} bằng ZaloPay!"
                                                    )
                                                    invoiceViewModel.newCustomerInvoices(
                                                        MyOder(
                                                            customerID = UserSesion.currentUser!!.id,
                                                            addressID = selectedAddress!!.id,
                                                            paidStatus = true,
                                                            invoiceItemDTOs = UserSesion.userOrderItems,
                                                            paymentMethod = selectedMethod,
                                                            total = total
                                                        )
                                                    )
                                                }

                                                override fun onPaymentCanceled(
                                                    payUrl: String?,
                                                    transToken: String?
                                                ) {
                                                    Toast.makeText(context, "Hủy thanh toán", Toast.LENGTH_SHORT).show()
                                                    Log.d("ZaloPay", "Payment canceled: payUrl=$payUrl, transToken=$transToken")
                                                    sendNotification(
                                                        context,
                                                        "BeesLearn",
                                                        "Bạn Đã Hủy Thanh Toán"
                                                    )
                                                }

                                                override fun onPaymentError(
                                                    error: ZaloPayError?,
                                                    payUrl: String?,
                                                    transToken: String?
                                                ) {
                                                    Toast.makeText(context, "Lỗi thanh toán!", Toast.LENGTH_SHORT).show()
                                                    Log.e("ZaloPayError", "Payment error: payUrl=$payUrl, transToken=$transToken")
                                                    sendNotification(
                                                        context,
                                                        "BeesLearn",
                                                        "Lôĩ Thanh Toán!"
                                                    )
                                                }
                                            }
                                        )
                                } else {
                                    Toast
                                        .makeText(context, "Không thể tạo đơn hàng", Toast.LENGTH_SHORT).show()
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast
                                    .makeText(context, "Đã xảy ra lỗi", Toast.LENGTH_SHORT)
                                    .show()
                                Log.e("ZaloPayError", "Exception: ${e.message}")
                            }
                        }
                        Toast.makeText(context, "Thanh toan bang zalopay", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = selectedAddress?.id != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                modifier = Modifier
                    .padding(4.dp)
                    .height(36.dp)
            ) {
                Text(
                    text = "Đặt hàng",
                    color = Color.White,
                    fontSize = 14.sp // Giảm từ 16.sp xuống 14.sp
                )
            }
        }
    }
}

@Composable
fun PaymentItem(
    name: String,
    imageLink: String,
    size: String,
    quantity: Int,
    price: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageLink,
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Size: $size",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "SL: x$quantity",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = formatCurrency(price),
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                fontSize = 14.sp
            )
        }
    }
}

fun formatCurrency(price: Any?): String {
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    return when (price) {
        is Number -> formatter.format(price.toLong()) + " ₫"
        is String -> price.toLongOrNull()?.let { formatter.format(it) + " ₫" } ?: "0 ₫"
        else -> "0 ₫"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPaymentScreen() {
    val navController = rememberNavController()
    PaymentScreen(navController = navController, orderItems = listOf<OrderItem>())
}

