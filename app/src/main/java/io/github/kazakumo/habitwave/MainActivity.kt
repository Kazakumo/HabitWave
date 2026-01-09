package io.github.kazakumo.habitwave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import dagger.hilt.android.AndroidEntryPoint
import io.github.kazakumo.habitwave.ui.theme.HabitWaveTheme
import io.github.kazakumo.habitwave.ui.theme.habit.HabitScreen
import io.github.kazakumo.habitwave.ui.theme.habit.HabitViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitWaveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: HabitViewModel = hiltViewModel()
                    HabitScreen(viewModel = viewModel)
                }
            }
        }
    }
}