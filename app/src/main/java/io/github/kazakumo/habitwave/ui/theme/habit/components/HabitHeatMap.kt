package io.github.kazakumo.habitwave.ui.theme.habit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HabitHeatMap(history: List<Boolean>, baseColor: Color, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = 7,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        history.forEach { isCompleted ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = if (isCompleted) baseColor else baseColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }

}