package com.gmail.uli153.workdaymeter.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val state = mainViewModel.state.collectAsState()
    val time = mainViewModel.time.collectAsState()
    val history = mainViewModel.history.collectAsState()
    val historyFilter = mainViewModel.filter.collectAsState()
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(state, time, toggleState = {
                mainViewModel.toggleState()
            })
        }

        composable(NavigationItem.Charts.route) {
            ChartScreen(historyFilter, history, filterSelectedListener = {
                mainViewModel.setHistoryFilter(it)
            })
        }

        composable(NavigationItem.History.route) {
            HistoryScreen(state, historyFilter, history, time, filterSelectedListener = {
                mainViewModel.setHistoryFilter(it)
            })
        }
    }
}