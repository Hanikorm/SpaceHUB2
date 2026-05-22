package com.hanikorm.spacehub2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hanikorm.spacehub2.data.repository.ApodRepository
import com.hanikorm.spacehub2.ui.screens.ApodScreen
import com.hanikorm.spacehub2.ui.screens.HubScreen
import com.hanikorm.spacehub2.ui.screens.MapScreen
import com.hanikorm.spacehub2.ui.screens.ScreenWrapper
import com.hanikorm.spacehub2.ui.viewmodel.ApodViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val repository = remember { ApodRepository(context) }

            NavHost(navController = navController, startDestination = "hub") {

                composable("hub") {
                    HubScreen(
                        onNavigateToApod = { navController.navigate("apod") },
                        onNavigateToEarth = { navController.navigate("earth") },
                        onExit = { finish() },
                    )
                }

                composable("apod") {
                    // ИСПРАВЛЕНО: Передаем ту же фабрику, что и наверху
                    val apodViewModel: ApodViewModel = viewModel(
                        factory = ApodViewModel.Factory(repository)
                    )

                    ScreenWrapper("ГАЛЕРЕЯ", onBack = { navController.popBackStack() }) {
                        ApodScreen(apodViewModel)
                    }
                }

                composable("earth") {
                    ScreenWrapper("ЗЕМЛЯ", onBack = { navController.popBackStack() }) {
                        MapScreen()
                    }
                }
            }
        }
    }
}