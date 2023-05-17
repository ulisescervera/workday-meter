package com.gmail.uli153.workdaymeter.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(
            start = 0.dp,
            top = 0.dp,
            end = 0.dp,
            bottom = padding.calculateBottomPadding()
        )
    ) {
        ChronometerButton(state, time, toggleState, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun ChronometerButton(
    state: State<UIState<Record>>,
    time: State<Long>,
    toggleState: () -> Unit,
    modifier: Modifier
) {
    val planetSize = 16.dp
    val planetSize2 = (2 * planetSize.value).dp
    val distanceToSun = 8.dp
    val buttonState: ButtonState = state.value.toButtonState()


//    val lastAngle = remember { mutableStateOf(0f) }
//    val infiniteTransition = rememberInfiniteTransition()
//    val angle = infiniteTransition.animateFloat(
//        initialValue = 0F,
//        targetValue = 360F,
//        animationSpec = infiniteRepeatable(
//            animation = tween(8000, easing = LinearEasing)
//        )
//    )
//    lastAngle.value = angle.value


    val animate = buttonState == ButtonState.In
    val lastAngle = remember { mutableStateOf(0f) }
    val angle = remember(animate) { Animatable(lastAngle.value) }
    LaunchedEffect(animate) {
        if (animate) {
            angle.animateTo(
                360f + lastAngle.value,
                animationSpec = infiniteRepeatable(animation = tween(8000, easing = LinearEasing), repeatMode = RepeatMode.Restart)
            ) {
                lastAngle.value = value // store the anim value
            }
        }
    }

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

    ConstraintLayout(modifier = modifier) {
        val (sun, planet1, planet2, planet3, planet4, planet5, planet6, planet7, planet8, planet9, planet10, planet11, planet12) = createRefs()
        val sunSize = remember { mutableStateOf(Dp(0f)) }
        val localDensity = LocalDensity.current
        Button(onClick = toggleState,
            modifier = Modifier
                .aspectRatio(1f)
                .padding(planetSize2 + distanceToSun)
                .constrainAs(sun) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .onGloballyPositioned {
                    sunSize.value = with(localDensity) { it.size.width.toDp() }
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

        val distance = (sunSize.value / 2) + planetSize + distanceToSun
        val planets = listOf(planet1, planet2, planet3, planet4, planet5, planet6, planet7, planet8, planet9, planet10, planet11, planet12)
        planets.forEachIndexed { index, constrainedLayoutReference ->
            Planet(planetSize, constraints = Modifier.constrainAs(constrainedLayoutReference) {
                circular(sun, lastAngle.value + (360f * (index.toFloat() / planets.size)), distance)
            })
        }
    }
}

@Composable
fun Planet(
    planetSize: Dp,
    constraints: Modifier
) {
    Box(modifier = constraints
        .width(planetSize)
        .aspectRatio(1f)
        .drawBehind {
            drawCircle(
                color = Color.Magenta,
                radius = this.size.maxDimension
            )
        }
    )
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