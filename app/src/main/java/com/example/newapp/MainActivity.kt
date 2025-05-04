package com.example.newapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.newapp.data.classes.habitViewModel
import com.example.newapp.ui.components.*
import com.example.newapp.ui.theme.NewAppTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with notifications
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val habitViewModel = ViewModelProvider(this)[habitViewModel::class.java]

        // Check and request permission for POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            NewAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    HabitScreen(innerPadding, habitViewModel)
                }
            }
        }
    }
}

@Composable
fun HabitScreen(innerPadding: PaddingValues, habitViewModel: habitViewModel) {
    var isDescriptionMenuVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (isDescriptionMenuVisible) {
            descriptionMenu(
                onClose = { isDescriptionMenuVisible = false },
                viewModel = habitViewModel
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                createHabitBtn(onClick = {
                    isDescriptionMenuVisible = true
                })
                ListHabits(habitViewModel)
            }
        }
    }
}
