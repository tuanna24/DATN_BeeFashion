package fpl.md19.beefashion.screens.payment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R

@Composable
fun PaymentScreen (navController : NavController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, top = 30.dp, end = 25.dp),
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
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Thanh Toán",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview (showBackground = true, showSystemUi = true)
@Composable
fun PreviewPaymentScreen () {
    val navController = rememberNavController()
    PaymentScreen(navController)
}