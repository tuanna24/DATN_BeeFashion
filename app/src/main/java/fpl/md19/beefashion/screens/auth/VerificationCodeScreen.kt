package fpl.md19.beefashion.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerificationCodeScreen(
    email: String = "dotienthanhx6@gmail.com",
    onContinueClick: () -> Unit = {},
    onResendCode: () -> Unit = {}
) {
    var code by remember { mutableStateOf("") }
    val isFormValid = code.length == 4

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Back button
        IconButton(
            onClick = { /* Handle back navigation */ },
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        // Title and subtitle
        Text(
            text = "Nhập mã 4 chữ số",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Nhập mã 4 chữ số mà bạn nhận được qua email ($email)",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Code input
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0..3) {
                OutlinedTextField(
                    value = if (code.length > i) code[i].toString() else "",
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            code = when {
                                newValue.isEmpty() -> code.substring(0, i)
                                i == code.length -> code + newValue
                                else -> code.substring(0, i) + newValue + code.substring(i + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .width(64.dp)
                        .height(56.dp),
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    maxLines = 1
                )
            }
        }

        // Resend code link
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Không nhận được email? ",
                fontSize = 16.sp,
                color = Color.Gray
            )
            TextButton(onClick = onResendCode) {
                Text(
                    text = "Gửi lại mã",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.clickable { }
                )
            }
        }

        // Continue button
//        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color.Black else Color.LightGray,
                disabledContainerColor = Color.LightGray
            ),
            enabled = isFormValid,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Tiếp tục",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun VerificationCodeScreenPreview() {
    VerificationCodeScreen()
}