package fpl.md19.beefashion

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.navigation.BottomNavBar
import fpl.md19.beefashion.ui.theme.BeefashionTheme
import fpl.md19.beefashion.viewModels.AuthViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Thiết lập giao diện composable
        setContent {
            BeefashionTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Truyền navController và authViewModel vào MainScreen
                    val navController = rememberNavController()
                    MainScreen(navController = navController)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Truyền navController và authViewModel vào BottomNavBar
        BottomNavBar(navController = navController)
    }
}
