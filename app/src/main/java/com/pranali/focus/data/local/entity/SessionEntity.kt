package com.pranali.focus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: Long = System.currentTimeMillis(),
    val durationMinutes: Int,
    val sessionCount: Int, // Planned count
    val completed: Boolean,

    // --- NEW FIELDS ---
    val rhythmState: String, // We store the Enum as a String (e.g. "Balanced")
    val breakSkipped: Boolean
)