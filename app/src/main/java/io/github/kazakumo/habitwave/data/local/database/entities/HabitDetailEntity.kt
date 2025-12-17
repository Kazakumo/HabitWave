package io.github.kazakumo.habitwave.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "habit_details",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class HabitDetailEntity(
    @PrimaryKey
    val habitId: Long,
    val colorHex: String = "#6200EE",
    val reminderTime: String? = null, // "09:00", "17:30" など
    // 将来の拡張性: 読書などの総目標数値
    val totalGoalValue: Double? = null,
    val unit: String? = null,// "pages", "km" など
)
