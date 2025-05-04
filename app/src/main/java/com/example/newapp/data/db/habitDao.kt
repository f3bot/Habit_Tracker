package com.example.newapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.newapp.data.classes.Habit

@Dao
interface habitDao {

    @Query("SELECT * FROM HABIT")
    fun getAllHabit() : LiveData<List<Habit>>

    @Insert
    fun addHabit(habit: Habit)

    @Query("DELETE FROM Habit WHERE id = :habitId")
    fun deleteTodo(habitId: Int)

    @Update
    fun updateHabit(habit: Habit)
}