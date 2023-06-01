package com.gmail.uli153.workdaymeter.navigation

import com.gmail.uli153.workdaymeter.R

enum class NavigationItem(val route: String, val icon: Int, val title: Int) {
    Home("home", R.drawable.ic_chronometer, R.string.title_home),
    Charts("charts", R.drawable.ic_chart, R.string.title_charts),
    History("history", R.drawable.ic_history, R.string.title_history)
}
