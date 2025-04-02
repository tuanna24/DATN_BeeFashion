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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import fpl.md19.beefashion.R
import fpl.md19.beefashion.models.Products
import fpl.md19.beefashion.viewModels.CategoriesViewModels
import fpl.md19.beefashion.viewModels.FavoriteViewModel
import fpl.md19.beefashion.viewModels.ProductsViewModels
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    productsViewModels: ProductsViewModels = viewModel(),
    categoriesViewModels: CategoriesViewModels = viewModel()
) {

    val favoriteViewModel: FavoriteViewModel = viewModel()

    val products by productsViewModels.products
    val loading by productsViewModels.loading
    val errorMessage by productsViewModels.errMessage


    val categoryList by categoriesViewModels.categories // Lấy danh sách danh mục từ ViewModel

    // Tạo ánh xạ categoryId -> categoryName
    val categoryMap = categoryList.associate { it.id to it.name }

    // Lấy danh mục từ sản phẩm và ánh xạ sang tên
    val uniqueCategories = products.mapNotNull { categoryMap[it.categoryId] }.distinct()
    val categories = listOf("Toàn bộ") + uniqueCategories

    var selectedCategory by remember { mutableStateOf("Toàn bộ") }

    // Lọc sản phẩm theo danh mục đã chọn
    val filteredProducts = if (selectedCategory == "Toàn bộ") {
        products
    } else {
        products.filter { categoryMap[it.categoryId] == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Topbar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BeeFashion",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Thanh tìm kiếm
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Tìm kiếm áo nam...", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp) // Tăng nhẹ chiều cao để đảm bảo hiển thị
                    .border(0.5.dp, Color.Black, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    disabledContainerColor = Color(0xFFF5F5F5),
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.Gray, // Đảm bảo placeholder hiển thị
                    unfocusedPlaceholderColor = Color.Gray
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true // Đảm bảo nội dung không bị cắt
            )

            Spacer(modifier = Modifier.width(6.dp))

            Button(
                onClick = { /* Thêm xử lý tìm kiếm */ },
                modifier = Modifier.size(48.dp), // Đồng bộ chiều cao với TextField
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color.Black),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.seach),
                    contentDescription = "Filter",
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Danh mục
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFFFF5722) else Color.White)
                        .border(0.5.dp, Color.Black, RoundedCornerShape(8.dp))
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Danh sách sản phẩm
        when {
            loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            errorMessage != null -> Text(
                text = "Lỗi: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            else -> ProductList(filteredProducts, favoriteViewModel = favoriteViewModel, navController)
        }
    }
}

@Composable
fun ProductList(products: List<Products>, favoriteViewModel: FavoriteViewModel, navController: NavController) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.padding(6.dp),
        verticalItemSpacing = 6.dp,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product, favoriteViewModel = favoriteViewModel, navController = navController)
        }
    }
}

@Composable
fun ProductCard(
    product: Products,
    favoriteViewModel: FavoriteViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val totalQuantity = (product.quantities ?: emptyList()).sum()
    var isFavorite: Boolean by remember { mutableStateOf(product.isFavByCurrentUser) }

    Column(
        modifier = modifier
            .padding(4.dp)
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .clickable { navController.navigate("productScreen/${product.id}") }
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("productScreen/${product.id}/${product.isFavByCurrentUser}")
            }
    ) {
        Box {
            AsyncImage(
                model = product.image,
                contentDescription = "Product",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
//            Icon(
//                painter = painterResource(id = R.drawable.heart),
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(4.dp)
//                    .size(20.dp)
//                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(4.dp))
//                    .clickable { }
//                    .padding(2.dp)
//            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = product.name,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 6.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatCurrency(product.price),
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            Text(
                text = "SL: $totalQuantity",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }
    }
}

fun formatCurrency(price: Any?): String {
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    return when (price) {
        is Number -> formatter.format(price.toLong()) + " ₫"
        is String -> price.toLongOrNull()?.let { formatter.format(it) + " ₫" } ?: "0 ₫"
        else -> "0 ₫"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}