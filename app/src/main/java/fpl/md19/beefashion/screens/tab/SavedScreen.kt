package fpl.md19.beefashion.screens.tab

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import fpl.md19.beefashion.models.Products
import fpl.md19.beefashion.models.Saved
import fpl.md19.beefashion.screens.adress.NotifiSharePre
import fpl.md19.beefashion.screens.adress.NotificationStatus.createNotificationChannel
import fpl.md19.beefashion.screens.adress.NotificationStatus.sendOrderStatusNotification
import fpl.md19.beefashion.screens.adress.NotificationStatus.sendOrderStatusNotification1
import fpl.md19.beefashion.viewModels.FavoriteViewModel
import fpl.md19.beefashion.viewModels.InvoiceViewModel
import java.text.NumberFormat
import java.time.OffsetDateTime
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavedScreen(
    navController: NavController,
    invoiceViewModel: InvoiceViewModel = viewModel(),
    viewModel: NotifiSharePre = viewModel()
) {

    val favoriteViewModel: FavoriteViewModel = viewModel()
    val savedList by favoriteViewModel.products.observeAsState()

    LaunchedEffect(Unit) {
        favoriteViewModel.getFavoriteProducts()
    }

    var itemToDelete by remember { mutableStateOf<Products?>(null) }

    val context = LocalContext.current
    val myOrders by invoiceViewModel.invoices.observeAsState(emptyList())
    val sortedOrders = myOrders.sortedByDescending { order ->
        try {
            OffsetDateTime.parse(order.createdAt)
        } catch (e: Exception) {
            OffsetDateTime.MIN
        }
    }
    LaunchedEffect(Unit) {
        invoiceViewModel.getCustomerInvoices()
    }

    var lastOrderStatusMap by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    LaunchedEffect(sortedOrders) {
        sortedOrders.forEach { order ->
            val orderId = order.id?.take(8) ?: return@forEach
            val currentStatus = order.status ?: return@forEach

            val previousStatus = lastOrderStatusMap[orderId]

            if (previousStatus != currentStatus) {
                if (viewModel.shouldNotify(orderId, currentStatus, context)) {
                    createNotificationChannel(context)
                    sendOrderStatusNotification1(context, orderId, currentStatus)

                    Toast.makeText(context, "Đơn $orderId cập nhật trạng thái: $currentStatus", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Cập nhật map lưu trạng thái sau khi xử lý
        lastOrderStatusMap = sortedOrders.mapNotNull { order ->
            val orderId = order.id?.take(8)
            val status = order.status
            if (orderId != null && status != null) {
                orderId to status
            } else null
        }.toMap()
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
                text = "Sản phẩm yêu thích",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        if (savedList == null || savedList!!.isEmpty()) {
            EmptySavedScreen()
        } else {
            SaveList(savedList!!, { item -> itemToDelete = item }, navController)
        }
    }

    // Hiển thị Dialog xác nhận khi xóa sản phẩm
    itemToDelete?.let { item ->
        ConfirmDeleteDialog(
            item = item,
            onConfirm = {
                favoriteViewModel.removeFavoriteProducts(item.id)
                Toast.makeText(
                    context,
                    "Bạn đã xóa sản phẩm ${item.name} khỏi danh sách yêu thích.",
                    Toast.LENGTH_SHORT
                ).show()
                itemToDelete = null
                favoriteViewModel.getFavoriteProducts()
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

@Composable
fun SaveList(saver: List<Products>, onRemoveRequest: (Products) -> Unit, navController: NavController) {
    val state = rememberLazyStaggeredGridState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = state,
    ) {
        items(saver) { savedItem ->
            MySaveCard(savedItem, onRemoveRequest, navController) // Truyền navController vào
        }
    }
}

@Composable
fun MySaveCard(saved: Products, onRemoveRequest: (Products) -> Unit, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp, top = 20.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Box {
            AsyncImage(
                model = saved.image,
                contentDescription = "Product",
                contentScale = ContentScale.Fit,
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable{navController.navigate("productScreen/${saved.id}/${true}")}
            )
            Icon(
                painter = painterResource(R.drawable.hear_red),
                contentDescription = "Remove from favorites",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .background(Color(0xFFBD0000), shape = RoundedCornerShape(4.dp))
                    .clickable { onRemoveRequest(saved) } // Hiển thị Dialog xác nhận
                    .padding(4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = saved.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatCurrency(saved.price),
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun ConfirmDeleteDialog(item: Products, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Xác nhận xóa") },
        text = { Text("Bạn có chắc muốn xóa sản phẩm \"${item.name}\" khỏi danh sách yêu thích?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@Composable
fun EmptySavedScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.heart_icon),
                contentDescription = "No results",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Không có sản phẩm yêu thích",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hãy thêm một sản phẩm yêu thích trước",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)
            )
        }
    }
}

fun formatCurrency2(price: Any?): String {
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
fun PreviewSavedScreen() {
    val navController = rememberNavController()
    SavedScreen(navController)
}
