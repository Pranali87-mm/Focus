package com.pranali.focus.ui.screens.reports

import android.text.format.DateUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pranali.focus.data.local.entity.SessionEntity
import com.pranali.focus.data.repository.SessionRepository
import com.pranali.focus.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReportsScreen(
    repository: SessionRepository
) {
    // 1. Fetch ALL sessions (The raw history)
    val sessions by repository.allSessions.collectAsState(initial = emptyList())

    // 2. Logic: Calculate TODAY'S totals
    val todayStats = remember(sessions) {
        val startOfToday = getStartOfDay()
        val todaySessions = sessions.filter { it.date >= startOfToday }
        val minutes = todaySessions.sumOf { it.durationMinutes }
        val count = todaySessions.size
        Pair(minutes, count)
    }

    // 3. Logic: Group history by Date String (e.g., "Today", "Yesterday", "Jan 05")
    val groupedSessions = remember(sessions) {
        sessions.groupBy { getRelativeDateHeader(it.date) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBeige)
            .padding(24.dp)
    ) {
        Text(
            text = "Your Rhythm",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkWarmBrown
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Summary Card (TODAY ONLY) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BurntOrange),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Today's Focus",
                        style = MaterialTheme.typography.labelLarge,
                        color = WarmCream.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${todayStats.first} mins",
                        style = MaterialTheme.typography.displayMedium,
                        color = WarmCream
                    )
                }

                // Small badge for session count
                Surface(
                    color = WarmCream.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${todayStats.second} Sessions",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = WarmCream,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Grouped History List ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            groupedSessions.forEach { (header, sessionList) ->

                // The Date Header (Sticky or normal)
                stickyHeader {
                    DateHeader(text = header)
                }

                // The Items for that date
                items(sessionList) { session ->
                    SessionHistoryItem(
                        time = session.date,
                        duration = session.durationMinutes,
                        count = session.sessionCount
                    )
                }
            }
        }
    }
}

@Composable
fun DateHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MutedGreyBrown,
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftBeige) // Matches background to look transparent
            .padding(vertical = 8.dp),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SessionHistoryItem(time: Long, duration: Int, count: Int) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    Card(
        colors = CardDefaults.cardColors(containerColor = WarmCream),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${count} Sessions Set",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkWarmBrown
                )
                Text(
                    text = timeFormat.format(Date(time)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MutedGreyBrown.copy(alpha = 0.7f)
                )
            }
            Text(
                text = "+$duration min",
                style = MaterialTheme.typography.titleMedium,
                color = MutedMoss
            )
        }
    }
}

// --- Helpers for Date Calculation ---

private fun getStartOfDay(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

private fun getRelativeDateHeader(dateMillis: Long): String {
    val now = System.currentTimeMillis()
    return when {
        DateUtils.isToday(dateMillis) -> "Today"
        DateUtils.isToday(dateMillis + DateUtils.DAY_IN_MILLIS) -> "Yesterday"
        else -> {
            val format = SimpleDateFormat("MMMM dd", Locale.getDefault())
            format.format(Date(dateMillis))
        }
    }
}