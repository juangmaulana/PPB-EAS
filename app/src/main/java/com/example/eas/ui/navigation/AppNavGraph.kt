package com.example.eas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eas.ui.screen.AdminDashboardScreen
import com.example.eas.ui.screen.AdminOrdersScreen
import com.example.eas.ui.screen.AdminProductsScreen
import com.example.eas.ui.screen.AdminProfileScreen
import com.example.eas.ui.screen.AdminRewardsSettingsScreen
import com.example.eas.ui.screen.AdminUsersScreen
import com.example.eas.ui.screen.AuthScreen
import com.example.eas.ui.screen.CartScreen
import com.example.eas.ui.screen.CoffeeAppSplashScreen
import com.example.eas.ui.screen.CustomerHomeScreen
import com.example.eas.ui.screen.CustomerMemberCardScreen
import com.example.eas.ui.screen.CustomerProfileScreen
import com.example.eas.ui.screen.CustomerRewardsScreen
import com.example.eas.ui.screen.OrderDetailScreen
import com.example.eas.ui.screen.OrderHistoryScreen
import com.example.eas.ui.screen.PaymentResultScreen
import com.example.eas.ui.screen.ProductDetailScreen
import com.example.eas.ui.screen.ProductListScreen
import com.example.eas.ui.viewmodel.CoffeeShopViewModel

@Composable
fun AppNavGraph(viewModel: CoffeeShopViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            CoffeeAppSplashScreen(viewModel = viewModel) { isAdmin ->
                navController.navigate(if (viewModel.currentUser.value == null) Screen.Auth.route else if (isAdmin) Screen.AdminDashboard.route else Screen.CustomerHome.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }
        composable(Screen.Auth.route) {
            AuthScreen(viewModel = viewModel) { isAdmin ->
                navController.navigate(if (isAdmin) Screen.AdminDashboard.route else Screen.CustomerHome.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            }
        }
        composable(Screen.CustomerHome.route) {
            CustomerHomeScreen(
                viewModel = viewModel,
                onProducts = { navController.navigate(Screen.ProductList.route) },
                onProductDetail = { navController.navigate(Screen.ProductDetail.createRoute(it)) },
                onCart = { navController.navigate(Screen.Cart.route) },
                onRewards = { navController.navigate(Screen.CustomerRewards.route) },
                onHistory = { navController.navigate(Screen.OrderHistory.route) },
                onProfile = { navController.navigate(Screen.CustomerProfile.route) }
            )
        }
        composable(Screen.ProductList.route) {
            ProductListScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onProductDetail = { navController.navigate(Screen.ProductDetail.createRoute(it)) },
                onCart = { navController.navigate(Screen.Cart.route) }
            )
        }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { entry ->
            ProductDetailScreen(
                viewModel = viewModel,
                productId = entry.arguments?.getInt("productId") ?: 0,
                onBack = { navController.popBackStack() },
                onCart = { navController.navigate(Screen.Cart.route) }
            )
        }
        composable(Screen.Cart.route) {
            CartScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOrderCreated = { navController.navigate(Screen.PaymentResult.createRoute(it)) },
                onProducts = { navController.navigate(Screen.ProductList.route) }
            )
        }
        composable(
            route = Screen.PaymentResult.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { entry ->
            PaymentResultScreen(
                viewModel = viewModel,
                orderId = entry.arguments?.getInt("orderId") ?: 0,
                onHistory = { navController.navigate(Screen.OrderHistory.route) },
                onHome = { navController.navigateCustomerHome() }
            )
        }
        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOrderDetail = { navController.navigate(Screen.OrderDetail.createRoute(it)) }
            )
        }
        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { entry ->
            OrderDetailScreen(
                viewModel = viewModel,
                orderId = entry.arguments?.getInt("orderId") ?: 0,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.CustomerRewards.route) {
            CustomerRewardsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.CustomerProfile.route) {
            CustomerProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onMemberCard = { navController.navigate(Screen.CustomerMemberCard.route) },
                onRewards = { navController.navigate(Screen.CustomerRewards.route) },
                onHistory = { navController.navigate(Screen.OrderHistory.route) },
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(Screen.CustomerMemberCard.route) {
            CustomerMemberCardScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                viewModel = viewModel,
                onProducts = { navController.navigate(Screen.AdminProducts.route) },
                onUsers = { navController.navigate(Screen.AdminUsers.route) },
                onOrders = { navController.navigate(Screen.AdminOrders.route) },
                onRewardsSettings = { navController.navigate(Screen.AdminRewardsSettings.route) },
                onProfile = { navController.navigate(Screen.AdminProfile.route) },
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.AdminProducts.route) {
            AdminProductsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.AdminUsers.route) {
            AdminUsersScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.AdminOrders.route) {
            AdminOrdersScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.AdminRewardsSettings.route) {
            AdminRewardsSettingsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.AdminProfile.route) {
            AdminProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

private fun NavHostController.navigateCustomerHome() {
    navigate(Screen.CustomerHome.route) {
        popUpTo(Screen.CustomerHome.route) { inclusive = true }
        launchSingleTop = true
    }
}
