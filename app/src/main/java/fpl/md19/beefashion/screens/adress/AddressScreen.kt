package fpl.md19.beefashion

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.AddressModel
import fpl.md19.beefashion.viewModels.AddressViewModel
import fpl.md19.beefashion.viewModels.AddressViewModelFactory
import kotlin.math.roundToInt

@Composable
fun AddressScreen(
    navController: NavController,
    viewModel: AddressViewModel,
    customerId: String
) {
    val apiService = remember { HttpRequest.getInstance() }
    val factory = remember { AddressViewModelFactory(apiService) }
    val viewModel: AddressViewModel = viewModel(factory = factory)
    var selectedAddress by remember { mutableStateOf("") }
    val addresses by viewModel.addresses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val deleteStatus by viewModel.deleteStatus.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var addressToDelete by remember { mutableStateOf<AddressModel?>(null) }


    LaunchedEffect(customerId) {
        Log.d("AddressScreen", "User ID being used: '$customerId'")
        if (customerId.isNotBlank()) {
            viewModel.fetchAddresses(customerId)
        }
    }
    LaunchedEffect(addresses) {
        Log.d("AddressScreen", "Danh sách địa chỉ sau khi cập nhật: ${addresses.size}")
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(deleteStatus) {
        when (deleteStatus) {
            is AddressViewModel.DeleteStatus.Success -> {
                snackbarHostState.showSnackbar("Địa chỉ đã được xóa thành công")
            }
            is AddressViewModel.DeleteStatus.Error -> {
                snackbarHostState.showSnackbar((deleteStatus as AddressViewModel.DeleteStatus.Error).message)
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Địa chỉ",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = R.drawable.ic_notifications),
                contentDescription = "Notifications",
                modifier = Modifier
                    .size(24.dp)
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
            val fixedAddresses = addresses.take(10)
            Log.d("AddressScreen", "Displaying addresses: ${addresses.size}")
            addresses.forEach { address ->
                Log.d("AddressScreen", "Address: $address")
            }

            val sortedAddresses = if (selectedAddress != null) {
                val selected = fixedAddresses.find { it.id == selectedAddress }
                val others = fixedAddresses.filter { it.id != selectedAddress }
                listOfNotNull(selected) + others
            } else {
                fixedAddresses
            }

            sortedAddresses.forEachIndexed { index, addressModel ->
                Log.d("AddressScreen", "Displaying address at index $index: ${addressModel.id}")
                val fullAddress =
                    "${addressModel.detail}, ${addressModel.ward}, ${addressModel.district}, ${addressModel.province}"
                val isDefault = index == 0 && selectedAddress != null

                AddressItem(
                    address = fullAddress,
                    selected = selectedAddress == addressModel.id,
                    isDefault = isDefault,
                    onSelect = { selectedAddress = addressModel.id },
                    onDelete = {
                        Log.d("UI", "Preparing to delete: ${addressModel.id}")
                        addressToDelete = addressModel
                        showDeleteDialog = true
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("NewAddressScreen/$customerId")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Thêm địa chỉ mới", color = Color.Black)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text(text = "Chọn", color = Color.White, fontWeight = FontWeight.Bold)
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
}
@Composable
fun AddressItem(
    address: String,
    selected: Boolean,
    isDefault: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit
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
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
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
                },
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
                    if (isDefault) {
                        Text(
                            text = "Mặc định",
                            fontSize = 8.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
                                .padding(horizontal = 6.dp)
                        )
                    }
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
}
@Preview(showBackground = true)
@Composable
fun AddressPreview() {
    val navController = rememberNavController()
    val viewModel: AddressViewModel = viewModel()

    LaunchedEffect(Unit) { viewModel.fetchAddresses("12345") }
    AddressScreen(navController, viewModel, "12345")
}



