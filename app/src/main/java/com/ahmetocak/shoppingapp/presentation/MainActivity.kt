package com.ahmetocak.shoppingapp.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ahmetocak.shoppingapp.common.helpers.PreferenceManager
import com.ahmetocak.shoppingapp.core.alarm.ShoppingAlarmScheduler
import com.ahmetocak.shoppingapp.presentation.navigation.MainDestinations
import com.ahmetocak.shoppingapp.utils.REMEMBER_ME
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var shoppingAlarmScheduler: ShoppingAlarmScheduler

    @Inject
    lateinit var preferenceManager: PreferenceManager

    private var hasNotificationPermission: Boolean = false

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { hasNotificationPermission = it }
            )

            if (!hasNotificationPermission) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    SideEffect {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            if (uiState.consumableViewEvent.isNotEmpty()) {
                shoppingAlarmScheduler.schedule()
                viewModel.onUiEventConsumed()
            }

            ShoppingApp(
                startDestination = if (preferenceManager.getData(
                        REMEMBER_ME,
                        false
                    )
                ) MainDestinations.PRODUCT_ROUTE
                else MainDestinations.LOGIN_ROUTE
            )
        }
    }
}