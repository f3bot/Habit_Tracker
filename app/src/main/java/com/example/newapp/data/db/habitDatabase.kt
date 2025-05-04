package com.example.newapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newapp.data.classes.Habit

@Database(entities = [Habit::class], version = 2)
@TypeConverters(Converters::class)
abstract class habitDatabase : RoomDatabase(){


    companion object {
        const val NAME = "Habit_DB"
    }

    abstract fun getHabitDao() : habitDao
}