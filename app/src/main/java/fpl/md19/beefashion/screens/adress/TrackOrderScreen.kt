package fpl.md19.beefashion

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrackOrderScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { }
                )
                Text(
                    text = "Tình trạng đơn hàng",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_notifications),
                    contentDescription = "Notification Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { }
                )
            }

            Box() {
                Image(
                    painter = painterResource(id = R.drawable.map_placeholder),
                    contentDescription = "Map",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        BottomSheetOrderStatus(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun BottomSheetOrderStatus(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 200.dp),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Trạng thái",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray
                    )
                }
            }

            // Status Steps
            OrderStatusStep(
                status = "Packing",
                address = "2336 Jack Warren Rd, Delta Junction, Alaska",
                completed = true
            )
            OrderStatusStep(
                status = "Picked",
                address = "2417 Tongass Ave #111, Ketchikan, Alaska",
                completed = true
            )
            OrderStatusStep(
                status = "In Transit",
                address = "16 Rr 2, Ketchikan, Alaska",
                completed = true
            )
            OrderStatusStep(
                status = "Delivered",
                address = "925 S Chugach St #APT 10, Alaska",
                completed = false
            )

            Divider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = Color(0xFFEEEEEE)
            )

            DeliveryPersonInfo(
                name = "Nguyen Minh Nghia",
                role = "Delivery Guy"
            )
        }
    }
}

@Composable
fun OrderStatusStep(
    status: String,
    address: String,
    completed: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    if (completed) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Status",
                tint = if (completed) Color(0xFF4CAF50) else Color(0xFFBDBDBD),
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = status,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = address,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun DeliveryPersonInfo(name: String, role: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_details),
            contentDescription = "Delivery Person",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5))
                .padding(8.dp)
        )

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = role,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        IconButton(
            onClick = {},
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF4CAF50), CircleShape)
        ) {
            Icon(
                Icons.Default.Phone,
                contentDescription = "Call",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackOrderPreview() {
    TrackOrderScreen()
}