package com.gmail.uli153.workdaymeter.ui.views

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gmail.uli153.workdaymeter.navigation.NavigationItem
import com.gmail.uli153.workdaymeter.ui.theme.disabled

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Charts,
        NavigationItem.History
    )
    NavigationBar(

    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val onPrimary = MaterialTheme.colorScheme.onPrimary
        items.forEachIndexed { _, item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = stringResource(id = item.title)) },
                label = { Text(text = stringResource(id = item.title)) },
                selected = item.route == currentRoute,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = onPrimary,
                    unselectedIconColor = onPrimary.disabled(),
                    indicatorColor = Color.Transparent
                ),
                onClick = { onItemClicked(item, navController) }
            )
        }
    }
}

private fun onItemClicked(item: NavigationItem, navController: NavHostController) {
    navController.navigate(item.route) {
        navController.graph.startDestinationRoute?.let { start ->
            popUpTo(start) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}