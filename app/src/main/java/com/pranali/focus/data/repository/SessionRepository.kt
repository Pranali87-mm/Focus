package com.pranali.focus.data.repository

import com.pranali.focus.data.local.dao.SessionDao
import com.pranali.focus.data.local.entity.SessionEntity
import com.pranali.focus.util.RhythmState
import kotlinx.coroutines.flow.Flow

class SessionRepository(private val sessionDao: SessionDao) {

    // Updated to accept Rhythm State and Break Skipped
    suspend fun saveSession(
        durationMinutes: Int,
        plannedCount: Int,
        rhythmState: RhythmState,
        breakSkipped: Boolean
    ) {
        val session = SessionEntity(
            durationMinutes = durationMinutes,
            sessionCount = plannedCount,
            completed = true,
            rhythmState = rhythmState.name, // Convert Enum to String for Database
            breakSkipped = breakSkipped
        )
        sessionDao.insertSession(session)
    }

    val allSessions: Flow<List<SessionEntity>> = sessionDao.getAllSessions()
    val totalMinutes: Flow<Int?> = sessionDao.getTotalFocusMinutes()
}