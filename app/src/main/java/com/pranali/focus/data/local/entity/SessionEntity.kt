package com.pranali.focus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    // Auto-generating ID handles unique keys for us
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Store timestamp to sort history later
    val date: Long = System.currentTimeMillis(),

    val durationMinutes: Int,
    val sessionCount: Int, // How many sessions were in the set (e.g. 4)
    val completed: Boolean
)