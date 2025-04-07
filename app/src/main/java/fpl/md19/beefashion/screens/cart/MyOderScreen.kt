package fpl.md19.beefashion.screens.cart

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import fpl.md19.beefashion.models.MyOder
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import fpl.md19.beefashion.screens.adress.AddressPreferenceManager
import fpl.md19.beefashion.viewModels.AddressViewModel

enum class OrderStatus(val label: String) {
    WAITING_CONFIRM("Đang chờ xác nhận"),

    //    CONFIRMED("Đã xác nhận đơn hàng"),
//    PICKED_UP("Đã lấy hàng"),
    SHIPPING("Đang vận chuyển"),
    DELIVERED("Đã giao hàng");

    companion object {
        fun fromName(name: String): OrderStatus {
            return values().firstOrNull { it.name == name } ?: WAITING_CONFIRM
        }
    }
}

fun getButtonColorByStatus(status: OrderStatus): Color {
    return when (status) {
        OrderStatus.WAITING_CONFIRM -> Color.Red
//        OrderStatus.CONFIRMED,
//        OrderStatus.PICKED_UP -> Color.Red

        OrderStatus.SHIPPING -> Color(0xFF4CAF50) // Xanh lá

        OrderStatus.DELIVERED -> Color.Gray
    }
}

@Composable
fun MyOderScreen(
    navController: NavController,
    addressViewModel: AddressViewModel
) {

    val myOderList: List<MyOder> = listOf(
        MyOder("Áo ngắn", "Size M", "189000", R.drawable.ao_phong, OrderStatus.SHIPPING),
        MyOder("Áo ngắn", "Size M", "189000", R.drawable.ao_phong, OrderStatus.DELIVERED),
        MyOder("Áo ngắn", "Size M", "189000", R.drawable.ao_phong, OrderStatus.WAITING_CONFIRM),
        MyOder("Áo ngắn", "Size M", "189000", R.drawable.ao_phong, OrderStatus.WAITING_CONFIRM),
        MyOder("Áo ngắn", "Size M", "189000", R.drawable.ao_phong, OrderStatus.SHIPPING),
        MyOder("Áo ngắn", "Size M", "189000", R.drawable.ao_phong, OrderStatus.DELIVERED),
        MyOder("Áo ngắn", "Size M", "189000", R.drawable.ao_phong, OrderStatus.SHIPPING),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.Top
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
                    .size(20.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Đơn hàng",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(myOderList) { myOder ->
                MyOderCart(myOder, navController, addressViewModel)
            }
        }
    }
}

@Composable
fun MyOderCart(
    myOder: MyOder,
    navController: NavController,
    addressViewModel: AddressViewModel
) {
    val context = LocalContext.current
    val addresses by addressViewModel.addresses.collectAsState()
    val addressPreferenceManager = remember { AddressPreferenceManager(context) }
    // đọc giá trị từ pre
    var selectedAddress by remember { mutableStateOf(addressPreferenceManager.getSelectedAddress()) }
    val selectedAddressModel = addresses.find { it.id == selectedAddress }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
//                val route = "trackOrderScreen/${myOder.status.name}"
//                // navController.navigate("trackOrderScreen")
//
//                navController.navigate(route)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = myOder.imageRes),
                contentDescription = myOder.title,
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = myOder.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = myOder.size,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${myOder.price} VND",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val buttonColor = getButtonColorByStatus(myOder.status)
                Button(
                    onClick = {
                        if (selectedAddressModel != null) {
                            val encodedAddress = Uri.encode(
                                "${selectedAddressModel.name}, ${selectedAddressModel.phoneNumber}\n" +
                                        "${selectedAddressModel.detail}, ${selectedAddressModel.ward}, " +
                                        "${selectedAddressModel.district}, ${selectedAddressModel.province}"
                            )
                            navController.navigate("trackOrderScreen/${myOder.status.name}?address=$encodedAddress")
                        } else {
                            Toast.makeText(
                                context,
                                "Không có địa chỉ được chọn",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = myOder.status.label,
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,  // Chỉ hiển thị 1 dòng
                        modifier = Modifier.padding(horizontal = 12.dp) // tránh văn bản bị quá sát biên
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMyOderScreen() {
    val navController = rememberNavController()
    MyOderScreen(navController, addressViewModel = AddressViewModel())
}