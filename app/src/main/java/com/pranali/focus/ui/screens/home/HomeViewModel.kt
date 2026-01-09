package com.pranali.focus.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Simple data class for the Presets
data class TimerPreset(
    val focusMinutes: Int,
    val breakMinutes: Int
) {
    override fun toString(): String = "$focusMinutes / $breakMinutes"
}

// State holder for the Home Screen
data class HomeUiState(
    val selectedPreset: TimerPreset = TimerPreset(25, 5),
    val plannedSessions: Int = 2, // Default start
    val characterState: String = "Idle"
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun selectPreset(focus: Int, breakTime: Int) {
        _uiState.update { it.copy(selectedPreset = TimerPreset(focus, breakTime)) }
    }

    // --- NEW: Stepper Logic ---

    fun incrementSessions() {
        _uiState.update {
            // Cap at 12 sessions to prevent accidental huge numbers
            val newCount = (it.plannedSessions + 1).coerceAtMost(12)
            it.copy(plannedSessions = newCount)
        }
    }

    fun decrementSessions() {
        _uiState.update {
            // Minimum 1 session
            val newCount = (it.plannedSessions - 1).coerceAtLeast(1)
            it.copy(plannedSessions = newCount)
        }
    }
}