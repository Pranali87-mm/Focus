package com.pranali.focus

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import com.pranali.focus.data.local.AppDatabase
import com.pranali.focus.data.repository.SessionRepository
import com.pranali.focus.ui.screens.breaks.BreakScreen
import com.pranali.focus.ui.screens.focus.FocusScreen
import com.pranali.focus.ui.screens.home.HomeScreen
import com.pranali.focus.ui.screens.insight.ReflectionScreen
import com.pranali.focus.ui.screens.onboarding.OnboardingScreen
import com.pranali.focus.ui.screens.reports.ReportsScreen
import com.pranali.focus.ui.screens.rewards.RewardsScreen
import com.pranali.focus.ui.theme.*
import com.pranali.focus.util.NotificationHelper
import com.pranali.focus.util.RhythmManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Note: UNINSTALL the app if you get a crash here due to database schema changes
        val database = AppDatabase.getDatabase(this)
        val repository = SessionRepository(database.sessionDao())

        setContent {
            CozyFocusTheme {
                AppRoot(repository)
            }
        }
    }
}

@Composable
fun AppRoot(repository: SessionRepository) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    var isFirstRun by remember { mutableStateOf(prefs.getBoolean("is_first_run", true)) }

    // --- PERMISSION REQUEST LOGIC (Android 13+) ---
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* Permission logic if needed */ }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    // ----------------------------------------------

    if (isFirstRun) {
        OnboardingScreen(onFinishOnboarding = {
            prefs.edit().putBoolean("is_first_run", false).apply()
            isFirstRun = false
        })
    } else {
        MainAppContent(repository)
    }
}

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
                MainTab.Rewards -> RewardsScreen(repository)
                MainTab.Reports -> ReportsScreen(repository)
            }
        }
    }
}

@Composable
fun TimerFlow(repository: SessionRepository) {
    var currentScreen by remember { mutableStateOf<TimerScreen>(TimerScreen.Home) }
    var activeSessionData by remember { mutableStateOf<SessionData?>(null) }
    val scope = rememberCoroutineScope()

    // Initialize Notification Helper
    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }

    when (val screen = currentScreen) {
        is TimerScreen.Home -> {
            HomeScreen(
                onStartFocus = { focusMin, breakMin, totalSessions ->
                    activeSessionData = SessionData(
                        focusMinutes = focusMin,
                        breakMinutes = breakMin,
                        totalPlanned = totalSessions,
                        completed = 0,
                        breakSkipped = false // Default
                    )
                    currentScreen = TimerScreen.Focus
                }
            )
        }
        is TimerScreen.Focus -> {
            val data = activeSessionData ?: return HomeScreen { _, _, _ -> }

            FocusScreen(
                focusMinutes = data.focusMinutes,
                onSessionComplete = {
                    // --- 1. SEND NOTIFICATION ---
                    notificationHelper.showCompletionNotification(
                        "Session Complete",
                        "Great work! Take a breath."
                    )

                    // 2. CALCULATE STATE
                    val calculatedState = RhythmManager.calculateState(
                        completedSessions = data.completed + 1,
                        plannedSessions = data.totalPlanned,
                        breakSkipped = data.breakSkipped
                    )

                    // 3. SAVE TO DB (With State)
                    scope.launch {
                        repository.saveSession(
                            durationMinutes = data.focusMinutes,
                            plannedCount = data.totalPlanned,
                            rhythmState = calculatedState,
                            breakSkipped = data.breakSkipped
                        )
                    }

                    // 4. UPDATE DATA
                    val updatedData = data.copy(
                        completed = data.completed + 1,
                        breakSkipped = false
                    )
                    activeSessionData = updatedData

                    // 5. NAVIGATE
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
                onStartNextFocus = {
                    activeSessionData = data.copy(breakSkipped = false)
                    currentScreen = TimerScreen.Focus
                }
            )
        }
        is TimerScreen.Reflection -> {
            val data = activeSessionData ?: return HomeScreen { _, _, _ -> }

            val finalState = RhythmManager.calculateState(
                data.completed, data.totalPlanned, data.breakSkipped
            )

            ReflectionScreen(
                totalSessions = data.totalPlanned,
                rhythmState = finalState,
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
    val completed: Int,
    val breakSkipped: Boolean
)