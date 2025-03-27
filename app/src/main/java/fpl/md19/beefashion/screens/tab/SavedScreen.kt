package fpl.md19.beefashion.screens.tab

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import fpl.md19.beefashion.models.Saved

@Composable
fun SavedScreen(navController: NavController) {
    val savedList = remember {
        mutableStateListOf(
            Saved("Áo phông", "189000", R.drawable.ao_phong),
            Saved("Áo sơ mi", "250000", R.drawable.ao_phong),
            Saved("Áo sơ mi", "250000", R.drawable.ao_phong),
            Saved("Áo sơ mi", "250000", R.drawable.ao_phong),
            Saved("Áo sơ mi", "250000", R.drawable.ao_phong),
            Saved("Áo sơ mi", "250000", R.drawable.ao_phong),
            Saved("Áo sơ mi", "250000", R.drawable.ao_phong),
        )
    }

    var itemToDelete by remember { mutableStateOf<Saved?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sản phẩm yêu thích",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        if (savedList.isEmpty()) {
            EmptySavedScreen()
        } else {
            SaveList(savedList, { item -> itemToDelete = item }, navController)
        }
    }

    // Hiển thị Dialog xác nhận khi xóa sản phẩm
    itemToDelete?.let { item ->
        ConfirmDeleteDialog(
            item = item,
            onConfirm = {
                savedList.remove(item)
                Toast.makeText(
                    context,
                    "Bạn đã xóa sản phẩm ${item.titile} khỏi danh sách yêu thích.",
                    Toast.LENGTH_SHORT
                ).show()
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

@Composable
fun SaveList(saver: List<Saved>, onRemoveRequest: (Saved) -> Unit, navController: NavController) {
    val state = rememberLazyStaggeredGridState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = state,
    ) {
        items(saver) { savedItem ->
            MySaveCard(savedItem, onRemoveRequest, navController) // Truyền navController vào
        }
    }
}

@Composable
fun MySaveCard(saved: Saved, onRemoveRequest: (Saved) -> Unit, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp, top = 20.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Box {
            Image(
                painter = painterResource(id = saved.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable{navController.navigate("productScreen")}
            )
            Icon(
                painter = painterResource(R.drawable.hear_red),
                contentDescription = "Remove from favorites",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .background(Color(0xFFBD0000), shape = RoundedCornerShape(4.dp))
                    .clickable { onRemoveRequest(saved) } // Hiển thị Dialog xác nhận
                    .padding(4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = saved.titile,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = saved.price.toString(),
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun ConfirmDeleteDialog(item: Saved, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Xác nhận xóa") },
        text = { Text("Bạn có chắc muốn xóa sản phẩm \"${item.titile}\" khỏi danh sách yêu thích?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@Composable
fun EmptySavedScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.heart_icon),
                contentDescription = "No results",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Không có sản phẩm yêu thích",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hãy thử một từ khóa khác hoặc tìm kiếm thêm sản phẩm",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSavedScreen() {
    val navController = rememberNavController()
    SavedScreen(navController)
}
