package com.gmail.uli153.workdaymeter.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.gmail.uli153.workdaymeter.ui.theme.BottomBar
import com.gmail.uli153.workdaymeter.navigation.NavigationGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(

        bottomBar = { BottomBar(navController) }
    ) {
        NavigationGraph(navController = navController)
    }
}