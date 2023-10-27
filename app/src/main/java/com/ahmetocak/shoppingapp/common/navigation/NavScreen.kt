package com.ahmetocak.shoppingapp.common.navigation

sealed class NavScreen(val route: String) {
    object LoginScreen : NavScreen(route = NavRoutes.login_screen)
    object SignUpScreen : NavScreen(route = NavRoutes.sign_up_screen)
    object HomeScreen : NavScreen(route = NavRoutes.home_screen)
    object ChartScreen : NavScreen(route = NavRoutes.chart_screen)
    object FavoritesScreen : NavScreen(route = NavRoutes.favorites_screen)
    object PaymentScreen : NavScreen(route = NavRoutes.payment_screen)
    object ProductScreen : NavScreen(route = NavRoutes.product_screen)
    object ProfileScreen : NavScreen(route = NavRoutes.profile_screen)

    object SearchScreen : NavScreen(route = NavRoutes.search_screen)
}
