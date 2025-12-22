package io.github.kazakumo.habitwave.data.local.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import io.github.kazakumo.habitwave.data.local.database.entities.HabitDetailEntity
import io.github.kazakumo.habitwave.data.local.database.entities.HabitEntity

data class HabitWithDetail(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val detail: HabitDetailEntity?
)
