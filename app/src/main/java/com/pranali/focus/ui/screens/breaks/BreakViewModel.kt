package com.pranali.focus.ui.screens.breaks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BreakUiState(
    val timeLeftSeconds: Long = 300,
    val initialDurationSeconds: Long = 300, // Tracks original duration for reset logic
    val isRunning: Boolean = false,
    val isFinished: Boolean = false
) {
    val formattedTime: String
        get() = "%02d:%02d".format(timeLeftSeconds / 60, timeLeftSeconds % 60)
}

class BreakViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BreakUiState())
    val uiState: StateFlow<BreakUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startBreak(minutes: Int) {
        val totalSeconds = minutes * 60L

        // Rotation Guard: If it's already running for the same duration,
        // don't restart it (this happens on screen rotation).
        if (_uiState.value.isRunning && _uiState.value.initialDurationSeconds == totalSeconds) {
            return
        }

        // Initialize state and force start
        _uiState.update {
            it.copy(
                timeLeftSeconds = totalSeconds,
                initialDurationSeconds = totalSeconds,
                isRunning = true,
                isFinished = false
            )
        }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        // Ensure UI knows we are running
        _uiState.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeftSeconds > 0) {
                delay(1000L)
                _uiState.update { it.copy(timeLeftSeconds = it.timeLeftSeconds - 1) }
            }
            // Timer Finished
            _uiState.update { it.copy(isFinished = true, isRunning = false) }
        }
    }

    fun stopBreak() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }
}