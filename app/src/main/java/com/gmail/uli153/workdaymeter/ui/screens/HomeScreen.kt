package com.gmail.uli153.workdaymeter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.State
import com.gmail.uli153.workdaymeter.ui.viewmodel.MainViewModel

enum class ButtonState {
    In, Out, Disabled
}

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    val state: ButtonState = when (val a = mainViewModel.state.collectAsState().value) {
        is UIState.Success -> when (a.data.state) {
            State.StateIn -> ButtonState.In
            State.StateOut -> ButtonState.Out
        }
        else -> ButtonState.Disabled
    }
    ConstraintLayout(modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize()
    ) {
        val button = createRef()
        StateButton(
            state,
            modifier = Modifier
                .constrainAs(button) {
                    start.linkTo(parent.start, 20.dp)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, 20.dp)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .aspectRatio(1f)
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
    val alpha = when (state) {
        ButtonState.In -> 1f
        ButtonState.Out, ButtonState.Disabled -> 0.35f
    }
    Button(onClick = onClick) {
        Icon(
            painter = rememberAsyncImagePainter(R.drawable.ic_circle),
            contentDescription = "",
            tint = color,
            modifier = Modifier.alpha(alpha)
        )
    }
}