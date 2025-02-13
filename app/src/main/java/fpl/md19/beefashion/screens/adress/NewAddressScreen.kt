package fpl.md19.beefashion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun NewAddressScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
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
                    .clickable { }
            )
            Text(
                text = "Địa chỉ mới",
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
        Box(modifier = Modifier.weight(1f)) {
            Image(
                painter = painterResource(id = R.drawable.map_placeholder),
                contentDescription = "Map",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Pin",
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }
        AddressForm(modifier = Modifier.fillMaxWidth())
    }
}


@Composable
fun AddressForm(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) } // Biến trạng thái để hiển thị dialog
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Choose one") }
    val items = listOf("Home", "Office", "Apartment", "Other")

    var address by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Địa chỉ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { /* Đóng form */ }) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Box {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tên địa chỉ") },
                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        modifier = Modifier.clickable { expanded = true }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            selectedText = label
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Địa chỉ đầy đủ") },
            placeholder = { Text("Enter your full address...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    checkmarkColor = Color.White
                )
            )
            Text("Đặt làm địa chỉ mặc định")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (address.isNotEmpty()) Color.Black else Color.Gray),
            shape = RoundedCornerShape(12.dp),
            enabled = address.isNotEmpty()
        ) {
            Text(text = "Thêm", color = Color.White)
        }
    }

    if (showDialog) {
        SuccessDialog(onDismiss = { showDialog = false })
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
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
                Text(text = "Địa chỉ mới của bạn đã được thêm.", fontSize = 14.sp, color = Color.Gray)
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
    NewAddressScreen()
}
