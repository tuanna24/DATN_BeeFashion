package fpl.md19.beefashion


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ReviewsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(15.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back Icon",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { navController.popBackStack()}
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Đánh giá",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_notifications),
                contentDescription = "Notification Icon",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { }
            )
        }

        // Nội dung chính
        RatingSummary()

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        ReviewList()
    }
}


@Composable
fun RatingSummary() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "4.0",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column {
                RatingBar(rating = 4.0f, starSize = 28.dp)
                Text(text = "1034 đánh giá", color = Color.Gray)
            }
        }
        RatingDistribution()
    }
}


@Composable
fun RatingDistribution() {
    val ratings = listOf(80, 50, 30, 10, 5) // Phần trăm cho từng mức đánh giá

    Column(modifier = Modifier.padding(top = 8.dp)) {
        for (i in 5 downTo 1) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                // Hiển thị sao vàng và xám với kích thước 20dp
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(i) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFFFA000), // Màu vàng
                            modifier = Modifier.size(20.dp) // Cập nhật kích thước sao
                        )
                    }
                    repeat(5 - i) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color.LightGray, // Màu xám
                            modifier = Modifier.size(20.dp) // Cập nhật kích thước sao
                        )
                    }
                }

                // Thanh tiến trình
                LinearProgressIndicator(
                    progress = ratings[5 - i] / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                        .height(8.dp),
                    color = Color.Black
                )
            }
        }
    }
}


@Composable
fun ReviewList() {
    val reviews = listOf(
        Review(
            "Nguyen Anh Tuan",
            "6 ngày trước",
            5,
            "Sản phẩm này rất tốt, con trai của tôi rất thích chúng."
        ),
        Review("Chàng tai tốt", "1 tuần trước", 4, "Người bán hàng đóng gói rất nhanh em yêu anh!"),
        Review(
            "Do Tien Thanh",
            "2 tuần trước",
            4,
            "Tôi đã sử dụng nó trong rất nhiều lần ...!"
        )
    )

    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            Text("45 Đánh giá", fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Liên quan")
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "Arrow",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        reviews.forEach { review ->
            ReviewItem(review)
            Divider()
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        RatingBar(rating = review.rating.toFloat())
        Text(text = review.content, modifier = Modifier.padding(vertical = 4.dp))
        Text(text = "${review.author} • ${review.date}", color = Color.Gray, fontSize = 12.sp)
    }
}
@Composable
fun RatingBar(rating: Float, starSize: Dp = 20.dp) {
    Row {
        for (index in 0 until 5) {
            val starId = if (index < rating.toInt()) R.drawable.ic_star_filled else R.drawable.ic_star_border
            Image(
                painter = painterResource(id = starId),
                contentDescription = "Star",
                modifier = Modifier.size(starSize).padding(horizontal = 2.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReviewPreview() {
    val navController = rememberNavController()
    ReviewsScreen(navController)
}

data class Review(val author: String, val date: String, val rating: Int, val content: String)
