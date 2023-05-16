package com.gmail.uli153.workdaymeter.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
fun HomeScreen(
    padding: PaddingValues,
    state: State<UIState<Record>>,
    time: State<Long>,
    toggleState: () -> Unit
) {
    val buttonState: ButtonState = state.value.toButtonState()
    val icon: Int
    val color: Color
    val alpha: Float
    val infiniteTransition = rememberInfiniteTransition()
    val angle = infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing)
        )
    )
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
    ConstraintLayout(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = padding.calculateBottomPadding())
        .fillMaxSize()
    ) {
        val (button, img1) = createRefs()
        val buttonSize = remember { mutableStateOf(0) }

        Button(onClick = toggleState,
            modifier = Modifier
                .constrainAs(button) {
                    top.linkTo(parent.top, margin = 40.dp)
                    start.linkTo(parent.start, margin = 40.dp)
                    end.linkTo(parent.end, margin = 40.dp)
                }
                .aspectRatio(1f)
                .padding(4.dp)
                .fillMaxSize()
                .onGloballyPositioned {
                    buttonSize.value = it.size.width
                },
            shape = CircleShape
        ) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(0.75f)
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

        Box(modifier = Modifier
            .width(24.dp)
            .aspectRatio(1f)
            .constrainAs(img1) {
                circular(button, if (buttonState == ButtonState.In) angle.value else 90f, (buttonSize.value/4).dp)
            }
            .drawBehind {
                drawCircle(
                    color = Color.Magenta,
                    radius = this.size.maxDimension
                )
            }
        )
    }
}

@Preview
@Composable
fun HomeScreen_Preview() {
    val state : State<UIState<Record>> = remember { mutableStateOf(UIState.Loading) }
    val time : State<Long> = remember { mutableStateOf(0L) }
    HomeScreen(PaddingValues(0.dp) ,state, time, {})
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