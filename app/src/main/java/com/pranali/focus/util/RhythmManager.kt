package com.pranali.focus.util

import java.util.Calendar

enum class RhythmState {
    Balanced,   // The ideal state. Healthy flow.
    Strained,   // Pushing too hard, skipping breaks.
    Exhausted   // Late night, too many sessions. Burnout risk.
}

object RhythmManager {

    /**
     * Calculates the user's current state based on observation.
     * * @param completedSessions Total sessions done in this set so far.
     * @param plannedSessions How many they intended to do.
     * @param breakSkipped Did they skip the last break?
     * @return The calculated RhythmState.
     */
    fun calculateState(
        completedSessions: Int,
        plannedSessions: Int,
        breakSkipped: Boolean
    ): RhythmState {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        // 1. Check for EXHAUSTED
        // Condition: Late night usage (Midnight to 5 AM) OR significantly over-planning
        if (currentHour in 0..4) {
            return RhythmState.Exhausted
        }
        if (completedSessions > plannedSessions + 2) {
            return RhythmState.Exhausted
        }

        // 2. Check for STRAINED
        // Condition: Skipped break OR went past the plan
        if (breakSkipped) {
            return RhythmState.Strained
        }
        if (completedSessions > plannedSessions) {
            return RhythmState.Strained
        }

        // 3. Default to BALANCED
        return RhythmState.Balanced
    }

    /**
     * Returns a gentle reflection message based on the state.
     */
    fun getReflectionMessage(state: RhythmState): String {
        return when (state) {
            RhythmState.Balanced -> "Your rhythm is steady. You are moving forward without force."
            RhythmState.Strained -> "You are pushing hard. Remember, rest is part of the work."
            RhythmState.Exhausted -> "You have done enough. True focus requires recovery."
        }
    }
}