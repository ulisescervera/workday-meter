package com.gmail.uli153.workdaymeter.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gmail.uli153.workdaymeter.ui.screens.ChartScreen
import com.gmail.uli153.workdaymeter.ui.screens.HistoryScreen
import com.gmail.uli153.workdaymeter.ui.screens.HomeScreen
import com.gmail.uli153.workdaymeter.ui.viewmodel.MainViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(mainViewModel)
        }

        composable(NavigationItem.Charts.route) {
            ChartScreen()
        }

        composable(NavigationItem.History.route) {
            HistoryScreen()
        }
    }
}