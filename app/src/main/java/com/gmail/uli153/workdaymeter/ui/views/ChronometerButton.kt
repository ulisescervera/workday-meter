/**
 * Created by Ulises on 22/5/23.
 */
package com.gmail.uli153.workdaymeter.ui.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.ui.screens.ButtonState
import com.gmail.uli153.workdaymeter.ui.theme.disabled
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime

@Composable
fun ChronometerButton(
    state: State<UIState<Record>>,
    time: State<Long>,
    toggleState: () -> Unit,
    modifier: Modifier
) {
    val planetsCount = 24
    val planetSize = 12.dp
    val planetSize2 = (2 * planetSize.value).dp
    val distanceToSun = 6.dp
    val sunSize = remember { mutableStateOf(Dp(0f)) }
    val localDensity = LocalDensity.current
    val planetColor = MaterialTheme.colorScheme.secondary
    val distance = (sunSize.value / 2) + planetSize/2
    val buttonState: ButtonState = state.value.toButtonState()
    val animate = buttonState == ButtonState.In
    val lastAngle = remember { mutableStateOf(0f) }
    val angle = remember(animate) { Animatable(lastAngle.value) }
    LaunchedEffect(animate) {
        if (animate) {
            angle.animateTo(
                360f + lastAngle.value,
                animationSpec = infiniteRepeatable(animation = tween(15000, easing = LinearEasing), repeatMode = RepeatMode.Restart)
            ) {
                lastAngle.value = value // store the anim value
            }
        }
    }

    val lottieComposition = rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_anim_particles))
    val progress = animateLottieCompositionAsState(
        composition = lottieComposition.value,
        iterations = LottieConstants.IterateForever,
        isPlaying = buttonState == ButtonState.In,
        speed = 1f,
        restartOnPlay = false
    )

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
        val sun = createRef()
        val planets = (0 until planetsCount).map { createRef() }

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
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LottieAnimation(composition = lottieComposition.value, progress = progress.value, modifier = Modifier.fillMaxSize())
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
        }

        planets.forEachIndexed { index, constrainedLayoutReference ->
            val offset = (index.toFloat() / planetsCount) * 360f
            val angle = lastAngle.value + offset
            val dis = distance + distanceToSun
            Planet(planetSize, planetColor, constraints = Modifier.constrainAs(constrainedLayoutReference) {
                circular(sun, angle, dis)
            })
        }
    }
}

@Composable
private fun Planet(
    planetSize: Dp,
    planetColor: Color,
    constraints: Modifier
) {
    Box(modifier = constraints
        .size(planetSize)
        .background(planetColor, shape = CircleShape)
    )
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