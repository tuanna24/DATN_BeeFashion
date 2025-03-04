package fpl.md19.beefashion

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import fpl.md19.beefashion.requests.AddressRequest
import fpl.md19.beefashion.screens.adress.MapScreen
import fpl.md19.beefashion.viewModels.AddressViewModel
import fpl.md19.beefashion.viewModels.NewAddressViewModel

@Composable
fun NewAddressScreen(
    navController: NavController,
    addressViewModel: AddressViewModel,
    newAddressViewModel: NewAddressViewModel,
    customerId: String
) {
    val isLoading by newAddressViewModel.isLoading.collectAsState()
    val error by newAddressViewModel.error.collectAsState()

    // Nếu có lỗi, hiển thị Snackbar
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            // Hiển thị thông báo lỗi (có thể dùng Snackbar)
            // Sau đó clear lỗi để không hiển thị lại
            newAddressViewModel.clearError()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp).clickable { navController.popBackStack() }
            )
            Text(text = "Địa chỉ mới", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Image(
                painter = painterResource(id = R.drawable.ic_notifications),
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp).clickable { }
            )
        }

        Box(modifier = Modifier.fillMaxHeight(0.4f)) {
            MapScreen()
        }

        // Hiển thị loading nếu cần
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        } else {
            AddressForm(
                navController = navController,
                addressViewModel = addressViewModel,
                newAddressViewModel = newAddressViewModel,
                customerId = customerId
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressForm(
    navController: NavController,
    addressViewModel: AddressViewModel,
    newAddressViewModel: NewAddressViewModel,
    customerId: String
) {
    val context = LocalContext.current

    val selectedProvince by newAddressViewModel.selectedProvince.collectAsState()
    val selectedDistrict by newAddressViewModel.selectedDistrict.collectAsState()
    val selectedWard by newAddressViewModel.selectedWard.collectAsState()
    val provinces by newAddressViewModel.provinces.collectAsState()
    val districts by newAddressViewModel.districts.collectAsState()
    val wards by newAddressViewModel.wards.collectAsState()
    val createStatus by addressViewModel.createStatus.collectAsState()

    var expandedProvince by remember { mutableStateOf(false) }
    var expandedDistrict by remember { mutableStateOf(false) }
    var expandedWard by remember { mutableStateOf(false) }
    var detail by rememberSaveable { mutableStateOf("") }

    // Xử lý trạng thái tạo địa chỉ
    LaunchedEffect(createStatus) {
        when (createStatus) {
            is AddressViewModel.CreateStatus.Success -> navController.popBackStack()
            is AddressViewModel.CreateStatus.Error -> Toast.makeText(context, "Lỗi khi tạo địa chỉ", Toast.LENGTH_SHORT).show()
            else -> {}
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        DropdownField(
            label = "Tỉnh/Thành phố",
            value = selectedProvince?.first.orEmpty(),
            expanded = expandedProvince,
            onExpandedChange = { expandedProvince = it },
            items = provinces.map { it.name to it.code.toString() },
            onItemSelected = { name, code ->
                newAddressViewModel.setProvince(name, code)
                expandedProvince = false
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownField(
            label = "Quận/Huyện",
            value = selectedDistrict?.first.orEmpty(),
            expanded = expandedDistrict,
            onExpandedChange = { expandedDistrict = it },
            items = districts.map { it.name to it.code.toString() },
            enabled = selectedProvince != null,
            onItemSelected = { name, code ->
                newAddressViewModel.setDistrict(name, code)
                expandedDistrict = false
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownField(
            label = "Phường/Xã",
            value = selectedWard.orEmpty(),
            expanded = expandedWard,
            onExpandedChange = { expandedWard = it },
            items = wards.map { it.name to it.code.toString() },
            enabled = selectedDistrict != null,
            onItemSelected = { name, _ ->
                newAddressViewModel.setWard(name)
                expandedWard = false
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = detail,
            onValueChange = { detail = it },
            label = { Text("Địa chỉ chi tiết") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        val isFormValid = selectedProvince != null && selectedDistrict != null && selectedWard != null && detail.isNotEmpty()

        Button(
            onClick = {
                if (isFormValid) {
                    val addressRequest = AddressRequest(
                        province = selectedProvince?.first.orEmpty(),
                        district = selectedDistrict?.first.orEmpty(),
                        ward = selectedWard.orEmpty(),
                        detail = detail
                    )
                    addressViewModel.createAddress(addressRequest) // Không cần truyền customerId nữa
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = isFormValid

        ) {
            Text("Thêm địa chỉ")
        }

        if (!isFormValid) {
            Text(
                text = "Vui lòng điền đầy đủ thông tin",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<Pair<String, String>>,
    enabled: Boolean = true,
    onItemSelected: (String, String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) onExpandedChange(it) }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        if (expanded && items.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                items.forEach { (name, code) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = { onItemSelected(name, code) }
                    )
                }
            }
        }
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit, navController: NavController) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    // navController.navigate("AddressScreen")
                    Toast.makeText(context, "Bạn đã thêm một địa chỉ mới", Toast.LENGTH_SHORT)
                        .show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Thanks", color = Color.White)
            }
        },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF008000),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Chúc mừng!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Địa chỉ mới của bạn đã được thêm.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(16.dp),
        containerColor = Color.White
    )
}

@Preview(showBackground = true)
@Composable
fun NewAddressPreview() {
    val navController = rememberNavController()
    val addressViewModel = viewModel<AddressViewModel>()
    val newAddressViewModel = viewModel<NewAddressViewModel>() // Thêm ViewModel này

    NewAddressScreen(navController, addressViewModel, newAddressViewModel, customerId = "preview-customer-id")
}
