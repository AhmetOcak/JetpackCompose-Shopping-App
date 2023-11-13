package com.ahmetocak.shoppingapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ahmetocak.shoppingapp.common.helpers.getRememberMe
import com.ahmetocak.shoppingapp.core.navigation.graph.NavGraph
import com.ahmetocak.shoppingapp.core.navigation.screens.NavScreen
import com.ahmetocak.shoppingapp.ui.theme.ShoppingAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (sharedPreferences.getRememberMe()) {
                        NavGraph(startDestination = NavScreen.CartScreen.route)
                    } else {
                        NavGraph(startDestination = NavScreen.LoginScreen.route)
                    }
                }
            }
        }
    }
}