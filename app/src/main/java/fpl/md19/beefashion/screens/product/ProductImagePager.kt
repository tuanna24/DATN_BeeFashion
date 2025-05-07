package fpl.md19.beefashion.screens.product

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductImagePager(images: List<String>) {
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Ảnh trong pager
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Product Image $page",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Dấu chấm đè lên ảnh, ở giữa, cách đáy 15dp
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 15.dp),
            activeColor = androidx.compose.ui.graphics.Color(0xFFFF5722),
            inactiveColor = androidx.compose.ui.graphics.Color.LightGray
        )
    }
}

