package com.pranali.focus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pranali.focus.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    // Insert a new session into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)

    // Get all sessions, sorted by newest first
    // We use Flow<> so the UI updates automatically if data changes
    @Query("SELECT * FROM sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    // Optional: Get total minutes focused (useful for Insights later)
    @Query("SELECT SUM(durationMinutes) FROM sessions")
    fun getTotalFocusMinutes(): Flow<Int?>
}