package io.github.kazakumo.habitwave.domain.model

/**
 * UI
 */
data class Habit(
    val id: Long,
    val title: String,
    val colorHex: String,
    // 今日達成しているか
    val isCompletedToday: Boolean,
    // 昨日達成しているか
    val isCompletedYesterday: Boolean,
    // 継続日数（ストリーク）
    val streakCount: Int,
    // 単位（将来用）
    val unit: String? = null
)

