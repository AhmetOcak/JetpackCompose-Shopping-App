package com.ahmetocak.shoppingapp.common.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ahmetocak.shoppingapp.presentation.chart.ChartScreen
import com.ahmetocak.shoppingapp.presentation.favorites.FavoritesScreen
import com.ahmetocak.shoppingapp.presentation.home.HomeScreen
import com.ahmetocak.shoppingapp.presentation.login.LoginScreen
import com.ahmetocak.shoppingapp.presentation.payment.PaymentScreen
import com.ahmetocak.shoppingapp.presentation.product.ProductScreen
import com.ahmetocak.shoppingapp.presentation.profile.ProfileScreen
import com.ahmetocak.shoppingapp.presentation.search.SearchScreen
import com.ahmetocak.shoppingapp.presentation.sign_up.SignUpScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

private val bottomNavItems = listOf(
    BottomNavItem.HomeScreen,
    BottomNavItem.SearchScreen,
    BottomNavItem.FavoritesScreen,
    BottomNavItem.ProfileScreen
)

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = NavScreen.HomeScreen.route
) {
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (bottomNavItems.find { it.route == currentDestination?.route } != null) {
                BottomNavigationView(currentDestination, navController)
            }
        }
    ) {
        AnimatedNavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = startDestination
        ) {
            composable(route = NavScreen.LoginScreen.route) {
                LoginScreen()
            }
            composable(route = NavScreen.SignUpScreen.route) {
                SignUpScreen()
            }
            composable(route = NavScreen.HomeScreen.route) {
                HomeScreen()
            }
            composable(route = NavScreen.ChartScreen.route) {
                ChartScreen()
            }
            composable(route = NavScreen.FavoritesScreen.route) {
                FavoritesScreen()
            }
            composable(route = NavScreen.PaymentScreen.route) {
                PaymentScreen()
            }
            composable(route = NavScreen.ProductScreen.route) {
                ProductScreen()
            }
            composable(route = NavScreen.ProfileScreen.route) {
                ProfileScreen()
            }
            composable(route = NavScreen.SearchScreen.route) {
                SearchScreen()
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