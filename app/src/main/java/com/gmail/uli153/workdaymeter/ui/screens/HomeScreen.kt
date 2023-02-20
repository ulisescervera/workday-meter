package com.gmail.uli153.workdaymeter.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.Data
import com.gmail.uli153.workdaymeter.domain.models.State
import com.gmail.uli153.workdaymeter.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    val state: ButtonState = when (val a = mainViewModel.state.collectAsState().value) {
        is Data.Success -> when (a.data.state) {
            State.StateIn -> ButtonState.In
            State.StateOut -> ButtonState.Out
        }
        else -> ButtonState.Disabled
    }
    ConstraintLayout {
        val button = createRef()
        StateButton(
            state,
            modifier = Modifier.constrainAs(button) {
                start.linkTo(parent.start, 20.dp)
                top.linkTo(parent.top)
                end.linkTo(parent.end, 20.dp)
                bottom.linkTo(parent.bottom)
            }.aspectRatio(1f)
        ) { mainViewModel.toggleState() }
    }
}

@Composable
fun StateButton(state: ButtonState, modifier: Modifier, onClick: () -> Unit) {
    val color = when (state) {
        ButtonState.In -> MaterialTheme.colorScheme.secondary
        ButtonState.Out -> MaterialTheme.colorScheme.secondary
        ButtonState.Disabled -> MaterialTheme.colorScheme.error
    }
    Button(onClick = onClick) {
        Image(
            painter = painterResource(id = R.drawable.ic_circle),
            contentDescription = "",
            colorFilter = ColorFilter.tint(color),
            alpha = when (state) {
                ButtonState.In -> 1f
                ButtonState.Out, ButtonState.Disabled -> 0.35f
            }
        )
    }
}

enum class ButtonState {
    In, Out, Disabled
}