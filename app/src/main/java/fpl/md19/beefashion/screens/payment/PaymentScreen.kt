package fpl.md19.beefashion.screens.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PaymentScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(10.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Thanh toán",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(24.dp)) // Giữ khoảng trống cân đối
        }

        // Địa chỉ giao hàng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(15.dp)
                .clickable{
                    navController.navigate("addressScreen")
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.LocationOn, contentDescription = "Location", tint = Color.Gray
                )
                Column (
                    modifier = Modifier
                        .weight(1f)
                ) {

                    Text (

                        text = "Tún Nè (+84) 966 347 311",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Số Nhà 34, Ngõ 143/1 Đường Xuân Phương, Phường Phương Canh, Quận Nam Từ Liêm, Hà Nội",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Edit Address",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }


        Spacer(modifier = Modifier.height(10.dp))

        // Sản phẩm
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(15.dp)
        ) {
            Column {
                Text(text = "AKEDO", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Image(
                        painterResource(R.drawable.ao_phong),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "⚡ Giá Sốc ⚡ Thắt lưng nam da cao cấp khóa kim...", fontSize = 14.sp)
                        Text(text = "Cón Lóc Vàng", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            text = formatCurrency(23900),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "x1", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chi tiết thanh toán
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(15.dp)
        ) {
            Column (
                modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Tổng tiền hàng", color = Color.Gray)
                    Text(text = formatCurrency(23900), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Phí vận chuyển", color = Color.Gray)
                    Text(text = formatCurrency(5000), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Giảm giá vận chuyển", color = Color.Gray)
                    Text(text = "-${formatCurrency(5000)}", fontWeight = FontWeight.Bold, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Divider()

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Tổng cộng", fontWeight = FontWeight.Bold)
                    Text(text = formatCurrency(22900), fontWeight = FontWeight.Bold, color = Color.Red)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Nút đặt hàng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Tổng thanh toán", fontSize = 14.sp, color = Color.Gray)
                Text(text = formatCurrency(22900), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Red)
            }
            Button(
                onClick = { /* TODO: Xử lý đặt hàng */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Đặt hàng", color = Color.White, fontSize = 16.sp)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPaymentScreen() {
    val navController = rememberNavController()
    PaymentScreen(navController)
}

