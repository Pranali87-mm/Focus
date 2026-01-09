package com.pranali.focus

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.pranali.focus.ui.screens.breaks.BreakScreen
import com.pranali.focus.ui.screens.focus.FocusScreen
import com.pranali.focus.ui.screens.home.HomeScreen
import com.pranali.focus.ui.screens.insight.ReflectionScreen
import com.pranali.focus.ui.screens.onboarding.OnboardingScreen
import com.pranali.focus.ui.theme.CozyFocusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CozyFocusTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current

    // Quick Logic: Check SharedPrefs for "isFirstRun"
    val prefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    // Default to true if key doesn't exist
    val isFirstRun = remember { prefs.getBoolean("is_first_run", true) }

    // Set initial screen based on First Run check
    var currentScreen by remember {
        mutableStateOf<AppScreen>(if (isFirstRun) AppScreen.Onboarding else AppScreen.Home)
    }

    // Tracks the current session loop data
    var activeSessionData by remember { mutableStateOf<SessionData?>(null) }

    when (val screen = currentScreen) {

        // 0. ONBOARDING (Shows only on first install)
        is AppScreen.Onboarding -> {
            OnboardingScreen(
                onFinishOnboarding = {
                    // Save that we have seen it
                    prefs.edit().putBoolean("is_first_run", false).apply()
                    // Go to Home
                    currentScreen = AppScreen.Home
                }
            )
        }

        // 1. HOME
        is AppScreen.Home -> {
            HomeScreen(
                onStartFocus = { focusMin, breakMin, totalSessions ->
                    activeSessionData = SessionData(
                        focusMinutes = focusMin,
                        breakMinutes = breakMin,
                        totalPlanned = totalSessions,
                        completed = 0
                    )
                    currentScreen = AppScreen.Focus
                }
            )
        }

        // 2. FOCUS
        is AppScreen.Focus -> {
            val data = activeSessionData ?: return HomeScreen { _, _, _ -> }

            FocusScreen(
                focusMinutes = data.focusMinutes,
                onSessionComplete = {
                    val updatedData = data.copy(completed = data.completed + 1)
                    activeSessionData = updatedData

                    // Logic: If all sessions are done, go to Reflection. Otherwise, Break.
                    if (updatedData.completed >= updatedData.totalPlanned) {
                        currentScreen = AppScreen.Reflection
                    } else {
                        currentScreen = AppScreen.Break
                    }
                },
                onStopClicked = {
                    currentScreen = AppScreen.Home
                }
            )
        }

        // 3. BREAK
        is AppScreen.Break -> {
            val data = activeSessionData ?: return HomeScreen { _, _, _ -> }

            BreakScreen(
                breakMinutes = data.breakMinutes,
                onStartNextFocus = {
                    currentScreen = AppScreen.Focus
                }
            )
        }

        // 4. REFLECTION
        is AppScreen.Reflection -> {
            val data = activeSessionData ?: return HomeScreen { _, _, _ -> }

            ReflectionScreen(
                totalSessions = data.totalPlanned,
                onReturnHome = {
                    activeSessionData = null
                    currentScreen = AppScreen.Home
                }
            )
        }
    }
}

// Navigation Destinations
sealed class AppScreen {
    object Onboarding : AppScreen()
    object Home : AppScreen()
    object Focus : AppScreen()
    object Break : AppScreen()
    object Reflection : AppScreen()
}

// Session Data Model
data class SessionData(
    val focusMinutes: Int,
    val breakMinutes: Int,
    val totalPlanned: Int,
    val completed: Int
)