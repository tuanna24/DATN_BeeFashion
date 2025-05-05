package fpl.md19.beefashion.screens.payment

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import vn.zalopay.sdk.ZaloPaySDK

class SuccessScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setContent(){
//            val navController = rememberNavController()
//            SuccessScreen2(navController)
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}

@Composable
fun SuccessScreen2(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
    ) {
        Card (
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column (
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
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
                            navController.navigate("myOderScreen")
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

@Preview (showBackground = true, showSystemUi = true)
@Composable
fun PreviewSuccessScreen2 () {
    val navController = rememberNavController()
    SuccessScreen2(navController)
}