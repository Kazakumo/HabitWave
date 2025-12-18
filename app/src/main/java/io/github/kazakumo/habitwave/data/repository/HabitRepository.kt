package io.github.kazakumo.habitwave.data.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    // 全ての有効な習慣を、現在のステータス付きで取得
    fun getHabits(): Flow<List<Habit>>

    // 新しい習慣の作成
    suspend fun addHabit(title: String, colorHex: String)

    // チェックイン（今日または機能のリカバリ）
    suspend fun toggleCheckIn(habitId: Long, date: LocalDate)

    // 習慣の削除
    suspend fun deleteHabit(habitId: Long)
}