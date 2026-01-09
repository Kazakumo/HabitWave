package io.github.kazakumo.habitwave.ui.theme.habit.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("#6750A4") }

    val colors = listOf(
        "#6750A4", // Purple
        "#0061A4", // Blue
        "#006A60", // Teal
        "#7D5800", // Orange
        "#B3261E"  // Red
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新しい習慣") },
        text = {
            Column {
                Text("何を始めますか？", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("例: 読書・散歩など") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("テーマカラー", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colors.forEach { colorHex ->
                        val isSelected = selectedColor == colorHex
                        Surface(
                            onClick = { selectedColor = colorHex },
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = Color(android.graphics.Color.parseColor(colorHex)),
                            border = if (isSelected) BorderStroke(
                                2.dp,
                                MaterialTheme.colorScheme.outline
                            ) else null,
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, selectedColor)
                        onDismiss()
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}

@Composable
fun HabitFormDialog(
    initialTitle: String = "",
    initialColor: String = "#6750A4",
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var selectedColor by remember { mutableStateOf(initialColor) }
    val colors = listOf("#6750A4", "#0061A4", "#006A60", "#7D5800", "#B3261E")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialTitle.isEmpty()) "新しい習慣" else "習慣の編集") },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, selectedColor)
                        onDismiss()
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("習慣の名前") },
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("テーマカラー", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colors.forEach { colorHex ->
                        val isSelected = selectedColor == colorHex
                        Surface(
                            onClick = { selectedColor = colorHex },
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = Color(android.graphics.Color.parseColor(colorHex)),
                            border = if (isSelected) BorderStroke(
                                2.dp,
                                MaterialTheme.colorScheme.outline
                            ) else null,
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}