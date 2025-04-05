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
fun TrackOrderShippingScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(15.dp)
        ) {
            // Header
            Header(navController)

            Box(modifier = Modifier.fillMaxSize()) {
                BottomSheetOrderStatus(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    currentStatus = "Đang vận chuyển"
                )
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TrackOrderShippingScreenPreview () {
    val navController = rememberNavController()
    TrackOrderShippingScreen(navController)
}