package com.ahmetocak.shoppingapp.core.navigation.graph

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmetocak.shoppingapp.common.helpers.removeRememberMe
import com.ahmetocak.shoppingapp.core.navigation.BottomNavItem
import com.ahmetocak.shoppingapp.core.navigation.screens.NavScreen
import com.ahmetocak.shoppingapp.presentation.cart.CartScreen
import com.ahmetocak.shoppingapp.presentation.favorites.FavoritesScreen
import com.ahmetocak.shoppingapp.presentation.home.HomeScreen
import com.ahmetocak.shoppingapp.presentation.login.LoginScreen
import com.ahmetocak.shoppingapp.presentation.payment.PaymentScreen
import com.ahmetocak.shoppingapp.presentation.product.ProductScreen
import com.ahmetocak.shoppingapp.presentation.profile.ProfileScreen
import com.ahmetocak.shoppingapp.presentation.search.SearchScreen
import com.ahmetocak.shoppingapp.presentation.sign_up.SignUpScreen
import com.ahmetocak.shoppingapp.utils.NavKeys
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private val bottomNavItems = listOf(
    BottomNavItem.HomeScreen,
    BottomNavItem.SearchScreen,
    BottomNavItem.FavoritesScreen,
    BottomNavItem.ProfileScreen
)

@SuppressLint("CommitPrefEdits")
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = NavScreen.HomeScreen.route,
    sharedPreferences: SharedPreferences
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(modifier = modifier.fillMaxSize(), bottomBar = {
        if (bottomNavItems.find { it.route == currentDestination?.route } != null) {
            BottomNavigationView(currentDestination, navController)
        }
    }) {
        NavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = startDestination
        ) {
            composable(route = NavScreen.LoginScreen.route) {
                LoginScreen(onNavigateSignUp = {
                    navController.navigate(NavScreen.SignUpScreen.route)
                }, onNavigateHome = {
                    navController.navigate(NavScreen.HomeScreen.route) {
                        popUpTo(NavScreen.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(route = NavScreen.SignUpScreen.route) {
                SignUpScreen(onNavigate = { navController.navigateUp() })
            }
            composable(route = NavScreen.HomeScreen.route) {
                HomeScreen(
                    onNavigateProductScreen = { product ->
                        val encodedValue =
                            URLEncoder.encode(Gson().toJson(product), StandardCharsets.UTF_8.toString())
                        navController.navigate("${NavScreen.ProductScreen.route}/$encodedValue")
                    },
                    onNavigateCartScreen = {
                        navController.navigate(NavScreen.CartScreen.route)
                    }
                )
            }
            composable(route = NavScreen.CartScreen.route) {
                CartScreen(onNavigatePaymentScreen = { totalAmount ->
                    navController.navigate("${NavScreen.PaymentScreen.route}/$totalAmount")
                })
            }
            composable(route = NavScreen.FavoritesScreen.route) {
                FavoritesScreen(onNavigateProductScreen = { product ->
                    val encodedValue =
                        URLEncoder.encode(Gson().toJson(product), StandardCharsets.UTF_8.toString())
                    navController.navigate("${NavScreen.ProductScreen.route}/$encodedValue")
                })
            }
            composable(
                route = "${NavScreen.PaymentScreen.route}/{${NavKeys.TOTAL_AMOUNT}}",
                arguments = listOf(
                    navArgument(NavKeys.TOTAL_AMOUNT) { type = NavType.FloatType }
                )
            ) {
                PaymentScreen(onNavigateHomeScreen = {
                    navController.navigate(NavScreen.HomeScreen.route) {
                        popUpTo(NavScreen.HomeScreen.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(
                route = "${NavScreen.ProductScreen.route}/{${NavKeys.PRODUCT}}",
                arguments = listOf(
                    navArgument(NavKeys.PRODUCT) { type = NavType.StringType }
                )
            ) {
                ProductScreen(onNavigateCartScreen = {
                    navController.navigate(NavScreen.CartScreen.route)
                })
            }
            composable(route = NavScreen.ProfileScreen.route) {
                ProfileScreen(onSignOutClicked = {
                    sharedPreferences.edit().removeRememberMe()

                    navController.navigate(NavScreen.LoginScreen.route) {
                        popUpTo(0)
                    }
                })
            }
            composable(route = NavScreen.SearchScreen.route) {
                SearchScreen(onNavigateProductScreen = { product ->
                    val encodedValue =
                        URLEncoder.encode(Gson().toJson(product), StandardCharsets.UTF_8.name())
                    navController.navigate("${NavScreen.ProductScreen.route}/$encodedValue")
                })
            }
        }
    }
}

@Composable
private fun BottomNavigationView(
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBar {
        bottomNavItems.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                selected = isSelected,
                icon = {
                    Icon(
                        imageVector = if (isSelected) {
                            screen.selectedIcon
                        } else {
                            screen.unSelectedIcon
                        },
                        contentDescription = null
                    )
                },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(text = stringResource(id = screen.labelId)) }
            )
        }
    }
}