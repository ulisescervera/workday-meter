package com.gmail.uli153.workdaymeter.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.ui.viewmodel.MainViewModel
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime
import java.util.*
import kotlin.concurrent.fixedRateTimer

enum class ButtonState {
    In, Out, Disabled
}

@Composable
fun HomeScreen(state: State<UIState<Record>>, time: State<Long>, toggleState: () -> Unit) {
    val buttonState: ButtonState = when (val value = state.value) {
        is UIState.Success -> when (value.data.state) {
            MeterState.StateIn -> ButtonState.In
            MeterState.StateOut -> ButtonState.Out
        }
        else -> ButtonState.Disabled
    }
    val icon: Int
    val color: Color
    val alpha: Float
    when (buttonState) {
        ButtonState.In -> {
            icon = R.drawable.ic_clock_out
            color = MaterialTheme.colorScheme.primary
            alpha = 1f
        }
        ButtonState.Out -> {
            icon = R.drawable.ic_clock_in
            color = MaterialTheme.colorScheme.secondary
            alpha = 1f
        }
        ButtonState.Disabled -> {
            icon = R.drawable.ic_clock_in
            color = MaterialTheme.colorScheme.error
            alpha = 1f
        }
    }
    Box(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(horizontal = 20.dp)
        .fillMaxSize()
    ) {
        Box(modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()) {
            Button(
                onClick = toggleState,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize(1f)
            ) {
                Text(text = time.value.formattedTime)
                Icon(
                    painter = rememberAsyncImagePainter(icon),
                    contentDescription = "",
                    tint = color,
                    modifier = Modifier
                        .alpha(alpha)
                        .fillMaxSize(1f)
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreen_Preview() {
    val state : State<UIState<Record>> = remember { mutableStateOf(UIState.Loading) }
    val time : State<Long> = remember { mutableStateOf(0L) }
    HomeScreen(state, time, {})
}