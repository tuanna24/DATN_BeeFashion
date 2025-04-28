package fpl.md19.beefashion.screens.tab

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import fpl.md19.beefashion.screens.adress.NotifiSharePre
import fpl.md19.beefashion.screens.adress.NotificationStatus.createNotificationChannel
import fpl.md19.beefashion.screens.adress.NotificationStatus.sendOrderStatusNotification
import fpl.md19.beefashion.screens.adress.NotificationStatus.sendOrderStatusNotification1
import fpl.md19.beefashion.viewModels.CategoriesViewModels
import fpl.md19.beefashion.viewModels.FavoriteViewModel
import fpl.md19.beefashion.viewModels.InvoiceViewModel
import fpl.md19.beefashion.viewModels.ProductsViewModels
import java.text.Normalizer
import java.text.NumberFormat
import java.time.OffsetDateTime
import java.util.Locale
import java.util.regex.Pattern

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    productsViewModels: ProductsViewModels = viewModel(),
    categoriesViewModels: CategoriesViewModels = viewModel(),
    invoiceViewModel: InvoiceViewModel = viewModel(),
    viewModel: NotifiSharePre = viewModel()
) {
    val favoriteViewModel: FavoriteViewModel = viewModel()

    val products by productsViewModels.products
    val loading by productsViewModels.loading
    val errorMessage by productsViewModels.errMessage

    // Thêm state cho tìm kiếm
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }

    val categoryList by categoriesViewModels.categories // Lấy danh sách danh mục từ ViewModel

    // Tạo ánh xạ categoryId -> categoryName
    val categoryMap = categoryList.associate { it.id to it.name }

    // Lấy danh mục từ sản phẩm và ánh xạ sang tên
    val uniqueCategories = products.mapNotNull { categoryMap[it.categoryId] }.distinct()
    val categories = listOf("Toàn bộ") + uniqueCategories

    var selectedCategory by remember { mutableStateOf("Toàn bộ") }

    // Lọc sản phẩm theo danh mục đã chọn và từ khóa tìm kiếm
    val normalizedSearchWords = if (searchText.isNotEmpty()) {
        removeDiacritics(searchText.trim())
            .lowercase()
            .split("\\s+".toRegex())
    } else {
        emptyList()
    }

    val filteredProducts = if (searchText.isEmpty()) {
        if (selectedCategory == "Toàn bộ") {
            products
        } else {
            products.filter { categoryMap[it.categoryId] == selectedCategory }
        }
    } else {
        products.filter { product ->
            val normalizedTitle = removeDiacritics(product.name).lowercase()
            val matchesSearch = normalizedSearchWords.all { word -> normalizedTitle.contains(word) }
            matchesSearch && (selectedCategory == "Toàn bộ" || categoryMap[product.categoryId] == selectedCategory)
        }
    }

    // Tìm kiếm gợi ý
    val normalizedSearchText = removeDiacritics(searchText.trim()).lowercase()
    val suggestions = if (searchText.isNotEmpty()) {
        products.filter { product ->
            val normalizedName = removeDiacritics(product.name).lowercase()
            product.name.contains(searchText, ignoreCase = true) || normalizedName.contains(normalizedSearchText)
        }.take(3)
    } else {
        emptyList()
    }

    val myOrders by invoiceViewModel.invoices.observeAsState(emptyList())
    val sortedOrders = myOrders.sortedByDescending { order ->
        try {
            OffsetDateTime.parse(order.createdAt)
        } catch (e: Exception) {
            OffsetDateTime.MIN
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        invoiceViewModel.getCustomerInvoices()
    }

    // Lưu trạng thái của đơn hàng trước đó để kiểm tra thay đổi
    var lastNotifiedOrderId by remember { mutableStateOf<String?>(null) }
    var lastNotifiedOrderStatus by remember { mutableStateOf<String?>(null) }

    // Kiểm tra và gửi thông báo khi đơn hàng mới nhất thay đổi
    LaunchedEffect(sortedOrders) {
        val latestOrder = sortedOrders.firstOrNull() ?: return@LaunchedEffect
        val orderId = latestOrder.id?.take(8) ?: return@LaunchedEffect

        // Kiểm tra nếu trạng thái đơn hàng đã thay đổi và cần thông báo không
        if (lastNotifiedOrderId != orderId || lastNotifiedOrderStatus != latestOrder.status) {
            if (viewModel.shouldNotify(orderId, latestOrder.status, context)) {
                createNotificationChannel(context)
                sendOrderStatusNotification1(context, orderId, latestOrder.status ?: "")

                // Lưu lại trạng thái của đơn hàng đã thông báo
                lastNotifiedOrderId = orderId
                lastNotifiedOrderStatus = latestOrder.status

                Toast.makeText(context, "Cập nhật trạng thái đơn hàng: ${latestOrder.status}", Toast.LENGTH_SHORT).show()
            }
        }
    }
//    LaunchedEffect(myOrders) {
//        Log.d("HomeScreen", "Invoices updated: ${myOrders.size}")
//        val latestOrder = myOrders.firstOrNull() ?: return@LaunchedEffect
//        val orderId = latestOrder.id?.take(8) ?: return@LaunchedEffect
//        if (viewModel.shouldNotify(orderId, latestOrder.status, context)) {
//            createNotificationChannel(context)
//            sendOrderStatusNotification(context, orderId, latestOrder.status ?: "")
//
//            Toast.makeText(context, "Thông báo mới: ${latestOrder.status}", Toast.LENGTH_SHORT).show()
//        }
//    }

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
                value = searchText,
                onValueChange = {
                    searchText = it
                    isSearchActive = it.isNotEmpty()
                },
                placeholder = { Text("Tìm kiếm áo nam...", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(0.5.dp, Color.Black, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    disabledContainerColor = Color(0xFFF5F5F5),
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.Gray,
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
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.search), // Thay bằng icon clear thích hợp
                            contentDescription = "Clear",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { searchText = "" }
                        )
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.width(6.dp))

            Button(
                onClick = { showBottomSheet = true },
                modifier = Modifier.size(48.dp),
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

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tìm kiếm theo giá",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showBottomSheet = false }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    OutlinedTextField(
                        value = minPrice,
                        onValueChange = { minPrice = it.filter { char -> char.isDigit() } },
                        label = { Text("Giá tối thiểu") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        supportingText = {
                            if (minPrice.isNotEmpty() && (minPrice.toLongOrNull() ?: 0) < 1000) {
                                Text(
                                    text = "Giá tối thiểu phải từ 1.000 trở lên",
                                    color = Color.Red,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = maxPrice,
                        onValueChange = { maxPrice = it.filter { char -> char.isDigit() } },
                        label = { Text("Giá tối đa") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        supportingText = {
                            if (maxPrice.isNotEmpty() && (maxPrice.toLongOrNull() ?: 0) < 1000) {
                                Text(
                                    text = "Giá tối đa phải từ 1.000 trở lên",
                                    color = Color.Red,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Kiểm tra điều kiện để enable/disable nút Áp dụng
                    val min = minPrice.toFloatOrNull() ?: 0f
                    val max = maxPrice.toFloatOrNull() ?: Float.MAX_VALUE
                    val isApplyEnabled = (minPrice.isEmpty() || min >= 1000) &&
                            (maxPrice.isEmpty() || max >= 1000)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                productsViewModels.resetProducts()
                                productsViewModels.filterProductsByPrice(min..max)
                                showBottomSheet = false
                            },
                            enabled = isApplyEnabled,
                            colors = ButtonDefaults.buttonColors(Color.Red),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Áp dụng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))


                        Button(
                            onClick = {
                                productsViewModels.resetProducts()
                                minPrice = ""
                                maxPrice = ""
                                showBottomSheet = false
                            },
                            colors = ButtonDefaults.buttonColors(Color.Black),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Đặt lại",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }


        // Hiển thị gợi ý tìm kiếm
        if (isSearchActive && suggestions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(suggestions) { suggestion ->
                        Text(
                            text = suggestion.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    searchText = suggestion.name
                                    isSearchActive = false
                                }
                                .padding(12.dp),
                            fontSize = 14.sp
                        )
                        if (suggestions.indexOf(suggestion) < suggestions.size - 1) {
                            Divider(color = Color.LightGray, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Danh mục
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
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
            filteredProducts.isEmpty() -> {
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
            }
            else -> ProductList(filteredProducts, favoriteViewModel = favoriteViewModel, navController)
        }
    }
}

// Các hàm khác giữ nguyên
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
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        if (totalQuantity == 0) {
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
                    text = "Hết hàng",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        } else {
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
}

fun formatCurrency(price: Any?): String {
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    return when (price) {
        is Number -> formatter.format(price.toLong()) + " ₫"
        is String -> price.toLongOrNull()?.let { formatter.format(it) + " ₫" } ?: "0 ₫"
        else -> "0 ₫"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}