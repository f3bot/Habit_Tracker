package com.example.newapp.ui.components
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.newapp.R
import com.example.newapp.data.classes.habitViewModel
import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.platform.LocalContext
import com.example.newapp.data.classes.ReminderScheduler
import java.util.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon



@Composable
fun descriptionMenu(onClose: () -> Unit, viewModel: habitViewModel) {
    var nameText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var completionsInt by remember { mutableStateOf("") }
    var selectedColorHexID by remember { mutableStateOf(vibrantColors[0]) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    var selectedTime by remember { mutableStateOf("") }
    var selectedHour_ by remember { mutableStateOf(currentHour) }
    var selectedMinute_ by remember { mutableStateOf(currentMinute) }
    val iconOptions = listOf(
        R.drawable.icon1,
        R.drawable.icon2,
        R.drawable.icon3,
        R.drawable.icon4,
        R.drawable.icon5,
        R.drawable.icon6,
        R.drawable.icon7,
        R.drawable.icon8,
        R.drawable.icon9,
        R.drawable.icon10,
        R.drawable.icon11,
        R.drawable.icon12
    )

    var selectedIcon by remember { mutableStateOf(iconOptions[0]) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Close button (X) in the top-right corner
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { onClose() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White   // You can change the color if needed
                )
            }
        }

        // Habit Name
        Text("Habit Name", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = nameText,
            onValueChange = { nameText = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time picker section
        Text("Select Time (24hr format)", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Select Time for Reminder", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                TimePickerDialog(
                    context,
                    { _: TimePicker, hour: Int, minute: Int ->
                        selectedHour_ = hour
                        selectedMinute_ = minute
                        selectedTime = String.format("%02d:%02d", hour, minute)
                    },
                    selectedHour_,
                    selectedMinute_,
                    true // 24-hour format
                ).show()
            }) {
                Text(if (selectedTime.isEmpty()) "Pick Time" else "Selected: $selectedTime")
            }
        }

        // Completions field
        Text("Completions in a day (INT ONLY)", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = completionsInt,
            onValueChange = { completionsInt = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Color selection section
        Text("Pick a Color", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Color selection boxes
        for (i in 0 until vibrantColors.size step 6) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                vibrantColors.subList(i, (i + 6).coerceAtMost(vibrantColors.size)).forEach { colorHex ->
                    ColorBox(
                        color = Color(android.graphics.Color.parseColor(colorHex)),
                        isSelected = (colorHex == selectedColorHexID)
                    ) {
                        selectedColorHexID = colorHex
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Icon selection section
        Text("Pick an Icon", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Icon selection boxes
        for (i in 0 until iconOptions.size step 6) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                iconOptions.subList(i, (i + 6).coerceAtMost(iconOptions.size)).forEach { icon ->
                    IconBox(drawableResId = icon, isSelected = (icon == selectedIcon)) {
                        selectedIcon = icon
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Create habit button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                Log.v("Habit Name", "$nameText")
                Log.v("Habit description", "$descriptionText")
                val completions = completionsInt.toIntOrNull() ?: 0

                viewModel.addHabit(
                    habitName = nameText,
                    colorID = selectedColorHexID,
                    description = descriptionText,
                    iconResId = selectedIcon,
                    completionsDay = completions,
                    selectedHour = selectedHour_,
                    selectedMinute = selectedMinute_
                )

                ReminderScheduler.scheduleReminder(
                    context = context,
                    hour = selectedHour_,
                    minute = selectedMinute_,
                    title = nameText,
                    message = "Time to complete your habit: $nameText!"
                )

                onClose() // Close the menu after creating the habit
            }) {
                Text("Create Habit")
            }
        }
    }
}


@Composable
fun IconBox(drawableResId: Int, isSelected: Boolean, onClick: () -> Unit) {
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.LightGray,
        animationSpec = tween(durationMillis = 300)
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    val painter = painterResource(id = drawableResId)

    Box(
        modifier = Modifier
            .size(56.dp)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Transparent)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painter,
            contentDescription = null
        )
    }
}


@Composable
fun ColorBox(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.LightGray,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 300)
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
    )
}

val vibrantColors = listOf(
    "#E91E63", // Pink
    "#FF5722", // Deep Orange
    "#FFC107", // Amber
    "#4CAF50", // Green
    "#00BCD4", // Cyan
    "#3F51B5", // Indigo
    "#9C27B0", // Purple
    "#CDDC39", // Lime
    "#2196F3", // Blue
    "#673AB7", // Deep Purple
    "#FF9800", // Orange
    "#8BC34A"  // Light Green
)

