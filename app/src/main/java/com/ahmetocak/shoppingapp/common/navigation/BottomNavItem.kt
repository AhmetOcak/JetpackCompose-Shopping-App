package com.ahmetocak.shoppingapp.common.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetocak.shoppingapp.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val labelId: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
) {
    object HomeScreen :
        BottomNavItem(NavRoutes.home_screen, R.string.home, Icons.Filled.Home, Icons.Outlined.Home)

    object SearchScreen :
        BottomNavItem(
            NavRoutes.search_screen,
            R.string.search,
            Icons.Filled.Search,
            Icons.Outlined.Search
        )

    object FavoritesScreen :
        BottomNavItem(
            NavRoutes.favorites_screen,
            R.string.favorites,
            Icons.Filled.Favorite,
            Icons.Outlined.FavoriteBorder
        )

    object ProfileScreen :
        BottomNavItem(
            NavRoutes.profile_screen,
            R.string.profile,
            Icons.Filled.AccountCircle,
            Icons.Outlined.AccountCircle
        )
}