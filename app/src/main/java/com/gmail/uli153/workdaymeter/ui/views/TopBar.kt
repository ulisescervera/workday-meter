/**
 * Created by Ulises on 31/5/23.
 */
package com.gmail.uli153.workdaymeter.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gmail.uli153.workdaymeter.navigation.NavigationItem
import com.gmail.uli153.workdaymeter.utils.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Charts,
        NavigationItem.History
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = AppDimens.bottomNavigationMarginHorizontal,
            end = AppDimens.bottomNavigationMarginHorizontal,
            top = AppDimens.bottomNavigationMarginBottom
        )
    ) {
        TopAppBar(
            title = {
                val title = items.find { it.route == navBackStackEntry?.destination?.route }?.title?.let { stringResource(id = it) } ?: ""
                Box(modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.Center)) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.bottomNavigationHeight)
                .clip(RoundedCornerShape(AppDimens.bottomNavigationHeight / 2))
        )
    }
}