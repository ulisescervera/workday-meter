package com.gmail.uli153.workdaymeter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.ui.views.ChronometerButton

enum class ButtonState {
    In, Out, Disabled
}

@Composable
fun HomeScreen(
    padding: PaddingValues,
    state: State<UIState<Record>>,
    time: State<Long>,
    toggleState: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(
            start = 20.dp,
            top = padding.calculateTopPadding() + 20.dp,
            end = 20.dp,
            bottom = padding.calculateBottomPadding()
        )
    ) {
        ChronometerButton(state, time, toggleState, modifier = Modifier.fillMaxWidth())
    }
}



@Preview
@Composable
fun HomeScreen_Preview() {
    val state : State<UIState<Record>> = remember { mutableStateOf(UIState.Loading) }
    val time : State<Long> = remember { mutableStateOf(0L) }
    HomeScreen(PaddingValues(0.dp) ,state, time, {})
}

