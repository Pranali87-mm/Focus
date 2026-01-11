package com.pranali.focus

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.pranali.focus.data.local.AppDatabase
import com.pranali.focus.data.repository.SessionRepository
import com.pranali.focus.ui.screens.breaks.BreakScreen
import com.pranali.focus.ui.screens.focus.FocusScreen
import com.pranali.focus.ui.screens.home.HomeScreen
import com.pranali.focus.ui.screens.insight.ReflectionScreen
import com.pranali.focus.ui.screens.onboarding.OnboardingScreen
import com.pranali.focus.ui.screens.reports.ReportsScreen
import com.pranali.focus.ui.screens.rewards.RewardsScreen
import com.pranali.focus.ui.theme.BurntOrange
import com.pranali.focus.ui.theme.CozyFocusTheme
import com.pranali.focus.ui.theme.MutedGreyBrown
import com.pranali.focus.ui.theme.SoftBeige
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = SessionRepository(database.sessionDao())

        setContent {
            CozyFocusTheme {
                AppRoot(repository)
            }
        }
    }
}

// 1. Root Logic
@Composable
fun AppRoot(repository: SessionRepository) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    var isFirstRun by remember { mutableStateOf(prefs.getBoolean("is_first_run", true)) }

    if (isFirstRun) {
        OnboardingScreen(
            onFinishOnboarding = {
                prefs.edit().putBoolean("is_first_run", false).apply()
                isFirstRun = false
            }
        )
    } else {
        MainAppContent(repository)
    }
}

// 2. Main App with Bottom Navigation
@Composable
fun MainAppContent(repository: SessionRepository) {
    var currentTab by remember { mutableStateOf(MainTab.Timer) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = SoftBeige,
                contentColor = MutedGreyBrown
            ) {
                NavigationBarItem(
                    selected = currentTab == MainTab.Timer,
                    onClick = { currentTab = MainTab.Timer },
                    icon = { Icon(Icons.Default.Timer, contentDescription = "Timer") },
                    label = { Text("Timer") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BurntOrange,
                        selectedTextColor = BurntOrange,
                        indicatorColor = SoftBeige
                    )
                )
                NavigationBarItem(
                    selected = currentTab == MainTab.Rewards,
                    onClick = { currentTab = MainTab.Rewards },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Rewards") },
                    label = { Text("Rewards") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BurntOrange,
                        selectedTextColor = BurntOrange,
                        indicatorColor = SoftBeige
                    )
                )
                NavigationBarItem(
                    selected = currentTab == MainTab.Reports,
                    onClick = { currentTab = MainTab.Reports },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Reports") },
                    label = { Text("Reports") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BurntOrange,
                        selectedTextColor = BurntOrange,
                        indicatorColor = SoftBeige
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                MainTab.Timer -> TimerFlow(repository)
                // UPDATE: Now passing repository to RewardsScreen
                MainTab.Rewards -> RewardsScreen(repository)
                MainTab.Reports -> ReportsScreen(repository)
            }
        }
    }
}

// 3. The Timer Flow
@Composable
fun TimerFlow(repository: SessionRepository) {
    var currentScreen by remember { mutableStateOf<TimerScreen>(TimerScreen.Home) }
    var activeSessionData by remember { mutableStateOf<SessionData?>(null) }
    val scope = rememberCoroutineScope()

    when (val screen = currentScreen) {
        is TimerScreen.Home -> {
            HomeScreen(
                onStartFocus = { focusMin, breakMin, totalSessions ->
                    activeSessionData = SessionData(focusMin, breakMin, totalSessions, 0)
                    currentScreen = TimerScreen.Focus
                }
            )
        }
        is TimerScreen.Focus -> {
            val data = activeSessionData ?: return HomeScreen { _, _, _ -> }
            FocusScreen(
                focusMinutes = data.focusMinutes,
                onSessionComplete = {
                    scope.launch {
                        repository.saveSession(data.focusMinutes, data.totalPlanned)
                    }
                    val updatedData = data.copy(completed = data.completed + 1)
                    activeSessionData = updatedData
                    if (updatedData.completed >= updatedData.totalPlanned) {
                        currentScreen = TimerScreen.Reflection
                    } else {
                        currentScreen = TimerScreen.Break
                    }
                },
                onStopClicked = { currentScreen = TimerScreen.Home }
            )
        }
        is TimerScreen.Break -> {
            val data = activeSessionData ?: return HomeScreen { _, _, _ -> }
            BreakScreen(
                breakMinutes = data.breakMinutes,
                onStartNextFocus = { currentScreen = TimerScreen.Focus }
            )
        }
        is TimerScreen.Reflection -> {
            val data = activeSessionData ?: return HomeScreen { _, _, _ -> }
            ReflectionScreen(
                totalSessions = data.totalPlanned,
                onReturnHome = {
                    activeSessionData = null
                    currentScreen = TimerScreen.Home
                }
            )
        }
    }
}

enum class MainTab { Timer, Rewards, Reports }

sealed class TimerScreen {
    object Home : TimerScreen()
    object Focus : TimerScreen()
    object Break : TimerScreen()
    object Reflection : TimerScreen()
}

data class SessionData(
    val focusMinutes: Int,
    val breakMinutes: Int,
    val totalPlanned: Int,
    val completed: Int
)