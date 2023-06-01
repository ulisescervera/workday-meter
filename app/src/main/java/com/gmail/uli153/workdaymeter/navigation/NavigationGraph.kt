package com.gmail.uli153.workdaymeter.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gmail.uli153.workdaymeter.ui.screens.HistoryScreen
import com.gmail.uli153.workdaymeter.ui.screens.HomeScreen
import com.gmail.uli153.workdaymeter.ui.screens.chart.ChartScreen
import com.gmail.uli153.workdaymeter.ui.viewmodel.DateFilter
import com.gmail.uli153.workdaymeter.ui.viewmodel.MainViewModel
import org.threeten.bp.DayOfWeek

@Composable
fun NavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    mainViewModel: MainViewModel = viewModel(),
    isFilterVisible: State<Boolean>
) {
    val state = mainViewModel.state.collectAsState()
    val time = mainViewModel.time.collectAsState()
    val history = mainViewModel.history.collectAsState()
    val historyFilter = mainViewModel.dateFilter.collectAsState()
    val selectedDays = mainViewModel.dayFilter.collectAsState()

    val filterSelectedListener: (DateFilter) -> Unit = {
        mainViewModel.setDateFilter(it)
    }
    val dayFilterListener: (Set<DayOfWeek>) -> Unit = {
        mainViewModel.setDayFilter(it)
    }

    val navigateHomeListener = {
        navController.navigate(NavigationItem.Home.route)
    }

    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(padding, state, time, toggleState = {
                mainViewModel.toggleState()
            })
        }

        composable(NavigationItem.Charts.route) {
            ChartScreen(padding, historyFilter, history, selectedDays, isFilterVisible, filterSelectedListener, dayFilterListener)
        }

        composable(NavigationItem.History.route) {
            HistoryScreen(padding, state, historyFilter, history, time, selectedDays, isFilterVisible, navigateHomeListener, filterSelectedListener, dayFilterListener)
        }
    }
}