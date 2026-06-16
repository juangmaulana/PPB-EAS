package com.example.eas.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object CustomerHome : Screen("customer_home")
    object ProductList : Screen("product_list")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    object Cart : Screen("cart")
    object PaymentResult : Screen("payment_result/{orderId}") {
        fun createRoute(orderId: Int) = "payment_result/$orderId"
    }
    object OrderHistory : Screen("order_history")
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: Int) = "order_detail/$orderId"
    }
    object CustomerRewards : Screen("customer_rewards")
    object CustomerProfile : Screen("customer_profile")
    object CustomerMemberCard : Screen("customer_member_card")
    object AdminDashboard : Screen("admin_dashboard")
    object AdminProducts : Screen("admin_products")
    object AdminUsers : Screen("admin_users")
    object AdminOrders : Screen("admin_orders")
    object AdminRewardsSettings : Screen("admin_rewards_settings")
    object AdminProfile : Screen("admin_profile")

}
