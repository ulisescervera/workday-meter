package com.gmail.uli153.workdaymeter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.ui.theme.disabled
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime

enum class ButtonState {
    In, Out, Disabled
}

@Composable
fun HomeScreen(state: State<UIState<Record>>, time: State<Long>, toggleState: () -> Unit) {
    val buttonState: ButtonState = state.value.toButtonState()
    val icon: Int
    val color: Color
    val alpha: Float
    when (buttonState) {
        ButtonState.In -> {
            icon = R.drawable.ic_clock_out
            color = MaterialTheme.colorScheme.onPrimary
            alpha = 1f
        }
        ButtonState.Out -> {
            icon = R.drawable.ic_clock_in
            color = MaterialTheme.colorScheme.onPrimary.disabled()
            alpha = 1f
        }
        ButtonState.Disabled -> {
            icon = R.drawable.ic_clock_in
            color = MaterialTheme.colorScheme.error.disabled()
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
            .fillMaxSize()
        ) {
            Button(onClick = toggleState,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize(1f)
            ) {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize(0.75f)
                ) {
                    Text(text = time.value.formattedTime, color = Color.White, fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(30.dp))
                    Icon(painter = rememberAsyncImagePainter(icon),
                        contentDescription = "",
                        tint = color,
                        modifier = Modifier
                            .alpha(alpha)
                            .fillMaxSize(0.75f)
                    )
                }
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

private fun UIState<Record>.toButtonState(): ButtonState {
    return when (this) {
        is UIState.Success -> when (this.data.state) {
            MeterState.StateIn -> ButtonState.In
            MeterState.StateOut -> ButtonState.Out
        }
        else -> ButtonState.Disabled
    }
}