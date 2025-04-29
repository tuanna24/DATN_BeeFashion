package fpl.md19.beefashion.screens.cart

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import fpl.md19.beefashion.models.MyOder
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import fpl.md19.beefashion.GlobalVarible.UserSesion

import fpl.md19.beefashion.screens.adress.NotificationStatus.createNotificationChannel
import fpl.md19.beefashion.screens.adress.NotificationStatus.sendOrderStatusNotification
import fpl.md19.beefashion.screens.adress.NotifiSharePre
import fpl.md19.beefashion.screens.adress.NotificationStatus.sendOrderStatusNotification1
import fpl.md19.beefashion.viewModels.InvoiceViewModel
import java.text.NumberFormat
import java.time.OffsetDateTime
import java.util.Locale

enum class OrderStatus(val label: String) {
    WAITING_CONFIRM("Đang chờ xác nhận"),
    CONFIRMED("Đã xác nhận đơn hàng"),
    PICKED_UP("Đã lấy hàng"),
    SHIPPING("Đang vận chuyển"),
    DELIVERED("Đã giao hàng");

    companion object {
        fun fromName(name: String): OrderStatus {
            return values().firstOrNull { it.name == name } ?: WAITING_CONFIRM
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyOderScreen(
    navController: NavController,
    invoiceViewModel: InvoiceViewModel = viewModel(),
    viewModel: NotifiSharePre = viewModel()
) {

    val myOders by invoiceViewModel.invoices.observeAsState(emptyList())

    val sortedOrders = myOders.sortedByDescending { order ->
        try {
            OffsetDateTime.parse(order.createdAt)
        } catch (e: Exception) {
            OffsetDateTime.MIN
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        invoiceViewModel.getCustomerInvoices()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.Top
    ) {
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
                text = "Đơn hàng",
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

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(sortedOrders) { order ->
                LaunchedEffect(order.id, order.status) {// order thay vi myOrder
                    val orderId = order.id?.take(8) ?: return@LaunchedEffect
                    if (viewModel.shouldNotify(orderId, order.status, context)) {
                        createNotificationChannel(context)
                        sendOrderStatusNotification1(context, orderId, order.status ?: "")

                        Toast.makeText(context, "Thông báo mới: ${order.status}", Toast.LENGTH_SHORT).show()
                    }
                }
                MyOderCart(order, navController)

            }
        }
    }
}

@Composable
fun MyOderCart(
    myOder: MyOder,
    navController: NavController,
    viewModel: NotifiSharePre = viewModel()
) {

    val context = LocalContext.current

    // cachs 1
//    LaunchedEffect(myOder.id, myOder.status) {
//        val orderId = myOder.id?.take(8) ?: return@LaunchedEffect
//        if (viewModel.shouldNotify(orderId, myOder.status, context)) {
//            createNotificationChannel(context)
//            sendOrderStatusNotification(context, orderId, myOder.status ?: "")
//        }
//    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                UserSesion.selectedOrder = myOder
                navController.navigate("trackOrderScreen")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
        ) {
            AsyncImage(
                model = myOder.invoiceItemDTOs[0].product!!.image,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = myOder.invoiceItemDTOs[0].product?.name ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Đơn hàng: #OD_${myOder.id?.take(8) ?: ""}...",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Gray)) {
                            append("Phương thức thanh toán: ")
                        }
                        withStyle(style = SpanStyle(color = Color.Red)) { // Đổi màu ở đây
                            append(myOder.paymentMethod.toUpperCase())
                        }
                    },
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatCurrency(myOder.total),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(
                        text = myOder.status!!,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMyOderScreen() {
    val navController = rememberNavController()
    //MyOderScreen(navController)
}