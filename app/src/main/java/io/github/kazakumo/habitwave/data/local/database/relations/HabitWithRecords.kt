package io.github.kazakumo.habitwave.data.local.database.relations

import io.github.kazakumo.habitwave.data.local.database.entities.HabitEntity
import androidx.room.Embedded
import androidx.room.Relation
import io.github.kazakumo.habitwave.data.local.database.entities.HabitRecordEntity

data class HabitWithRecords(
    @Embedded val habit: HabitEntity, // parent habit
    @Relation(
        parentColumn = "id", // primary key of habitEntity
        entityColumn = "habitId" // foreign key of habitRecordEntity
    )
    val records: List<HabitRecordEntity> // relationally linked records
)
