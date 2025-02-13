package fpl.md19.beefashion.screens.auth


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fpl.md19.beefashion.R


@Composable
fun WelcomeScreen() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center), // Căn giữa cả cột trong box
            horizontalAlignment = Alignment.CenterHorizontally, // Căn giữa theo chiều ngang
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .offset(y = 80.dp)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun WelcomeScreenPreview() {
   WelcomeScreen()
}