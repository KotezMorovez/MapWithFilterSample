package com.example.filterpointsonthemap.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.filterpointsonthemap.presentation.feature.filter.FilterScreen
import com.example.filterpointsonthemap.presentation.feature.map.ui.MapScreen

enum class MapScreens {
    MAP,
    FILTER
}

@Composable
fun MapApp(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = MapScreens.MAP.name,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(
            route = MapScreens.MAP.name,
            ) {
            MapScreen(
                onFilterButtonClickListener = {
                    navController.navigate(
                        route = MapScreens.FILTER.name
                    )
                }
            )
        }

        composable(route = MapScreens.FILTER.name) {
            FilterScreen(
                onBackClickListener = {
                    navController.popBackStack()
                },
                onApplyClickListener = {
                    navController.popBackStack()
                }
            )
        }
    }
}