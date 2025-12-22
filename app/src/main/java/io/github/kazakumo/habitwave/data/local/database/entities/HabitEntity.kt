package io.github.kazakumo.habitwave.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// 習慣マスター
@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    // 将来用: 0=固定(Checkのみ), 1=数値目標(ページ数など)
    val type: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
