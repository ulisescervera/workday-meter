package com.gmail.uli153.workdaymeter.navigation

sealed class NavigationItem(val route: String, val icon: Int, val title: Int) {
    object Home: NavigationItem("home", 0, 0)
    object Charts: NavigationItem("charts", 0, 0)
    object History: NavigationItem("history", 0, 0)
}