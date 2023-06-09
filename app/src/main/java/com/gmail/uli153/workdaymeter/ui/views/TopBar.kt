/**
 * Created by Ulises on 31/5/23.
 */
package com.gmail.uli153.workdaymeter.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.navigation.NavigationItem
import com.gmail.uli153.workdaymeter.utils.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, isFilterVisible: MutableState<Boolean>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val items = NavigationItem.values()
    val onFilterButtonClicked = {
        isFilterVisible.value = !isFilterVisible.value
    }
    val title = items.find { it.route == navBackStackEntry?.destination?.route }?.title?.let { stringResource(id = it) } ?: ""
    val showFilterButton = navBackStackEntry?.destination?.route != NavigationItem.Home.route

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = AppDimens.navigationBarHorizontalMargin,
            end = AppDimens.navigationBarHorizontalMargin,
            top = AppDimens.bottomNavigationMarginBottom
        )
    ) {
        TopAppBar(
            title = {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (text, filterIcon) = createRefs()
                    val iconWidth = 32.dp
                    val iconMargin = 8.dp
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.constrainAs(text) {
                            start.linkTo(parent.start, iconWidth + iconMargin)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end, iconWidth + iconMargin)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                    )
                    AnimatedVisibility(visible = showFilterButton,
                        modifier = Modifier.padding(iconMargin)
                        .clickable(onClick = onFilterButtonClicked)
                        .constrainAs(filterIcon) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        }
                    ) {
                        Icon(painter = rememberAsyncImagePainter(R.drawable.ic_filter),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(iconWidth)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.navigationBarHeight)
                .clip(RoundedCornerShape(AppDimens.navigationBarHeight / 2))
        )
    }
}