package fpl.md19.beefashion.screens.adress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fpl.md19.beefashion.models.MyOder
import fpl.md19.beefashion.models.OrderItem
import fpl.md19.beefashion.screens.payment.formatCurrency

@Composable
fun ItemTrackOrder(
    navController: NavController,
    order: MyOder
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp)) // Giảm từ 10.dp xuống 8.dp
            .padding(10.dp) // Giảm từ 15.dp xuống 10.dp
    ) {
        item {
            Text(
                text = "Địa chỉ: ${order.fullAddress}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sản phẩm",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(order.invoiceItemDTOs) { orderItem ->
            PaymentItem(
                orderItem = orderItem
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PaymentItem(
    orderItem: OrderItem
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = orderItem.product!!.image,
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = orderItem.product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Size: ${orderItem.size!!.name}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "SL: x${orderItem.quantity}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = formatCurrency(orderItem.product.price * orderItem.quantity),
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                fontSize = 14.sp
            )
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ItemTrackOrderPreview () {
//    val navController = rememberNavController()
//    ItemTrackOrder(navController = navController)
//}