package com.example.newapp

import android.app.Application
import androidx.room.Room
import com.example.newapp.data.db.habitDatabase

class MainApplication : Application() {

    companion object{
        lateinit var HabitDatabase: habitDatabase
    }

    override fun onCreate() {
        super.onCreate()
        HabitDatabase = Room.databaseBuilder(
            applicationContext,
            habitDatabase::class.java,
            habitDatabase.NAME
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
}