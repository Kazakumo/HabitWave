package io.github.kazakumo.habitwave.ui.theme.habit.components

import android.graphics.Color.parseColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kazakumo.habitwave.domain.model.Habit
import androidx.core.graphics.toColorInt
import io.github.kazakumo.habitwave.ui.theme.HabitWaveTheme

@Composable
fun HabitItem(
    habit: Habit,
    onToggleToday: (Long) -> Unit,
    onToggleYesterday: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onEdit: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {

    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    val themeColor = Color(habit.colorHex.toColorInt())
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
        Column(modifier = Modifier) {
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
                    Spacer(modifier = Modifier.height(8.dp))
                    HabitHeatMap(
                        history = habit.recentHistory,
                        baseColor = themeColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (habit.streakCount > -1) {
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
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onToggleYesterday(habit.id)
                        },
                        activeColor = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    // 今日ボタン
                    CheckButton(
                        label = "今日",
                        isCompleted = habit.isCompletedToday,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onToggleToday(habit.id)
                        },
                        activeColor = MaterialTheme.colorScheme.primary
                    )
                }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "メニュー")
                    }

                    // ボタン下に表示されるメニュー
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("編集") },
                            onClick = {
                                showMenu = false
                                onEdit(habit)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("削除", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                showMenu = false
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }

        }

    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("習慣の削除") },
            text = { Text("${habit.title} を削除しますか？これまでの記録もすべて消去されます。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete(habit.id)
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("削除")
                }
            },
            modifier = Modifier,
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("キャンセル")
                }
            },
        )
    }
}

@Composable
fun CheckButton(
    label: String,
    isCompleted: Boolean,
    onClick: () -> Unit,
    activeColor: Color,
) {


    // 色のアニメーション設定
    val animatedColor by animateColorAsState(
        targetValue = if (isCompleted) activeColor else Color.LightGray,
        animationSpec = tween(durationMillis = 300),
        label = "ColorAnimation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isCompleted) 1.2f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ScaleAnimation"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick, modifier = Modifier.scale(scale)) {
            Icon(
                imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = animatedColor,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}


@Preview(showBackground = true, name = "習慣カード（継続中）")
@Composable
fun HabitItemPreview() {
    // デバッグ用に「それっぽい」データを作成
    val mockHabit = Habit(
        id = 1,
        title = "朝のヨガ",
        colorHex = "#6750A4",
        isCompletedToday = true,
        isCompletedYesterday = false,
        streakCount = 5,
        recentHistory = listOf(
            true, false, true, true, true, false, true, // 1週目
            true, true, true, false, true, true, true,  // 2週目
            false, true, true, true, true, false, true  // 3週目（今日含む）
        )
    )

    HabitWaveTheme { // あなたのアプリのテーマで囲む
        Box(modifier = Modifier.padding(16.dp)) {
            HabitItem(
                habit = mockHabit,
                onToggleToday = {},
                onToggleYesterday = {},
                onDelete = {},
                onEdit = {}
            )
        }
    }
}