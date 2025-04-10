package fpl.md19.beefashion

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import fpl.md19.beefashion.api.Zalopay.AppInfo.APP_ID
import fpl.md19.beefashion.navigation.BottomNavBar
import fpl.md19.beefashion.ui.theme.BeefashionTheme
import fpl.md19.beefashion.viewModels.AuthViewModel
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ZaloPaySDK.init(2553.toInt(), Environment.SANDBOX)
        // Khởi tạo ViewModel
        val authViewModel: AuthViewModel by viewModels()

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
        try {
            ZaloPaySDK.init(APP_ID, Environment.SANDBOX)
            Log.d("ZaloPayInit", "ZaloPay SDK initialized successfully")
        } catch (e: Exception) {
            Log.e("ZaloPayInitError", "Error initializing ZaloPay SDK: ${e.message}")
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
