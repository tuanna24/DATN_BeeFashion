package fpl.md19.beefashion.screens.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import fpl.md19.beefashion.R
import fpl.md19.beefashion.models.Products
import fpl.md19.beefashion.viewModels.AuthState
import fpl.md19.beefashion.viewModels.AuthViewModel
import fpl.md19.beefashion.viewModels.CategoriesViewModels
import fpl.md19.beefashion.viewModels.ProductsViewModels
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    productsViewModels: ProductsViewModels = viewModel(),
    categoriesViewModels: CategoriesViewModels = viewModel()
) {
    val categories = listOf("Toàn bộ", "Áo Thun", "Áo sơ mi", "Áo Khoác", "Áo Vest")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    val products by productsViewModels.products
    val loading by productsViewModels.loading
    val errorMessage by productsViewModels.errMessage

    Column(
        modifier = Modifier
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "BeeFashion", fontSize = 28.sp, fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = "",
                onValueChange = {},
                placeholder = { Text("Tìm kiếm áo nam...", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .border(0.5.dp, Color.Black, RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    disabledContainerColor = Color(0xFFF5F5F5),
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )
                })

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { /* Thêm xử lý tìm kiếm */ },
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.seach),
                    contentDescription = "Filter",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) Color.Black else Color.White)
                    .border(0.5.dp, Color.Black, RoundedCornerShape(10.dp))
                    .clickable { selectedCategory = category }
                    .padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        when {
            loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            errorMessage != null -> Text(
                text = "Lỗi: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            else -> ProductList(products, navController)
        }
    }
}

@Composable
fun ProductList(products: List<Products>, navController: NavController) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product, navController = navController)
        }
    }
}

@Composable
fun ProductCard(
    product: Products,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val totalQuantity = (product.quantities ?: emptyList()).sum()

    Column(
        modifier = modifier
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("productScreen/${product.id}")
            }
    ) {
        Box {
            AsyncImage(
                model = product.image,
                contentDescription = "Product",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            )
            Icon(
                painter = painterResource(id = R.drawable.heart),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(4.dp))
                    .clickable { }
                    .padding(4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatCurrency(product.price),
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = "SL: $totalQuantity",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

fun formatCurrency(price: Any?): String {
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    return when (price) {
        is Number -> formatter.format(price.toLong()) + " ₫"
        is String -> price.toLongOrNull()?.let { formatter.format(it) + " ₫" } ?: "0 ₫"
        else -> "0 đ"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}
