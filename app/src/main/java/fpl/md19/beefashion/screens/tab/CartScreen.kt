package fpl.md19.beefashion.screens.tab

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import fpl.md19.beefashion.models.CartItem
import java.text.NumberFormat
import java.util.*

@Composable
fun CartScreen(navController: NavController) {
    val vatPercent = 10  // VAT 10%
    val shippingFee = 30000

    var cartItems by remember {
        mutableStateOf(
            listOf(
                CartItem(1, "Regular Fit Slogan", "Size L", 1190000, R.drawable.ao_phong, 2),
                CartItem(2, "Regular Fit Polo", "Size M", 1100000, R.drawable.ao_phong, 1),
                CartItem(3, "Regular Fit Black", "Size L", 1290000, R.drawable.ao_phong, 1),
            )
        )
    }

    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val vatAmount = subtotal * vatPercent / 100
    val total = subtotal + vatAmount + shippingFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, top = 30.dp, end = 25.dp)
    ) {
        // Thanh tiêu đề
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
                text = "Giỏ hàng",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            // Hiển thị khi giỏ hàng trống
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cart_icon),
                        contentDescription = "No results",
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Không có sản phẩm",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hãy thêm một sản phẩm trước",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                    )
                }
            }

        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemView(item,
                        onUpdate = { updatedItem ->
                            cartItems = cartItems.map {
                                if (it.id == updatedItem.id) updatedItem else it
                            }
                        },
                        onDelete = {
                            cartItems = cartItems.filter { it.id != item.id }
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
                SummaryRow("Tạm tính", subtotal)
                SummaryRow("VAT ($vatPercent%)", vatAmount)
                SummaryRow("Phí vận chuyển", shippingFee)
                Divider()
                SummaryRow("Tổng cộng", total, isBold = true)

                Button(
                    onClick = { /* Xử lý thanh toán */ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Thanh toán ngay",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemView(item: CartItem, onUpdate: (CartItem) -> Unit, onDelete: () -> Unit) {

    var context = LocalContext.current
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
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = item.name,
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = item.size,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatCurrency(item.price * item.quantity),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        onDelete()
                        Toast.makeText(context, "Bạn đã xóa sản phẩm '${item.name}' khỏi giỏ hàng!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Xóa",
                        tint = Color.Red
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = {
                        if (item.quantity > 1) onUpdate(item.copy(quantity = item.quantity - 1))
                    }) {
                        Icon(painter = painterResource(id = R.drawable.tru), contentDescription = "Giảm")
                    }
                    Text(
                        text = item.quantity.toString(),
                        fontSize = 14.sp
                    )
                    IconButton(onClick = {
                        onUpdate(item.copy(quantity = item.quantity + 1))
                    }) {
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
    CartScreen(navController)
}
