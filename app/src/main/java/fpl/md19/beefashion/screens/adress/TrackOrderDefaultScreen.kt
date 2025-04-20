package fpl.md19.beefashion.screens.adress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.BottomSheetOrderStatus
import fpl.md19.beefashion.components.Header
import fpl.md19.beefashion.components.formatCurrency
import fpl.md19.beefashion.models.MyOder

@Composable
fun TrackOrderDefaultScreen(
    navController: NavController,
    order: MyOder
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(15.dp)
    ) {
        // Header
        Header(navController)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ItemTrackOrder(
                navController = navController,
                order = order
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tổng cộng",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp // Giảm từ mặc định xuống 14.sp
            )
            Text(
                text = formatCurrency(order.total!!),
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                fontSize = 16.sp // Giảm từ mặc định xuống 14.sp
            )
        }

//        Text(
//            text = "Tổng tiền: ${formatCurrency(22900 * 3)}", // nếu sau này dynamic thì tính tổng
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .padding(vertical = 10.dp)
//                .align(Alignment.End),
//            color = Color.Black
//        )

        BottomSheetOrderStatus(
            navController,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            currentStatus = order.status ?: ""
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewTrackOder () {
//    val navController = rememberNavController()
//    TrackOrderDefaultScreen(navController, MyOder())
//}