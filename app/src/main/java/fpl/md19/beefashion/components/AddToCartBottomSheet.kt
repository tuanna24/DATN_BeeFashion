package fpl.md19.beefashion.components

import ProductDetailViewModel
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

import coil.compose.AsyncImage
import fpl.md19.beefashion.models.CartItemSentData
import fpl.md19.beefashion.screens.payment.formatCurrency
import fpl.md19.beefashion.viewModels.CartViewModel
import fpl.md19.beefashion.viewModels.ProductsViewModels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToCartBottomSheet (
    productsViewModels: ProductsViewModels,
    productDetailViewModel: ProductDetailViewModel,
    productId: String,
    onDismiss: () -> Unit,
    navController: NavController
) {

    val cartViewModel: CartViewModel = viewModel()
    val product by productDetailViewModel.productDetail.observeAsState()
    val errorMessage by productDetailViewModel.errorMessage.observeAsState()
    LaunchedEffect(productId) {
        productDetailViewModel.fetchProductDetails(productId)
    }

    val context = LocalContext.current

    val products by productsViewModels.products
    val sizes = product?.sizes ?: listOf()
    val quantities = product?.quantities ?: listOf()
    var stock = (product?.quantities ?: emptyList()).sum()

    var selectedSize by remember { mutableStateOf("") }
    var selectedQuantity by remember { mutableIntStateOf(1) }

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
                        text = "Kho: $stock",
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
                                    selectedSize = sizeObj.id
                                    stock = quantity
                                    selectedQuantity = 1
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
                        onClick = { if (selectedQuantity > 1) selectedQuantity-- },
                        enabled = selectedSize.isNotEmpty(),
                        modifier = Modifier
                            .size(36.dp) // Tăng kích thước nút
                            .border(1.dp, if(selectedSize.isEmpty()) Color.LightGray else Color.Black, RoundedCornerShape(8.dp)) // ✅ Thêm viền
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Giảm",
                            tint = if(selectedSize.isEmpty()) Color.LightGray else Color.Black // Màu icon
                        )
                    }

                    Text(
                        text = selectedQuantity.toString(),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = { if(selectedQuantity < stock) selectedQuantity++ },
                        enabled = selectedSize.isNotEmpty(),
                        modifier = Modifier
                            .size(36.dp) // Tăng kích thước nút
                            .border(1.dp, if(selectedSize.isEmpty()) Color.LightGray else Color.Black, RoundedCornerShape(8.dp)) // ✅ Thêm viền
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tăng",
                            tint = if(selectedSize.isEmpty()) Color.LightGray else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    cartViewModel.addProductToCart(CartItemSentData(selectedSize, productId, selectedQuantity))
                    Toast.makeText(context, "Thêm sản phẩm ${selectedProduct?.name ?: ""} vào giỏ hàng thành công!",
                        Toast.LENGTH_SHORT).show()
                },
                enabled = selectedSize.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(
                    text = "Thêm vào giỏ hàng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}