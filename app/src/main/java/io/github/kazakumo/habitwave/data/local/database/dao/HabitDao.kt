package io.github.kazakumo.habitwave.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.github.kazakumo.habitwave.data.local.database.entities.HabitDetailEntity
import io.github.kazakumo.habitwave.data.local.database.entities.HabitEntity
import io.github.kazakumo.habitwave.data.local.database.entities.HabitRecordEntity
import io.github.kazakumo.habitwave.data.local.database.relations.HabitWithDetail
import io.github.kazakumo.habitwave.data.local.database.relations.HabitWithRecords
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    // 書き込み
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitDetail(detail: HabitDetailEntity)

    // 習慣と詳細を同時に作成するトランザクション
    @Transaction
    suspend fun createHabitWithDetail(habit: HabitEntity, detail: HabitDetailEntity) {
        val habitId = insertHabit(habit)
        // 自動生成されたIDを詳細側にセットして保存
        insertHabitDetail(detail.copy(habitId = habitId))
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecord(record: HabitRecordEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    // 読み取り操作

    // アクティブな習慣の一覧を詳細設定とともに取得（Flowでリアルタイム監視）
    @Transaction
    @Query("SELECT * FROM habits WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllHabitsWithDetail(): Flow<List<HabitWithDetail>>

    // ある習慣とそれに紐づく履歴を全取得
    @Transaction
    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitWithRecords(habitId: Long): HabitWithRecords?

    @Transaction
    @Query("SELECT * FROM habits WHERE isActive = 1")
    fun getHabitsWithRecords(): Flow<List<HabitWithRecords>>

    // 特定の日付に達成済みの記録があるか確認する（YC重複防止などに利用）
    @Query("SELECT * FROM habit_records WHERE habitId = :habitId AND targetDate = :targetDate LIMIT 1")
    suspend fun getRecordByDate(habitId: Long, targetDate: Long): HabitRecordEntity?

    // 削除操作

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
}