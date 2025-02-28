package fpl.md19.beefashion.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

import coil.compose.AsyncImage
import fpl.md19.beefashion.screens.product.formatCurrency
import fpl.md19.beefashion.viewModels.ProductsViewModels


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyNowBottomSheet(
    productsViewModels: ProductsViewModels = viewModel(),
    viewModel: ProductDetailViewModel = viewModel(),
    productId: String,
    onDismiss: () -> Unit,
    navController: NavController
) {
    val product by viewModel.productDetail.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()
    LaunchedEffect(productId) {
        viewModel.fetchProductDetails(productId)
    }


    val products by productsViewModels.products
    val sizes = product?.sizes ?: listOf()
    val quantities = product?.quantities ?: listOf()
    val totalQuantity = (product?.quantities ?: emptyList()).sum()

    var selectedSize by remember { mutableStateOf("") }
    var selectedQuantity by remember { mutableStateOf(0) }
    var isFavorite by remember { mutableStateOf(false) }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val selectedProduct = products.find { it.id == productId }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = selectedProduct?.image,
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp)
                )
                Column(
                    modifier = Modifier.weight(1f).padding(start = 12.dp)
                ) {
                    Text(
                        text = selectedProduct?.name ?: "",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatCurrency(product?.price),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(
                        text = "Kho: $totalQuantity",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onDismiss() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Chọn màu sắc
            Text(
                text = "Màu Sắc",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                product?.color?.forEach { color ->
                    Button(
                        onClick = { /* Xử lý chọn màu */ },
                        colors = ButtonDefaults.buttonColors(Color.LightGray),
                        modifier = Modifier.weight(1f)
                    ) {
//                        Text(text = color)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Chọn Size
            Text(
                text = "Size",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            if (sizes.isEmpty()) {
                Text(
                    text = "Không có size của áo này",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    sizes.forEachIndexed { index, sizeObj ->
                        val quantity = quantities.getOrNull(index) ?: 0
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .border(
                                    2.dp,
                                    if (sizeObj.name == selectedSize) Color.Black else Color.Gray,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedSize = sizeObj.name
                                    selectedQuantity = quantity
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = sizeObj.name.trim(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

//            Text(
//                text = "Số lượng: $selectedQuantity",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold
//            )

            Spacer(modifier = Modifier.height(16.dp))


            var quantity by remember { mutableStateOf(1) } // ✅ Trạng thái số lượng

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Thêm khoảng cách
                horizontalArrangement = Arrangement.SpaceBetween, // Cách đều các phần tử
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ✅ Chọn số lượng
                Text(
                    text = "Số lượng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier
                            .size(36.dp) // Tăng kích thước nút
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // ✅ Thêm viền
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Giảm",
                            tint = Color.Black // Màu icon
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier
                            .size(36.dp) // Tăng kích thước nút
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // ✅ Thêm viền
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tăng",
                            tint = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("paymentScreen")
                },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(
                    text = "Mua ngay",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
