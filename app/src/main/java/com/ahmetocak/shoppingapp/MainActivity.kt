package com.ahmetocak.shoppingapp

import android.Manifest
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.ahmetocak.shoppingapp.common.helpers.getRememberMe
import com.ahmetocak.shoppingapp.core.alarm.ShoppingAlarmScheduler
import com.ahmetocak.shoppingapp.core.navigation.graph.NavGraph
import com.ahmetocak.shoppingapp.core.navigation.screens.NavScreen
import com.ahmetocak.shoppingapp.designsystem.theme.ShoppingAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var shoppingAlarmScheduler: ShoppingAlarmScheduler

    private var hasNotificationPermission: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { hasNotificationPermission = it }
            )

            if (!hasNotificationPermission){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    SideEffect {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            shoppingAlarmScheduler.schedule()

            ShoppingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (sharedPreferences.getRememberMe()) {
                        NavGraph(
                            startDestination = NavScreen.HomeScreen.route,
                            sharedPreferences = sharedPreferences
                        )
                    } else {
                        NavGraph(
                            startDestination = NavScreen.LoginScreen.route,
                            sharedPreferences = sharedPreferences
                        )
                    }
                }
            }
        }
    }
}