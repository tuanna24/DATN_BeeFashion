package fpl.md19.beefashion.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fpl.md19.beefashion.R
import fpl.md19.beefashion.screens.auth.LoginScreen
import fpl.md19.beefashion.screens.auth.SignUpScreen
import fpl.md19.beefashion.screens.auth.WelcomeScreen
import fpl.md19.beefashion.screens.auth.WelcomeScreen1
import fpl.md19.beefashion.screens.product.ProductScreen
import fpl.md19.beefashion.screens.tab.AccountScreen
import fpl.md19.beefashion.screens.tab.CartScreen
import fpl.md19.beefashion.screens.tab.HomeScreen
import fpl.md19.beefashion.screens.tab.SavedScreen
import fpl.md19.beefashion.screens.tab.SearchScreen
import fpl.md19.beefashion.viewModels.AuthViewModel


data class TabItem(
    val unselectedIcon: Int,
    val selectedIcon: Int,
    val content: @Composable (NavController) -> Unit,
    val screenName: String,
)

val tabItems = listOf(
    TabItem(
        unselectedIcon = R.drawable.home_icon,
        selectedIcon = R.drawable.home_icon_dark,
        content = { navController -> HomeScreen(navController, authViewModel = AuthViewModel()) },
        screenName = "HomeScreen"
    ),
    TabItem(
        unselectedIcon = R.drawable.search,
        selectedIcon = R.drawable.search,
        content = { navController -> SearchScreen(navController) },
        screenName = "searchScreen"
    ),
    TabItem(
        unselectedIcon = R.drawable.cart_icon,
        selectedIcon = R.drawable.cart_icon_dack,
        content = { navController -> CartScreen(navController) },
        screenName = "cartScreen"
    ),
    TabItem(
        unselectedIcon = R.drawable.heart_icon,
        selectedIcon = R.drawable.heart_icon_dack,
        content = { navController -> SavedScreen(navController) },
        screenName = "savedScreen"
    ),
    TabItem(
        unselectedIcon = R.drawable.account_icon,
        selectedIcon = R.drawable.account_icon_dack,
        content = { navController -> AccountScreen(navController, authViewModel = AuthViewModel()) },
        screenName = "accountScreen"
    )
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            bottomBar = { TabView(tabItems, navController = navController) }) {
            Box(modifier = Modifier.padding(it)) {
                NestedBottomTab(
                    navController = navController as NavHostController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NestedBottomTab(
    navController: NavHostController, authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    NavHost(
        navController, "WelcomeScreen"
    ) {
        //dinh nghia man hinh
        composable("WelcomeScreen") {
            WelcomeScreen(navController)
        }
        composable("WelcomeScreen1") {
            WelcomeScreen1(navController)
        }
        composable("LoginScreen") {
            LoginScreen(navController, authViewModel)
        }
        composable("SignUpScreen") {
            SignUpScreen(navController, authViewModel)
        }


        composable("HomeScreen") {
            HomeScreen(navController, authViewModel)
        }
        composable("searchScreen") {
            SearchScreen(navController)
        }
        composable("cartScreen") {
            CartScreen(navController)
        }
        composable("savedScreen") {
            SavedScreen(navController)
        }
        composable("accountScreen") {
            AccountScreen(navController, authViewModel)
        }
        composable("productScreen") {
            ProductScreen(navController)
        }
    }
}

@Composable
fun TabView(tabBarItems: List<TabItem>, navController: NavController) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStack?.destination

    val bottomBarDestination = tabBarItems.any() {
        it.screenName == currentDestination?.route
    }
    if (bottomBarDestination) {
        NavigationBar(containerColor = Color.White) {
            tabBarItems.forEachIndexed { index, tabBarItem ->
                NavigationBarItem(
                    selected = selectedTabIndex == index,
                    onClick = {
                        navController.navigate(tabBarItem.screenName)
                        selectedTabIndex = index
                    },
                    icon = {
                        TabBarIconView(
                            icon = tabBarItem.selectedIcon,
                            isFocused = selectedTabIndex == index
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
            }
        }
    }
}

// This component helps to clean up the API call from our TabView above,
// but could just as easily be added inside the TabView without creating this custom component

@Composable
fun TabBarIconView(
    icon: Int,
    badgeAmount: Int? = null,
    isFocused: Boolean,
) {
    val iconSize = if (icon == R.drawable.cart_icon || icon == R.drawable.cart_icon_dack) {
        30.dp // Tăng kích thước icon giỏ hàng
    } else {
        25.dp
    }

    val iconOffset = if (icon == R.drawable.cart_icon || icon == R.drawable.cart_icon_dack) {
        (-7).dp // Dịch chuyển icon giỏ hàng lên trên
    } else {
        0.dp
    }

    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier
                .size(iconSize)
                .offset(y = iconOffset) // Đẩy icon lên trên
        )
    }
}


// This component helps to clean up the API call from our TabBarIconView above,
// but could just as easily be added inside the TabBarIconView without creating this custom component
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMusicScreen() {
//    // Tạo navController giả cho Preview
//    val navController = rememberNavController()
//
//    // Tạo movieViewModel giả
//    val movieViewModel = MovieViewModel()
//
//    // Gọi MovieListScreen với cả navController và movieViewModel
//    MovieListScreen(navController = navController, movieViewModel = movieViewModel)
}