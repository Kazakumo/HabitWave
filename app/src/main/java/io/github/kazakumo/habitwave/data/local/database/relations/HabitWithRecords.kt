package io.github.kazakumo.habitwave.data.local.database.relations

import io.github.kazakumo.habitwave.data.local.database.entities.HabitEntity
import androidx.room.Embedded
import androidx.room.Relation
import io.github.kazakumo.habitwave.data.local.database.entities.HabitDetailEntity
import io.github.kazakumo.habitwave.data.local.database.entities.HabitRecordEntity

data class HabitWithRecords(
    @Embedded val habit: HabitEntity, // parent habit

    // NOTE: 1:1のリレーション(詳細情報)
    @Relation(
        parentColumn = "id", // primary key of habitEntity
        entityColumn = "habitId" // foreign key of habitDetailEntity
    )
    val detail: HabitDetailEntity?, // relationally linked detail


    // NOTE: 1:Nのリレーション(達成記録リスト)
    @Relation(
        parentColumn = "id", // primary key of habitEntity
        entityColumn = "habitId" // foreign key of habitRecordEntity
    )
    val records: List<HabitRecordEntity> // relationally linked records
)
