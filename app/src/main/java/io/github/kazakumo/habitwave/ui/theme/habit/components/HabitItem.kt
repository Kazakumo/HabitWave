package io.github.kazakumo.habitwave.ui.theme.habit.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kazakumo.habitwave.domain.model.Habit

@Composable
fun HabitItem(
    habit: Habit,
    onToggleToday: (Long) -> Unit,
    onToggleYesterday: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.5f
            )
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // --- 左側: テキスト情報 ---
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (habit.streakCount > 0) {
                    Text(
                        text = "🔥 ${habit.streakCount}日継続中",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            // --- 右側: 操作ボタン (昨日 | 今日) ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 昨日ボタン（YC）
                CheckButton(
                    label = "昨日",
                    isCompleted = habit.isCompletedYesterday,
                    onClick = { onToggleYesterday(habit.id) },
                    activeColor = Color.Gray
                )
                Spacer(modifier = Modifier.width(12.dp))
                // 今日ボタン
                CheckButton(
                    label = "今日",
                    isCompleted = habit.isCompletedToday,
                    onClick = { onToggleToday(habit.id) },
                    activeColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun CheckButton(
    label: String,
    isCompleted: Boolean,
    onClick: () -> Unit,
    activeColor: Color,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        IconButton(onClick = onClick) {
            Icon(
                imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                contentDescription = label,
                tint = if (isCompleted) activeColor else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
