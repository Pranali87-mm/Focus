package com.pranali.focus.ui.screens.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Holds the state of the active session
data class FocusUiState(
    val timeLeftSeconds: Long = 25 * 60,
    val initialDurationSeconds: Long = 25 * 60,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
    val characterState: String = "Focusing"
) {
    val formattedTime: String
        get() {
            val minutes = timeLeftSeconds / 60
            val seconds = timeLeftSeconds % 60
            return "%02d:%02d".format(minutes, seconds)
        }
}

class FocusViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState: StateFlow<FocusUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    // Called when screen loads
    fun startSession(minutes: Int) {
        val totalSeconds = minutes * 60L

        // FIX: Only skip if it is ALREADY RUNNING for the same duration.
        // This protects against screen rotation resetting the timer,
        // but ensures it auto-starts if it was paused/stopped/new.
        if (_uiState.value.isRunning && _uiState.value.initialDurationSeconds == totalSeconds) {
            return
        }

        // Force start
        _uiState.update {
            it.copy(
                timeLeftSeconds = totalSeconds,
                initialDurationSeconds = totalSeconds,
                isRunning = true,
                isFinished = false
            )
        }
        startTimerCountdown()
    }

    fun toggleTimer() {
        if (_uiState.value.isRunning) {
            pauseTimer()
        } else {
            startTimerCountdown()
        }
    }

    private fun startTimerCountdown() {
        timerJob?.cancel()

        _uiState.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeftSeconds > 0) {
                delay(1000L)
                _uiState.update {
                    it.copy(timeLeftSeconds = it.timeLeftSeconds - 1)
                }
            }
            _uiState.update { it.copy(isFinished = true, isRunning = false) }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }

    fun stopSession() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(isRunning = false, timeLeftSeconds = 0)
        }
    }
}