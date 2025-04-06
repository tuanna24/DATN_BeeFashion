package fpl.md19.beefashion.screens.tab

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import fpl.md19.beefashion.models.AddressModel
import fpl.md19.beefashion.models.CartItem
import fpl.md19.beefashion.models.CartItemSentData
import fpl.md19.beefashion.screens.adress.AddressPreferenceManager
import fpl.md19.beefashion.viewModels.AddressViewModel
import fpl.md19.beefashion.viewModels.CartViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun CartScreen(
    navController: NavController,
    addressViewModel: AddressViewModel
) {
    val vatPercent = 10  // VAT 10%
    val shippingFee = 30000

    val context = LocalContext.current

    val cartViewModel: CartViewModel = viewModel()
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val cartItem by cartViewModel.cartItem.observeAsState()

    val selectedItems = remember { mutableStateListOf<CartItem>() }

    val addresses by addressViewModel.addresses.collectAsState()
    val addressPreferenceManager = remember { AddressPreferenceManager(context) }
    // đọc giá trị từ pre
    var selectedAddress by remember { mutableStateOf(addressPreferenceManager.getSelectedAddress()) }
    val selectedAddressModel = addresses.find { it.id == selectedAddress }
    //selectedAddressModel?.let {
//    val encodedAddress =
//        Uri.encode("${it.name}, ${it.phoneNumber}\n${it.detail}, ${it.ward}, ${it.district}, ${it.province}")

    LaunchedEffect(cartItems) {
        println(cartItems)
        if (cartItem != null) {
//            println(cartItem)
            val newCartItem =
                cartItems.find { it.productId == cartItem!!.productId && it.sizeID == cartItem!!.sizeID }
//            println(newCartItem)
            selectedItems.replaceAll {
                if (it.sizeID == cartItem!!.sizeID && it.productId == cartItem!!.productId && newCartItem != null) {
                    newCartItem
                } else {
                    it
                }
            }
        }
    }

    val subTotal = selectedItems.sumOf { it.product.price * it.quantity }
    val vatAmount = subTotal * vatPercent / 100
    val total = subTotal + vatAmount

    LaunchedEffect(Unit) {
        cartViewModel.getCartItems()
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
                text = "Giỏ hàng",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            // Hiển thị khi giỏ hàng trống
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
                        text = "Không có sản phẩm trong giỏ hàng",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hãy thêm một sản phẩm vào giỏ hàng trước khi thanh toán!",
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
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemView(
                        item,
                        onIncrease = {
                            cartViewModel.updateCartItems(
                                CartItemSentData(
                                    item.sizeID,
                                    item.productId,
                                    1
                                )
                            )
                        },
                        onDecrease = {
                            cartViewModel.updateCartItems(
                                CartItemSentData(
                                    item.sizeID,
                                    item.productId,
                                    -1
                                )
                            )
                        },
                        isSelected = selectedItems.contains(item),
                        onCheckChanged = {
                            if (selectedItems.contains(item)) {
                                selectedItems.remove(item)
                            } else {
                                selectedItems.add(item)
                            }
                        },
                        onDelete = {
                            cartViewModel.removeProductFromCart(item.productId, item.sizeID)
                            Toast.makeText(
                                context,
                                "Đã xóa ${item.product.name} size ${item.size.name} khỏi giỏ hàng!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryRow("Tạm tính", subTotal)
                SummaryRow("VAT ($vatPercent%)", vatAmount)
                Divider()
                SummaryRow("Tổng cộng", total, isBold = true)
                Button(
                    onClick = {
                        if (selectedAddressModel != null) {
                            val encodedAddress = Uri.encode(
                                "${selectedAddressModel.name}, ${selectedAddressModel.phoneNumber}\n" +
                                        "${selectedAddressModel.detail}, ${selectedAddressModel.ward}, " +
                                        "${selectedAddressModel.district}, ${selectedAddressModel.province}"
                            )
                            navController.navigate("paymentScreen/$encodedAddress"){
                                popUpTo("addressScreen") {
                                    inclusive = true
                                }
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "Vui lòng chọn địa chỉ giao hàng!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        if (selectedItems.isNotEmpty()) Color(
                            0xFFFF5722
                        ) else Color.Gray
                    ),
                    enabled = selectedItems.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "Thanh toán ngay", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
    //}
}

@Composable
fun CartItemView(
    item: CartItem,
    isSelected: Boolean,
    onCheckChanged: (Boolean) -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(item.quantity) }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onCheckChanged,
                modifier = Modifier.padding(end = 8.dp)
            )
            AsyncImage(
                model = item.product.image,
                contentDescription = "product image",
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "Size: ${item.size.name}", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatCurrency(item.product.price * quantity),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Xóa",
                        tint = Color.Red
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            if (quantity > 1) {
                                onDecrease()
                                quantity--
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.tru),
                            contentDescription = "Giảm"
                        )
                    }
                    Text(text = quantity.toString(), fontSize = 14.sp)
                    IconButton(
                        onClick = {
                            onIncrease()
                            quantity++
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cong),
                            contentDescription = "Tăng"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, amount: Int, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight =
            if (isBold)
                FontWeight.Bold
            else
                FontWeight.Normal
        )
        Text(
            text = formatCurrency(amount),
            fontSize = 16.sp,
            fontWeight =
            if (isBold)
                FontWeight.Bold
            else
                FontWeight.Normal
        )
    }
}

fun formatCurrency(amount: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return format.format(amount)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCartScreen() {
    val navController = rememberNavController()
    val mockAddresses = listOf(
        AddressModel(
            id = "1",
            province = "TP.HCM",
            district = "Quận 1",
            ward = "Phường Bến Nghé",
            detail = "123 Lê Lợi",
            name = "Nguyễn Văn A",
            phoneNumber = "0901234567"
        )
    )
    val selectedAddress = mockAddresses.first().id
    CartScreen(
        navController = navController,
        addressViewModel = AddressViewModel()
    )
}

