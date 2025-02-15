package fpl.md19.beefashion.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LogOutComponent(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Warning Icon
                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = "Warning Icon",
                        tint = Color.Red,
                        modifier = Modifier.size(48.dp)
                    )
                    // Title
                    Text(
                        text = "Đăng xuất ?",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    // Subtitle
                    Text(
                        text = "Bạn có chắc chắn muốn đăng xuất không ?",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                    // Buttons Column
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Logout Button
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Có, Đăng xuất",
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        // Cancel Button
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Không, Hủy bỏ",
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LogOutComponentPreview() {
    LogOutComponent(
        onConfirm = { /* Mock confirm action, do nothing for preview */ },
        onDismiss = { /* Mock dismiss action, do nothing for preview */ },
        isVisible = true // Show the dialog in the preview
    )
}