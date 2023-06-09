package com.gmail.uli153.workdaymeter.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.gmail.uli153.workdaymeter.navigation.NavigationGraph
import com.gmail.uli153.workdaymeter.ui.viewmodel.MainViewModel
import com.gmail.uli153.workdaymeter.ui.views.BottomBar
import com.gmail.uli153.workdaymeter.ui.views.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = viewModel()
) {
    val navController = rememberNavController()
    val isFilterVisible = remember { mutableStateOf(true) }
    Scaffold(
        topBar = { TopBar(navController, isFilterVisible) },
        bottomBar = { BottomBar(navController) },
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxWidth(1f)
        ) {
            NavigationGraph(navController, padding, mainViewModel, isFilterVisible)
        }
    }
}