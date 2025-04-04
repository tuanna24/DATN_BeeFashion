package fpl.md19.beefashion.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import fpl.md19.beefashion.AddressScreen
import fpl.md19.beefashion.NewAddressScreen
import fpl.md19.beefashion.R
import fpl.md19.beefashion.TrackOrderScreen
import fpl.md19.beefashion.screens.accounts.MyDetailsScreen
import fpl.md19.beefashion.screens.accounts.NotificationsScreen
import fpl.md19.beefashion.screens.adress.UpdateScreen
import fpl.md19.beefashion.screens.auth.ForgotPasswordScreen
import fpl.md19.beefashion.screens.auth.LoginScreen
import fpl.md19.beefashion.screens.auth.SignUpScreen
import fpl.md19.beefashion.screens.auth.WelcomeScreen
import fpl.md19.beefashion.screens.auth.WelcomeScreen1
import fpl.md19.beefashion.screens.cart.MyOderScreen
import fpl.md19.beefashion.screens.payment.PaymentScreen
import fpl.md19.beefashion.screens.payment.SuccessScreen
import fpl.md19.beefashion.screens.product.ProductScreen
import fpl.md19.beefashion.screens.support.HelpScreen
import fpl.md19.beefashion.screens.tab.AccountScreen
import fpl.md19.beefashion.screens.tab.CartScreen
import fpl.md19.beefashion.screens.tab.HomeScreen
import fpl.md19.beefashion.screens.tab.SavedScreen
import fpl.md19.beefashion.screens.tab.SearchScreen
import fpl.md19.beefashion.viewModels.AddressViewModel
import fpl.md19.beefashion.viewModels.NewAddressViewModel

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
        content = { navController -> HomeScreen(navController)},
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
        content = { navController -> AccountScreen(navController) },
        screenName = "accountScreen"
    )
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            bottomBar = { TabView(tabItems, navController = navController) }) {
            Box(modifier = Modifier.padding(it)) {
                NestedBottomTab(
                    navController = navController as NavHostController,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NestedBottomTab(
    navController: NavHostController
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
            LoginScreen(navController)
        }
        composable("SignUpScreen") {
            SignUpScreen(navController)
        }
        composable("addressScreen") {
                backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            val viewModel: AddressViewModel = viewModel {
                AddressViewModel()
            }
            AddressScreen(navController, viewModel, customerId)
        }

        composable(
            route = "AddressScreen/{customerId}",
            arguments = listOf(navArgument("customerId")
            { type = NavType.StringType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            val viewModel: AddressViewModel = viewModel {
                AddressViewModel()
            }
            AddressScreen(navController, viewModel, customerId)
        }

        composable(
            route = "NewAddressScreen/{customerId}",
            arguments = listOf(navArgument("customerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""

            // Get the same ViewModel instance from the parent
            val addressViewModel: AddressViewModel = viewModel()

            // Lấy ViewModel trực tiếp
            val newAddressViewModel: NewAddressViewModel = viewModel()

            NewAddressScreen(
                navController = navController,
                addressViewModel = addressViewModel,
                newAddressViewModel = newAddressViewModel,
                customerId = customerId
            )
        }

        composable(
            route = "updateScreen/{customerId}",
            arguments = listOf(navArgument("customerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            val addressViewModel: AddressViewModel = viewModel()
            val newAddressViewModel: NewAddressViewModel = viewModel()

            UpdateScreen(
                navController = navController,
                addressViewModel = addressViewModel,
                newAddressViewModel = newAddressViewModel,
                customerId = customerId
            )
        }

        composable(
            "paymentScreen/{fullAddress}",
            arguments = listOf(navArgument("fullAddress") { type = NavType.StringType })
        ) { backStackEntry ->
            val fullAddress = backStackEntry.arguments?.getString("fullAddress")
            val viewModel: AddressViewModel = viewModel()
            PaymentScreen(
                navController = navController,
                viewModel = viewModel,
               fullAddress = fullAddress
            )
        }

        composable("myOderScreen") {
            MyOderScreen(navController)
        }
        composable("MyDetailsScreen") {
            MyDetailsScreen(
                navController,
                onBackClick = { /* Do nothing or mock back click action */ },
                onNotificationClick = { /* Do nothing or mock notification click action */ },
                onSubmit = { /* Do nothing or mock submit action */ }
            )
        }
        composable("NotificationsScreen") {
            NotificationsScreen(navController)
        }
        composable("HelpScreen") {
            HelpScreen(navController)
        }
        composable("ForgotPasswordScreen") {
            ForgotPasswordScreen(navController)
        }
        composable("trackOrderScreen") {
            TrackOrderScreen(navController)
        }
        composable("HomeScreen") {
            HomeScreen(navController)
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
            AccountScreen(navController)
        }
        composable(
            route = "productScreen/{productID}/{isFav}",
            arguments = listOf(
                navArgument("productID") {type = NavType.StringType},
                navArgument("isFav") {type = NavType.BoolType}
            )
        )
        { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productID")
            val isFavoriteByCurrentUser = backStackEntry.arguments?.getBoolean("isFav", false)

            if (productId.isNullOrEmpty() || isFavoriteByCurrentUser == null) {
                // Xử lý khi productId không hợp lệ, có thể chuyển hướng hoặc hiển thị thông báo lỗi
                navController.popBackStack()
            } else {
                ProductScreen(navController = navController, productId = productId, isFavoriteByCurrentUser = isFavoriteByCurrentUser)
            }
        }
        composable("myOderScreen") {
            MyOderScreen(navController)
        }
        composable("successScreen") {
            SuccessScreen(navController)
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

    val bottomBarDestination = tabBarItems.any {
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TabBarIconView(
                                icon = if (selectedTabIndex == index) tabBarItem.selectedIcon else tabBarItem.unselectedIcon,
                                isFocused = selectedTabIndex == index
                            )
                            Text(
                                text = when (tabBarItem.screenName) {
                                    "HomeScreen" -> "Trang chủ"
                                    "searchScreen" -> "Tìm kiếm"
                                    "cartScreen" -> "Giỏ hàng"
                                    "savedScreen" -> "Yêu thích"
                                    "accountScreen" -> "Tài khoản"
                                    else -> ""
                                },
                                color = if (selectedTabIndex == index) Color(0xFFFF5722) else Color.Gray,
                                fontSize = 12.sp
                            )
                        }
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
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(25.dp)
                .offset(y = (-2).dp) // Dịch chuyển nhẹ lên trên để cân đối
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