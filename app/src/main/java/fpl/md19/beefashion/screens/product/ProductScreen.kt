package fpl.md19.beefashion.screens.product

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fpl.md19.beefashion.R
import fpl.md19.beefashion.components.BuyNowBottomSheet
import fpl.md19.beefashion.viewModels.BrandViewModel
import fpl.md19.beefashion.viewModels.ProductsViewModels
import fpl.md19.beefashion.viewModels.SizesViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductScreen(
    productId: String,
    navController: NavController,
    viewModel: ProductDetailViewModel = viewModel(),
    brandsViewModel: BrandViewModel = viewModel(),
    productsViewModels: ProductsViewModels = viewModel()
) {
    var selectedSize by remember { mutableStateOf("") }
    var selectedQuantity by remember { mutableStateOf(0) }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        viewModel.fetchProductDetails(productId)
    }

    val product by viewModel.productDetail.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    val brands by brandsViewModel.brand
    val products by productsViewModels.products
    val sizes = product?.sizes ?: listOf()
    val quantities = product?.quantities ?: listOf()

    val context = LocalContext.current

    LaunchedEffect(product) {
        if (sizes.isNotEmpty() && quantities.isNotEmpty()) {
            selectedSize = sizes.first().name
            selectedQuantity = quantities.first()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, top = 30.dp, end = 25.dp)
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
                text = "Chi tiết sản phẩm",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
        } else if (product != null) {
            val productBrand = brands.find { it.id == product!!.brandId }
            val selectedProduct = products.find { it.id == productId }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            ) {
                AsyncImage(
                    model = selectedProduct?.image,
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(42.dp)
                        .border(0.1.dp, Color.Black, RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .clickable { isFavorite = !isFavorite },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                item {
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedProduct?.name ?: "",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "(${productBrand?.name ?: "Không có thương hiệu"})",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = " 4.0/5 ",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "(45 đánh giá)",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        text = "${product?.description}",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
//                Text(
//                    text = "Chọn kích thước",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                if (sizes.isEmpty()) {
//                    Text(
//                        text = "Không có size của áo này",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Red
//                    )
//                } else {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(20.dp)
//                    ) {
//                        sizes.forEachIndexed { index, sizeObj ->
//                            val quantity = quantities.getOrNull(index) ?: 0
//                            Box(
//                                modifier = Modifier
//                                    .size(60.dp)
//                                    .border(
//                                        2.dp,
//                                        if (sizeObj.name == selectedSize) Color.Black else Color.Gray,
//                                        RoundedCornerShape(8.dp)
//                                    )
//                                    .clickable {
//                                        selectedSize = sizeObj.name
//                                        selectedQuantity = quantity
//                                    }
//                                    .padding(8.dp),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                    Text(
//                                        text = sizeObj.name.trim(),
//                                        fontSize = 16.sp,
//                                        fontWeight = FontWeight.Bold
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text(
//                    text = "Số lượng: $selectedQuantity",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold
//                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.padding(end = 25.dp)
                    ) {
                        Text(
                            text = "Giá",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = formatCurrency(product?.price),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    val context = LocalContext.current
                    var isAddedToCart by remember { mutableStateOf(false) }

                    var showBottomSheet by remember { mutableStateOf(false) }

                    IconButton(
                        onClick = {
                            if (isAddedToCart) {
                                Toast.makeText(context, "Sản phẩm đã có trong giỏ hàng!", Toast.LENGTH_SHORT).show()
                            } else {
                                isAddedToCart = true
                                Toast.makeText(context, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)) // Viền cho đẹp
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_to_cart), // Thay bằng icon giỏ hàng của bạn
                            contentDescription = "Thêm vào giỏ hàng",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
//                    Button(
//                        onClick = { showBottomSheet = true }, // ✅ Mở Bottom Sheet
//                        shape = RoundedCornerShape(8.dp),
//                        colors = ButtonDefaults.buttonColors(Color.Red),
//                        modifier = Modifier
//                            .weight(1f)
//                            .height(60.dp)
//                    ) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text(
//                                text = "Mua ngay",
//                                color = Color.White,
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Spacer(modifier = Modifier.height(2.dp))
//                            Text(
//                                text = formatCurrency(product?.price),
//                                color = Color.White,
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Medium
//                            )
//                        }
//                    }

                    Button(
                        onClick = {
                            showBottomSheet = true
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Mua ngay",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val productDetailViewModel: ProductDetailViewModel = viewModel()
                    if (showBottomSheet) {
                        BuyNowBottomSheet(
                            productsViewModels = productsViewModels,
                            viewModel = productDetailViewModel,
                            productId = productId,// ✅ Truyền đúng instance
                            onDismiss = { showBottomSheet = false },
                            navController
                        )
                    }
                }
            }
        }
    }
}

fun formatCurrency(price: Any?): String {
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    return when (price) {
        is Number -> formatter.format(price.toLong()) + " ₫" // Định dạng số
        is String -> price.toLongOrNull()?.let { formatter.format(it) + " ₫" } ?: "0 ₫"
        else -> "0 đ"
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewProductScreen() {
//    val navController = rememberNavController()
//    val productId = "12345" // Giá trị giả định cho Preview
//    ProductScreen(navController, productId)
//}

