package fpl.md19.beefashion.screens.adress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import fpl.md19.beefashion.screens.payment.PaymentItem

@Composable
fun ItemTrackOrder(
    navController: NavController,
    fullAddress: String? ,
    modifier: Modifier = Modifier
) {

    val paymentProductList = listOf("Sản phẩm 1", "Sản phẩm 2", "Sản phẩm 3")

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = if (fullAddress != null) "Địa chỉ: $fullAddress" else "Chưa có địa chỉ",
                fontSize = 14.sp,
                color = if (fullAddress != null) Color.Black else Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sản phẩm",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
        }

        items(paymentProductList) { productName ->
            PaymentItem(
                name = productName,
                imageRes = R.drawable.ao_phong,
                size = "M",
                quantity = 1,
                price = 22900
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItemTrackOrderPreview () {
    val navController = rememberNavController()
    ItemTrackOrder(navController = navController, fullAddress = "")
}