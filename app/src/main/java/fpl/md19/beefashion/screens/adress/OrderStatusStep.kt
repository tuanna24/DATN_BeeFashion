package fpl.md19.beefashion.screens.adress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpl.md19.beefashion.models.OrderStatusStepModel

@Composable
fun OrderStatusStep(steps: List<OrderStatusStepModel>) {
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                // Icon + Line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                if (step.completed) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Status",
                            tint = if (step.completed) Color(0xFF4CAF50) else Color(0xFFBDBDBD),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Dotted line nếu không phải icon cuối
                    if (index < steps.size - 1) {
                        DottedLineVertical(
                            modifier = Modifier
                                .height(24.dp) // Khoảng cách giữa các icon
                                .width(1.dp)
                                .padding(top = 2.dp)
                        )
                    }
                }

                // Text content
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = step.status,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = step.address,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DottedLineVertical(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val lineHeight = size.height
        val dotHeight = 4.dp.toPx()
        val gap = 4.dp.toPx()

        var currentY = 0f
        while (currentY < lineHeight) {
            drawLine(
                color = Color.Gray,
                start = Offset(x = size.width / 2, y = currentY),
                end = Offset(x = size.width / 2, y = currentY + dotHeight),
                strokeWidth = size.width
            )
            currentY += dotHeight + gap
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderStatusStepPreview() {
    val steps = listOf(
        OrderStatusStepModel("Chờ xác nhận", "Kho HCM", true),
        OrderStatusStepModel("Đang giao hàng", "Trung chuyển Đà Nẵng", true),
        OrderStatusStepModel("Đã đến kho", "Kho Hà Nội", true),
        OrderStatusStepModel("Đang giao", "Giao cho shipper", false),
        OrderStatusStepModel("Đã giao", "Khách đã nhận", false)
    )

    OrderStatusStep(steps)
}
