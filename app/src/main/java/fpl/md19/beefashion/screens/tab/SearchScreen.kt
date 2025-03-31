package fpl.md19.beefashion.screens.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import fpl.md19.beefashion.R
import fpl.md19.beefashion.models.Products
import fpl.md19.beefashion.viewModels.ProductsViewModels
import java.text.Normalizer
import java.text.NumberFormat
import java.util.Locale
import java.util.regex.Pattern

@Composable
fun SearchScreen(
    navController: NavController,
    productsViewModels: ProductsViewModels = viewModel(),
) {
    var searchText by remember { mutableStateOf("") }
    val products by productsViewModels.products
    val loading by productsViewModels.loading
    val errorMessage by productsViewModels.errMessage

    val normalizedSearchWords = removeDiacritics(searchText.trim())
        .lowercase()
        .split("\\s+".toRegex())

    val filteredList = products.filter { product ->
        val normalizedTitle = removeDiacritics(product.name).lowercase()
        normalizedSearchWords.all { word -> normalizedTitle.contains(word) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tìm kiếm",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Nhập tên sản phẩm...") },
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

        Spacer(modifier = Modifier.width(8.dp))

        val normalizedSearchText = removeDiacritics(searchText.trim()).lowercase()
        val suggestions = products.filter { product ->
            val normalizedName = removeDiacritics(product.name).lowercase()
            product.name.contains(searchText, ignoreCase = true) || normalizedName.contains(
                normalizedSearchText
            )
        }.take(3)

        Spacer(modifier = Modifier.height(8.dp))
        if (searchText.isNotEmpty() && suggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            ) {
                items(suggestions) { suggestion ->
                    Text(
                        text = suggestion.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { searchText = suggestion.name }
                            .padding(8.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp)
                    )
                }
            }
        } else {
            LazyColumn {
                items(filteredList) { product ->
                    ProductItem(product = product, navController = navController)
                }
            }
        }
    }
}

fun removeDiacritics(input: String): String {
    val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(normalized).replaceAll("")
}

@Composable
fun ProductItem(product: Products, navController: NavController) {
    val totalQuantity = (product.quantities ?: emptyList()).sum()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Giảm từ 8.dp xuống 4.dp
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp)) // Giảm từ 8.dp xuống 6.dp
            .background(Color.White, RoundedCornerShape(6.dp))
            .padding(8.dp) // Giảm từ 12.dp xuống 8.dp
            .clickable {
                navController.navigate("productScreen/${product.id}/${product.isFavByCurrentUser}")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.image,
            contentDescription = product.name,
            modifier = Modifier
                .size(40.dp) // Giảm từ 50.dp xuống 40.dp
                .border(0.2.dp, Color.Black, RoundedCornerShape(6.dp)) // Giảm từ 8.dp xuống 6.dp
                .padding(1.dp), // Giảm từ 2.dp xuống 1.dp
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp)) // Giảm từ 12.dp xuống 8.dp

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp // Giảm từ 16.sp xuống 14.sp
            )
            Text(
                text = "SL: $totalQuantity",
                fontSize = 12.sp, // Giảm từ 14.sp xuống 12.sp
                color = Color.Gray
            )
            Text(
                text = "Giá: ${formatCurrency1(product.price)}",
                color = Color.Gray,
                fontSize = 12.sp, // Giảm từ 14.sp xuống 12.sp
                modifier = Modifier.padding(horizontal = 4.dp) // Giảm từ 8.dp xuống 4.dp
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Go",
            modifier = Modifier
                .clickable {
                    navController.navigate("productScreen/${product.id}")
                }
                .size(24.dp) // Giảm từ 30.dp xuống 24.dp
        )
    }
}

fun formatCurrency1(price: Any?): String {
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    return when (price) {
        is Number -> formatter.format(price.toLong()) + " ₫"
        is String -> price.toLongOrNull()?.let { formatter.format(it) + " ₫" } ?: "0 ₫"
        else -> "0 đ"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSearchScreen() {
    val navController = rememberNavController()
    SearchScreen(navController)
}
