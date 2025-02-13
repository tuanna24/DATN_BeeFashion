package fpl.md19.beefashion.screens.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import fpl.md19.beefashion.models.HomeProduct
import java.text.Normalizer
import java.util.regex.Pattern

@Composable
fun SearchScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }

    // Danh sách sản phẩm
    val productList = listOf(
        HomeProduct("quan thể thao", 189000, R.drawable.ao_phong, "M", "-52%"),
        HomeProduct("Áo phao thao", 189000, R.drawable.ao_phong, "L"),
        HomeProduct("Áo phong thao", 189000, R.drawable.ao_phong, "M", "-2%"),
        HomeProduct("Áo phong cách", 189000, R.drawable.ao_phong, "L", "-52%"),
        HomeProduct("Áo phông thao", 189000, R.drawable.ao_phong, "M", "-52%"),
        HomeProduct("Áo thể thao", 189000, R.drawable.ao_phong, "L", "-52%"),
        HomeProduct("Áo thể thao", 189000, R.drawable.ao_phong, "L", "-52%"),
    )

    // Lọc danh sách sản phẩm theo từ khóa tìm kiếm
    val normalizedSearchWords = removeDiacritics(searchText.trim())
        .lowercase()
        .split("\\s+".toRegex()) // Tách thành danh sách từ

    val filteredList = productList.filter { product ->
        val normalizedTitle = removeDiacritics(product.title).lowercase()

        // Kiểm tra xem tất cả từ trong searchWords có xuất hiện trong tiêu đề không
        normalizedSearchWords.all { word -> normalizedTitle.contains(word) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, top = 30.dp, end = 25.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Thanh tiêu đề với nút back và tiêu đề "Search"
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
                text = "Tìm kiếm",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Thanh tìm kiếm
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Search", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nếu danh sách lọc rỗng, hiển thị thông báo "Không có sản phẩm cần tìm"
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "No results",
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Không tìm thấy kết quả",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hãy thử một từ tương tự hoặc một cái gì đó chung chung hơn",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp)
                    )
                }
            }
        } else {
            // Hiển thị danh sách sản phẩm từ HomeProduct
            LazyColumn {
                items(filteredList) { product ->
                    ProductItem(product, navController)
                }
            }
        }
    }
}

// Hàm loại bỏ dấu tiếng Việt
fun removeDiacritics(input: String): String {
    val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(normalized).replaceAll("")
}

// Composable hiển thị từng sản phẩm
@Composable
fun ProductItem(item: HomeProduct, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable { /* Xử lý click vào sản phẩm */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            modifier = Modifier
                .size(50.dp) // Kích thước ảnh
                .border(0.2.dp, Color.Black, RoundedCornerShape(8.dp)) // Viền đen 2dp, bo tròn góc 8dp
                .padding(2.dp), // Tạo khoảng cách giữa viền và ảnh,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Size: ${item.size}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "${item.price}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Go",
            Modifier
                .clickable{navController.navigate("productScreen") }
                .size(30.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSearchScreen() {
    val navController = rememberNavController()
    SearchScreen(navController)
}
