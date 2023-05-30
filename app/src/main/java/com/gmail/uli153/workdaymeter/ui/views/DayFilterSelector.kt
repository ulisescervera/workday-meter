/**
 * Created by Ulises on 23/5/23.
 */
package com.gmail.uli153.workdaymeter.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun DayFilterSelector(
    selectedDays: State<List<DayOfWeek>>,
    modifier: Modifier,
    onDayListChanged: (List<DayOfWeek>) -> Unit
) {
    val days = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
    val selectedDays = selectedDays.value
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        for (day in days) {
            val isSelected = selectedDays.contains(day)
            val alpha = if (isSelected) 1f else 0.4f
            val color = MaterialTheme.colorScheme.secondary.copy(alpha = alpha)
            val onClick = {
                val newSelectedDays = selectedDays.toMutableList()
                if (newSelectedDays.contains(day)) newSelectedDays.remove(day) else newSelectedDays.add(day)
                onDayListChanged(newSelectedDays)
            }
            ElevatedCard(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .clickable { onClick() },
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                val letter = day.getDisplayName(TextStyle.NARROW_STANDALONE, Locale.getDefault()).uppercase(Locale.getDefault())
                Text(text = letter,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
@Preview
fun DayFilterSelector_Preview() {
    val selectedDays = remember { mutableStateOf(listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)) }
    DayFilterSelector(selectedDays, Modifier, {})
}