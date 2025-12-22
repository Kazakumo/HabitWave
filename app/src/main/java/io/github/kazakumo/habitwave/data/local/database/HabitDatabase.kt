package io.github.kazakumo.habitwave.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.kazakumo.habitwave.data.local.database.dao.HabitDao
import io.github.kazakumo.habitwave.data.local.database.entities.HabitDetailEntity
import io.github.kazakumo.habitwave.data.local.database.entities.HabitEntity
import io.github.kazakumo.habitwave.data.local.database.entities.HabitRecordEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitDetailEntity::class,
        HabitRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}