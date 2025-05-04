package com.example.newapp.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newapp.data.classes.Habit
import com.example.newapp.data.classes.habitViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import com.example.newapp.R
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
@Composable
fun HabitItem(habit: Habit, onDelete: () -> Unit, viewModel: habitViewModel) {
    // Directly calculate the remaining completions based on the habit's current state
    val completionsLeftInt = habit.dailyCompletions - habit.currentCompletionsToday
    val color = Color(android.graphics.Color.parseColor(habit.assignedColorHex))
    val completionsDone = habit.currentCompletionsToday
    val totalCompletions = habit.dailyCompletions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(width = 2.dp, color = color, shape = RoundedCornerShape(8.dp))
    ) {
        // Progress indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 6.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(totalCompletions) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .padding(horizontal = 2.dp)
                        .background(
                            color = if (index < completionsDone) color else Color.LightGray,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row split into 3 parts
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            // Left: Icon (25%)
            Column(
                modifier = Modifier
                    .weight(0.25f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = habit.assignedIconResId),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }

            // Middle: Info (50%)
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 4.dp)
            ) {
                // Name with bottom border
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = Color.Gray,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth
                            )
                        }
                ) {
                    Text(
                        text = habit.habitName,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Text(
                    text = "${habit.hour}:${habit.minute}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Display daily completions text based on the updated habit state
                Text(
                    text = if (completionsLeftInt > 0)
                        "Daily Completions: $completionsLeftInt"
                    else
                        "Task Completed",
                    fontSize = 12.sp
                )
            }

            // Right: Buttons (25%)
            Column(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        // When the button is clicked, complete the habit
                        viewModel.completeHabit(habit)
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_new),
                        contentDescription = "Complete",
                        tint = color,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_cio),
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}



@Composable
fun ListHabits(viewModel: habitViewModel){
    val habitList by viewModel.habitList.observeAsState()
    Column {
        Spacer(modifier = Modifier.size(16.dp))

        habitList?.let {
            LazyColumn(
                content = {
                    itemsIndexed(it) { index: Int, item: Habit ->
                        HabitItem(habit = item, onDelete = {
                            viewModel.deleteHabit(item.id)

                        }
                        ,viewModel = viewModel)
                    }
                }
            )
        }

    }
}