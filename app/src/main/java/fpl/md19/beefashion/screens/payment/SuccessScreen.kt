package fpl.md19.beefashion.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import fpl.md19.beefashion.models.MyOder
import fpl.md19.beefashion.screens.cart.OrderStatus
import fpl.md19.beefashion.screens.tab.HomeScreen

@Composable
fun SuccessScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)), // Màu nền nhẹ

    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
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
                        text = "Thành công",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                }

                Icon(
                    painter = painterResource(R.drawable.tick_green),
                    contentDescription = "Warning",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
                )

                Text(
                    text = "Đang chờ xác nhận",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Cùng BeeFashion bảo vệ quyền lợi của bạn - KHÔNG CHUYỂN TIỀN TRƯỚC cho Shipper khi đơn hàng chưa được giao tới với bất kỳ lý do gì",
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate("HomeScreen")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                        modifier = Modifier
                            .padding(4.dp)
                            .height(40.dp)
                    ) {
                        Text(
                            text = "Trang chủ",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigate("trackOrderScreen/")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                        modifier = Modifier
                            .padding(4.dp)
                            .height(40.dp)
                    ) {
                        Text(
                            text = "Đơn hàng",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSuccessScreen() {
    val navController = rememberNavController()
    SuccessScreen(
        navController = navController
    )
}
