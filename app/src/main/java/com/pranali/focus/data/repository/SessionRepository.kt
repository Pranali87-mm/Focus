package com.pranali.focus.data.repository

import com.pranali.focus.data.local.dao.SessionDao
import com.pranali.focus.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

class SessionRepository(private val sessionDao: SessionDao) {

    // Simple function to create the entity and save it
    suspend fun saveSession(durationMinutes: Int, plannedCount: Int) {
        val session = SessionEntity(
            durationMinutes = durationMinutes,
            sessionCount = plannedCount,
            completed = true
        )
        sessionDao.insertSession(session)
    }

    // Expose the data as flows
    val allSessions: Flow<List<SessionEntity>> = sessionDao.getAllSessions()
    val totalMinutes: Flow<Int?> = sessionDao.getTotalFocusMinutes()
}