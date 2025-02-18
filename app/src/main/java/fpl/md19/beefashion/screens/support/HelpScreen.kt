package fpl.md19.beefashion.screens.support

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R

@Composable
fun HelpScreen(navController: NavController) {
    // State for support form email input
    val support = remember { mutableStateOf("") }

    // Context for launching email intent
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                text = "Trợ giúp",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(25.dp))

        // FAQ Section
        Text(
            text = "Câu hỏi thường gặp",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(25.dp))
        // Example FAQ Item 1
        SupporItem("Làm thế nào để thay đổi mật khẩu của tôi?", "Đi tới phần 'Cài đặt', sau đó chọn 'Thay đổi mật khẩu'.")

        Spacer(modifier = Modifier.height(8.dp))

        // Example FAQ Item 2
        SupporItem("Tôi có thể liên hệ với bộ phận hỗ trợ như thế nào?", "Bạn có thể gửi email đến support@beefashion.com hoặc gọi đến số 1800-1234.")

        Spacer(modifier = Modifier.height(16.dp))

        // Contact Us Section
        Text(
            text = "Liên hệ với chúng tôi",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.Black
        )

        // Clickable email link
        ClickableText(
            text = AnnotatedString("support@beefashion.com"),
            onClick = {
                // Open email client to send an email
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@beefashion.com")
                }
                context.startActivity(intent)
            },
            style = TextStyle(color = Color.Blue)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contact Form (Optional)
        Text(
            text = "Gửi yêu cầu hỗ trợ:",
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.Black
        )

        // Message Input Field
        OutlinedTextField(
            value = support.value, // Use `.value` for mutableStateOf
            onValueChange = { support.value = it },
            placeholder = { Text("Vấn đề cần hỗ trợ") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Submit Button
        Button(onClick = {
            // Handle support request submission
            if (support.value.isNotEmpty()) {
                // Handle sending email or message
                println("Support request submitted: ${support.value}")
            }
        }) {
            Text(text = "Gửi yêu cầu")
        }
    }
}

// A simple composable for FAQ items
@Composable
fun SupporItem(question: String, answer: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = question, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = answer, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHelpScreen() {
    val navController = rememberNavController()
    HelpScreen(navController)
}
