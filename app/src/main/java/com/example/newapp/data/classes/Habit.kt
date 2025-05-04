package com.example.newapp.data.classes


import android.graphics.Paint
import java.time.LocalDate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitName: String,
    val habitDescription: String,
    val assignedColorHex: String,
    var isCompletedToday: Boolean,
    var currentStreak: Int,
    val assignedIconResId: Int,
    val dailyCompletions: Int,
    var currentCompletionsToday: Int = 0,
    var lastCompletionDate: LocalDate = LocalDate.now(),
    var streakAdded: Boolean = false,
    var hour: Int,
    var minute: Int
) {
    fun completeHabit(): Boolean {
        val today = LocalDate.now()

        if (today != lastCompletionDate) {
            currentCompletionsToday = 0
            isCompletedToday = false
            streakAdded = false
            lastCompletionDate = today
        }

        if (currentCompletionsToday < dailyCompletions) {
            currentCompletionsToday++
            if (currentCompletionsToday >= dailyCompletions) {
                isCompletedToday = true
                if (!streakAdded) {
                    currentStreak++
                    streakAdded = true
                }
            }
            return true
        }

        return false
    }

}