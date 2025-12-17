package io.github.kazakumo.habitwave.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "habit_records",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("habitId")] // NOTE: 全文検索で使うプロパティ。room > 2.1.0の場合は代わりに@FTS3 or 4のアノテーションのほうが早いかも。その場合パッケージも追加かな？
)
data class HabitRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    // 達成対象日（yyyymmdd形式のLong. 例: 20230101）
    val targetDate: Long,
    // 実際に操作した時間（YC判定用）
    val recordedAt: Long = System.currentTimeMillis(),
    // 将来の拡張性: 読書ならページ数、散歩ならkm. 初期は1.0(完了)固定.
    val value: Double = 1.0,
)
