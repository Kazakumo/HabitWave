package io.github.kazakumo.habitwave.data.repository

import io.github.kazakumo.habitwave.data.local.database.dao.HabitDao
import io.github.kazakumo.habitwave.data.local.database.entities.HabitDetailEntity
import io.github.kazakumo.habitwave.data.local.database.entities.HabitEntity
import io.github.kazakumo.habitwave.data.local.database.entities.HabitRecordEntity
import io.github.kazakumo.habitwave.domain.model.Habit
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HabitRepositoryImpl @Inject constructor(private val habitDao: HabitDao) : HabitRepository {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    override fun getHabits(): Flow<List<Habit>> {
        // DAOからRelationクラス（Entityの塊）を取得
        return habitDao.getAllHabitsWithRecords().map { relations ->
            val todayStr = LocalDate.now().format(dateFormatter)
            val yesterdayStr = LocalDate.now().minusDays(1).format(dateFormatter)

            relations.map { relation ->
                val records = relation.records
                // Entity -> Domain Modelへのマッピング
                Habit(
                    id = relation.habit.id,
                    title = relation.habit.title,
                    colorHex = "#6200EE",
                    isCompletedToday = records.any { it.targetDate.toString() == todayStr },
                    isCompletedYesterday = records.any { it.targetDate.toString() == yesterdayStr },
                    streakCount = calculateStreak(records) // ストリーク計算を分離
                )
            }
        }
    }

    override suspend fun addHabit(title: String, colorHex: String) {
        val habit = HabitEntity(title = title)
        val detail = HabitDetailEntity(habitId = 0, colorHex = colorHex)
        habitDao.createHabitWithDetail(habit, detail)
    }

    override suspend fun toggleCheckIn(habitId: Long, date: LocalDate) {
        val targetDateLong = date.format(dateFormatter).toLong()
        val existing = habitDao.getRecordByDate(habitId, targetDateLong)
        if (existing == null) {
            habitDao.upsertRecord(HabitRecordEntity(habitId = habitId, targetDate = targetDateLong))
        } else {
            // トグル動作：既に記録があれば削除（または未達成フラグを立てる）
            // 現状のEntity設計に合わせてDAOにdeleteRecord等の追加が必要
//            habitDao.deleteHabit(existing)
        }
    }

    override suspend fun deleteHabit(habitId: Long) {
        // 論理削除（isActive = false）の実装
    }

    private fun calculateStreak(records: List<HabitRecordEntity>): Int {
        if (records.isEmpty()) return 0
        // 重複を排除し、日付を降順に並べる
        val sortedDates =
            records.map { LocalDate.parse(it.targetDate.toString(), dateFormatter) }.distinct()
                .sortedDescending()
        val today = LocalDate.now()
        val yesterday = LocalDate.now().minusDays(1)

        // 直近（今日または昨日）に記録があるかチェック
        // どちらにも記録がなければストリークは途切れていると判断
        val latestDate = sortedDates.first()
        if (latestDate != today && latestDate != yesterday) return 0

        // 連続日数をカウント
        val streakCount = sortedDates.zipWithNext().takeWhile { (current, next) ->
            // 現在の日付の一日前が次の要素の日付と一致する間だけ取り出す
            current.minusDays(1) == next
        }.size + 1 // ペア数 + 最初の要素分
        return streakCount
    }
}