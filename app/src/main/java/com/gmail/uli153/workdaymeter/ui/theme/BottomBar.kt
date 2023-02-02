package com.gmail.uli153.workdaymeter.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.gmail.uli153.workdaymeter.navigation.NavigationItem

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
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = stringResource(id = item.title)) },
                label = { Text(text = stringResource(id = item.title)) },
                selected = index == 0,
                onClick = {}
            )
        }
    }
}