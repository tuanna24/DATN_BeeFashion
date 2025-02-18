package fpl.md19.beefashion.screens.accounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDetailsScreen(
    navController : NavController,
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSubmit: () -> Unit
) {
    var fullName = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var dateOfBirth = remember { mutableStateOf("") }
    var gender = remember { mutableStateOf("") }
    var phoneNumber = remember { mutableStateOf("") }

    val isFormValid = fullName.value.isNotEmpty() && email.value.isNotEmpty() && dateOfBirth.value.isNotEmpty() && gender.value.isNotEmpty() && phoneNumber.value.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        // Top Bar
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
                text = "Đơn hàng",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(25.dp))

        // Full Name
        Text(
            text = "Họ tên",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = fullName.value,
            onValueChange = { fullName.value = it },
            placeholder = { Text("Nhập họ tên của bạn") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Address
        Text(
            text = "Email",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            placeholder = { Text("Nhập email của bạn") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date of Birth
        Text(
            text = "Ngày sinh",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = dateOfBirth.value,
            onValueChange = { dateOfBirth.value = it },
            placeholder = { Text("Nhập ngày sinh của bạn") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Black
            ),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Select date",
                    modifier = Modifier.size(24.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Gender
        Text(
            text = "Giới tính",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = gender.value,
            onValueChange = { gender.value = it },
            placeholder = { Text("Nhập giới tính của bạn") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number
        Text(
            text = "Số điện thoại",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            placeholder = { Text("Nhập SDT của bạn") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Black
            ),
            leadingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vietnam),
                        contentDescription = "US flag",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color.Black else Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Lưu",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MyDetailsScreenPreview() {
    val navController = rememberNavController()
    MyDetailsScreen(
        navController,
        onBackClick = { /* Do nothing or mock back click action */ },
        onNotificationClick = { /* Do nothing or mock notification click action */ },
        onSubmit = { /* Do nothing or mock submit action */ }
    )
}