package com.ahmetocak.shoppingapp.presentation.home

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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.helpers.PreferenceManager
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.presentation.home.favorites.FavoritesScreen
import com.ahmetocak.shoppingapp.presentation.home.product.ProductScreen
import com.ahmetocak.shoppingapp.presentation.home.profile.ProfileScreen
import com.ahmetocak.shoppingapp.presentation.home.search.SearchScreen
import com.ahmetocak.shoppingapp.utils.REMEMBER_ME
import com.google.firebase.auth.FirebaseAuth

fun NavGraphBuilder.addHomeGraph(
    onProductClick: (Product, NavBackStackEntry) -> Unit,
    onSignOutClick: (NavBackStackEntry) -> Unit,
    onCartClick: (NavBackStackEntry) -> Unit,
    onNavigateToRoute: (String) -> Unit
) {
    composable(HomeSections.PRODUCT.route) { from ->
        ProductScreen(
            onProductClick = remember { { product -> onProductClick(product, from) } },
            onCartClick = remember { { onCartClick(from) } },
            onNavigateRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.SEARCH.route) { from ->
        SearchScreen(
            onProductClick = remember { { product -> onProductClick(product, from) } },
            onNavigateRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.FAVORITES.route) { from ->
        FavoritesScreen(
            onProductClick = remember {
                { product -> onProductClick(product, from) }
            },
            onNavigateRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.PROFILE.route) { from ->
        val preferenceManager = PreferenceManager(LocalContext.current)
        ProfileScreen(
            onSignOutClicked = remember {
                {
                    FirebaseAuth.getInstance().signOut()
                    preferenceManager.saveData(REMEMBER_ME, false)
                    onSignOutClick(from)
                }
            },
            onNavigateRoute = onNavigateToRoute
        )
    }
}

enum class HomeSections(
    @StringRes val title: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String
) {
    PRODUCT(R.string.home, Icons.Filled.Home, Icons.Outlined.Home, "home/product"),
    SEARCH(R.string.search, Icons.Filled.Search, Icons.Outlined.Search, "home/search"),
    FAVORITES(
        R.string.favorites,
        Icons.Filled.Favorite,
        Icons.Outlined.FavoriteBorder,
        "home/favorite"
    ),
    PROFILE(
        R.string.profile,
        Icons.Filled.AccountCircle,
        Icons.Outlined.AccountCircle,
        "home/profile"
    )
}

@Composable
fun ShoppingAppBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit
) {
    val currentSection = tabs.first { it.route == currentRoute }

    NavigationBar {
        tabs.forEach { section ->
            NavigationBarItem(
                selected = currentSection.route == section.route,
                onClick = {
                    if (currentSection.route != section.route) {
                        navigateToRoute(section.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (currentSection.route == section.route) section.selectedIcon else section.unSelectedIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = section.title))
                }
            )
        }
    }
}