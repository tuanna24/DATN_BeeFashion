package fpl.md19.beefashion.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBottomSheet(
    onDismiss: () -> Unit,
    minPrice: Int,
    maxPrice: Int,
    onPriceChange: (IntRange) -> Unit,
    onApply: () -> Unit // Thêm tham số này
) {

    var priceRange by remember { mutableStateOf(minPrice.toFloat()..maxPrice.toFloat()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Tìm kiếm nâng cao", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Khoảng giá", fontSize = 16.sp, fontWeight = FontWeight.Medium)

            RangeSlider(
                value = priceRange,
                onValueChange = { range ->
                    priceRange = range.start.coerceAtLeast(minPrice.toFloat())..range.endInclusive.coerceAtMost(maxPrice.toFloat())
                },
                valueRange = minPrice.toFloat()..maxPrice.toFloat(), // Sửa lỗi cú pháp
                steps = 10,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Black,         // Đầu tròn màu đen
                    activeTrackColor = Color.Black,   // Phần được chọn màu đen
                    inactiveTrackColor = Color.Gray   // Phần chưa chọn màu xám
                ),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Text(
                "Từ ${formatCurrency(priceRange.start.toInt())} - ${formatCurrency(priceRange.endInclusive.toInt())}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onPriceChange(IntRange(priceRange.start.toInt(), priceRange.endInclusive.toInt()))
                    onDismiss()
                    onApply()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) {
                Text("Áp dụng")
            }
        }
    }
}

// Hàm định dạng giá tiền thành dạng 1.000.000 đ
fun formatCurrency(price: Int): String {
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    return formatter.format(price) + " đ"
}
