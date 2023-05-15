package com.gmail.uli153.workdaymeter.navigation

import com.gmail.uli153.workdaymeter.R

sealed class NavigationItem(val route: String, val icon: Int, val title: Int) {
    object Home: NavigationItem("home", R.drawable.ic_chronometer, R.string.title_home)
    object Charts: NavigationItem("charts", R.drawable.ic_chart, R.string.title_charts)
    object History: NavigationItem("history", R.drawable.ic_history, R.string.title_history)
}