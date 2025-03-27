package fpl.md19.beefashion

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.models.AddressModel
import fpl.md19.beefashion.viewModels.AddressViewModel

@Composable
fun AddressScreen(
    navController: NavController,
    viewModel: AddressViewModel,
    customerId: String
) {
    var selectedAddress by remember { mutableStateOf("") }
    val addresses by viewModel.addresses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(customerId) {
        if (customerId.isNotBlank()) {
            viewModel.fetchAddresses(customerId)
        }
    }

    LaunchedEffect(addresses) {
        if (addresses.isNotEmpty() && selectedAddress.isBlank()) {
            selectedAddress = addresses.first().id // Giữ nguyên thứ tự, chỉ chọn mặc định lần đầu
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Địa chỉ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = R.drawable.ic_notifications),
                contentDescription = "Notifications",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Địa chỉ đã lưu", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        } else if (addresses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Không có địa chỉ nào")
            }
        } else {
            addresses.forEach { addressModel ->
                val fullAddress =
                    "${addressModel.detail}, ${addressModel.ward}, ${addressModel.district}, ${addressModel.province}"

                AddressItem(
                    address = fullAddress,
                    selected = selectedAddress == addressModel.id,
                    onSelect = { selectedAddress = addressModel.id } // Chỉ thay đổi giá trị, không thay đổi danh sách
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Thêm địa chỉ mới
        Button(
            onClick = {
                if (addresses.size >= 5) {
                    Toast.makeText(
                        context,
                        "Bạn chỉ có thể lưu tối đa 5 địa chỉ. Hãy xóa bớt để thêm mới.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    navController.navigate("NewAddressScreen/$customerId")
                }
            },
            enabled = addresses.size < 5,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (addresses.size < 5) Color.White else Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add",
                tint = if (addresses.size < 5) Color.Black else Color.DarkGray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Thêm địa chỉ mới",
                color = if (addresses.size < 5) Color.Black else Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Chọn địa chỉ
        Button(
            onClick = {
                Toast.makeText(context, "Cập nhật địa chỉ nhận hàng thành công!", Toast.LENGTH_SHORT).show()
                navController.navigate("paymentScreen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Chọn", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddressItem(
    address: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(vertical = 4.dp)
            .clickable { onSelect() }, // Chọn khi bấm vào toàn bộ item
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = address, fontSize = 12.sp, color = Color.Gray)
            }
            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Black,
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressPreview() {
    val navController = rememberNavController()
    val viewModel: AddressViewModel = viewModel()

    LaunchedEffect(Unit) { viewModel.fetchAddresses("12345") }
    AddressScreen(navController, viewModel, "12345")
}
