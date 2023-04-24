package com.gmail.uli153.workdaymeter.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.gmail.uli153.workdaymeter.navigation.NavigationGraph
import com.gmail.uli153.workdaymeter.ui.viewmodel.MainViewModel
import com.gmail.uli153.workdaymeter.ui.views.BottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainViewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    Scaffold(

        bottomBar = { BottomBar(navController) }
    ) {
        NavigationGraph(navController, mainViewModel)
    }
}