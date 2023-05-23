/**
 * Created by Ulises on 23/5/23.
 */
package com.gmail.uli153.workdaymeter.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun DayFilterSelector(
    selectedDays: List<DayOfWeek>,
    modifier: Modifier,
    onDayListChanged: (List<DayOfWeek>) -> Unit
) {
    val days = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
    LazyRow(
        modifier = modifier,
        userScrollEnabled = true,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { day ->
            val isSelected = selectedDays.contains(day)
            val color = MaterialTheme.colorScheme.secondary
            val alpha = if (isSelected) 1f else 0.4f
            Box(modifier = Modifier
                .size(40.dp)
                .background(color, shape = CircleShape)
                .alpha(alpha)
                .clickable {
                    val newSelectedDays = selectedDays.toMutableList()
                    if (newSelectedDays.contains(day)) newSelectedDays.remove(day) else newSelectedDays.add(day)
                    onDayListChanged(newSelectedDays)
                }
            ) {
                val letter = day.getDisplayName(TextStyle.NARROW_STANDALONE, Locale.getDefault())
                Text(text = letter, modifier = Modifier.fillMaxSize().padding(4.dp).align(Alignment.Center))
            }
        }
    }
}