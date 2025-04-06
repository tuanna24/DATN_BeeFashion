package fpl.md19.beefashion.screens.adress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.BottomSheetOrderStatus
import fpl.md19.beefashion.components.Header

@Composable
fun TrackOrderDefaultScreen(
    navController: NavController,
    fullAddress: String?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(15.dp)
        ) {
            // Header
            Header(navController)

            ItemTrackOrder(
                navController = navController,
                fullAddress = fullAddress,
                modifier = Modifier.weight(1f)
            )

            Box(modifier = Modifier
                .fillMaxSize()
                .weight(2f)
            ) {
                BottomSheetOrderStatus(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    currentStatus = "Đang chờ xác nhận"
                )
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTrackOder () {
    val navController = rememberNavController()
    TrackOrderDefaultScreen(navController, fullAddress = "")
}