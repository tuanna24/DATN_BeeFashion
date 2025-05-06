package fpl.md19.beefashion

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.api.Zalopay.AppInfo.APP_ID
import fpl.md19.beefashion.navigation.BottomNavBar
import fpl.md19.beefashion.screens.adress.NotificationStatus
import fpl.md19.beefashion.screens.adress.NotificationStatus.createNotificationChannel
import fpl.md19.beefashion.screens.adress.NotificationStatus.sendOrderStatusNotification
import fpl.md19.beefashion.ui.theme.BeefashionTheme
import fpl.md19.beefashion.viewModels.AuthViewModel
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import org.checkerframework.checker.units.qual.Time
import android.content.Intent
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

        // Tạo các notification channel khi mở app
        NotificationStatus.createNotificationChannel(this)

        MyFirebaseMessagingService().createNotificationChannel(this)


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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            UserSesion.deviceNotificationToken = it.result
            println("FCM device reg token: ${it.result}")
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Truyền navController và authViewModel vào BottomNavBar
        BottomNavBar(navController = navController)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Ứng dụng nhận thông báo từ Firebase!")
        }
    }
}
