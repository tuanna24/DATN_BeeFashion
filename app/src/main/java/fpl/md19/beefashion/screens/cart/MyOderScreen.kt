package fpl.md19.beefashion.screens.cart

import android.os.Build
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import fpl.md19.beefashion.components.formatCurrency

import fpl.md19.beefashion.models.OrderItem
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
    invoiceViewModel : InvoiceViewModel = viewModel()
) {

    val myOders by invoiceViewModel.invoices.observeAsState(emptyList())

    val sortedOrders = myOders.sortedByDescending { order ->
        try {
            OffsetDateTime.parse(order.createdAt)
        } catch (e: Exception) {
            OffsetDateTime.MIN
        }
    }

    LaunchedEffect(Unit) {
        invoiceViewModel.getCustomerInvoices()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
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
            items(sortedOrders) { myOder ->
                MyOderCart(myOder, navController)
            }
        }
    }
}

@Composable
fun MyOderCart(myOder: MyOder, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                val route = "trackOrderScreen/${myOder.status}"
                // navController.navigate("trackOrderScreen")
                navController.navigate(route)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = R.drawable.ao_phong,
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = myOder.id ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = myOder.paymentMethod,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = formatCurrency(myOder.total),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Button(
//                    onClick = { /* Xử lý trạng thái đơn hàng */ },
//                    shape = RoundedCornerShape(8.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(40.dp)
//                ) {
//                    Text(
//                        text = "In Transit",
//                        color = Color.Black,
//                        fontSize = 14.sp
//                    )
//                }

                Button(
                    onClick = {
                        // navController.navigate("trackOrderScreen/Đã lấy hàng")
                        navController.navigate("trackOrderScreen/${myOder.status}")
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = myOder.status!!,
                        color = Color.White,
                        fontSize = 12.sp
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
    MyOderScreen(navController)
}