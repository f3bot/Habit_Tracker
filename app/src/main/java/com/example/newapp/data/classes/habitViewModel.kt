package com.example.newapp.data.classes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapp.MainApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class habitViewModel : ViewModel() {

    val habitDao = MainApplication.HabitDatabase.getHabitDao()

    val habitList: LiveData<List<Habit>> = habitDao.getAllHabit()

    fun addHabit(habitName: String, colorID: String, description: String, iconResId: Int, completionsDay: Int, selectedHour: Int, selectedMinute: Int){
        viewModelScope.launch (Dispatchers.IO){
            habitDao.addHabit(Habit(0, habitName, description, colorID, isCompletedToday = false, currentStreak = 0, iconResId , dailyCompletions = completionsDay, hour = selectedHour, minute = selectedMinute ))
        }
    }

    fun deleteHabit(id: Int){
        viewModelScope.launch (Dispatchers.IO){
            habitDao.deleteTodo(id)
        }

    }

    fun completeHabit(habit: Habit) {
        val updatedHabit = habit.copy(currentCompletionsToday = habit.currentCompletionsToday + 1)
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.updateHabit(updatedHabit)
        }
    }

}
