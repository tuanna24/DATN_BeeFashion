package fpl.md19.beefashion

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.models.AddressModel
import fpl.md19.beefashion.screens.adress.AddressPreferenceManager
import fpl.md19.beefashion.viewModels.AddressViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun AddressScreen(
    navController: NavController,
    viewModel: AddressViewModel,
    customerId: String,
    returnToPayment: Boolean = false  // Default is false
) {

    val addresses by viewModel.addresses.collectAsState()
    //  val isLoading by viewModel.isLoading.collectAsState()
    val loading by viewModel.loading
    val context = LocalContext.current
    val deleteStatus by viewModel.deleteStatus.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var addressToDelete by remember { mutableStateOf<AddressModel?>(null) }
    val createStatus by viewModel.createStatus.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()
    val addressPreferenceManager = remember { AddressPreferenceManager(context) }

    // Đọc giá trị được lưu trong SharedPreferences khi khởi chạy màn hình
    var selectedAddress by remember { mutableStateOf(addressPreferenceManager.getSelectedAddress()) }

    LaunchedEffect(customerId) {
        if (customerId.isNotBlank()) {
            viewModel.fetchAddresses(customerId)
        }
    }
    LaunchedEffect(createStatus) {
        when (createStatus) {
            is AddressViewModel.CreateStatus.Success -> {
                viewModel.fetchAddresses(customerId) // Cập nhật lại sau khi thêm
            }

            else -> {}
        }
    }

    LaunchedEffect(updateStatus) {
        when (updateStatus) {
            is AddressViewModel.UpdateStatus.Success -> {
                viewModel.fetchAddresses(customerId)
                Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
            }

            is AddressViewModel.UpdateStatus.Error -> {
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
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
                    .clickable {
                        navController.popBackStack()
                    }
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

        //if (isLoading)
        if (loading) {
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
//                val fullAddress =
//                    "${addressModel.name},${addressModel.phoneNumber}\n${addressModel.detail}, ${addressModel.ward}, ${addressModel.district}, ${addressModel.province}"
                val fullAddress = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    ) {
                        append("${addressModel.name}, ${addressModel.phoneNumber}\n")
                    }
                    append("${addressModel.detail}, ${addressModel.ward}, ${addressModel.district}, ${addressModel.province}")
                }
                AddressItem(
                    context = LocalContext.current,
                    address = fullAddress.toString(),
                    selected = selectedAddress == addressModel.id,
                    onSelect = {
                        selectedAddress = addressModel.id
                        addressPreferenceManager.saveSelectedAddress(addressModel.id) // Lưu vào SharedPreferences
                        viewModel.setSelectedAddress1(fullAddress.toString())
                    },
                    onDelete = {
                        Log.d("UI", "Preparing to delete: ${addressModel.id}")
                        addressToDelete = addressModel
                        showDeleteDialog = true
                    },
                    navController = navController,
                    customerId = customerId,
                    addressModel = addressModel

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
        if (addresses.isNotEmpty()) {
            Button(
                onClick = {
                    val selectedAddressModel = addresses.find { it.id == selectedAddress }
                    if (selectedAddressModel != null) {
                        val fullAddress = Uri.encode(
                            "${selectedAddressModel.name}, ${selectedAddressModel.phoneNumber}\n" +
                                    "${selectedAddressModel.detail}, ${selectedAddressModel.ward}, " +
                                    "${selectedAddressModel.district}, ${selectedAddressModel.province}"
                        )
                        Log.d("AddressScreen", "Set selected address: $fullAddress")

                        viewModel._selectedAddress1.value = fullAddress
                        // Quay lại màn hình trước đó (PaymentScreen)
                        if (returnToPayment) {
                            navController.navigate("paymentScreen/$fullAddress") {
                                popUpTo("productScreen/{productID}/{isFav}") { inclusive = true }
                            }
                        } else {
                            // Just set the value in the ViewModel for other use cases
                            viewModel._selectedAddress1.value = fullAddress
                            Toast.makeText(
                                context,
                                "Cập nhật địa chỉ nhận hàng thành công!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Chọn", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                addressToDelete = null
            },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc chắn muốn xóa địa chỉ này không?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        Log.d("UI", "Clicked delete for address: $addressToDelete")
                        addressToDelete?.let { address ->
                            Log.d("UI", "Calling deleteAddress for id: ${address.id}")
                            viewModel.deleteAddress(address)

                            if (address.id == selectedAddress) {
                                selectedAddress = ""
                            }
                        }
                        showDeleteDialog = false
                        addressToDelete = null
                    }

                ) {
                    Text("Xóa", color = Color.Red)
                }

            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        addressToDelete = null
                    }
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun AddressItem(
    context: Context,
    address: String,
    selected: Boolean,
    onSelect: (String) -> Unit,
    onDelete: () -> Unit,
    navController: NavController,
    customerId: String,
    addressModel: AddressModel
) {
    val density = LocalDensity.current

    var offsetX by remember { mutableStateOf(0f) }
    var deleteThreshold by remember { mutableStateOf(0f) }
    val deleteRevealed = offsetX < -deleteThreshold * 0.5f

    val draggableState = rememberDraggableState { delta ->
        val newOffset = (offsetX + delta).coerceIn(-deleteThreshold, 0f)
        offsetX = newOffset
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
                .padding(end = 16.dp),

            contentAlignment = Alignment.CenterEnd

        ) {
            Row() {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(enabled = deleteRevealed) {
                            onDelete()  // Gọi callback xóa khi nhấp vào nút
                            offsetX = 0f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable() {
                            navController.navigate("updateScreen/${addressModel.id}")
                            offsetX = 0f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Update",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(87.dp)
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (offsetX > -deleteThreshold * 0.5f) {
                            offsetX = 0f
                        }
                    }
                )
                .onGloballyPositioned {
                    deleteThreshold = with(density) { 100.dp.toPx() }
                }
                .clickable {
                    onSelect(address)
                },
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                    onClick = {
                        onSelect(address)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Gray
                    )
                )

            }
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
