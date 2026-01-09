package com.pranali.focus.ui.screens.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pranali.focus.ui.theme.*

@Composable
fun FocusScreen(
    focusMinutes: Int, // Passed from Home Screen
    viewModel: FocusViewModel = viewModel(),
    onSessionComplete: () -> Unit,
    onStopClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Initialize timer when screen enters
    LaunchedEffect(key1 = focusMinutes) {
        viewModel.startSession(focusMinutes)
    }

    // Monitor for finish state
    LaunchedEffect(key1 = uiState.isFinished) {
        if (uiState.isFinished) {
            onSessionComplete()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // We use a slightly different background or overlay if desired,
            // but sticking to SoftBeige keeps it consistent and calm.
            .background(SoftBeige)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // --- Top: Status ---
        Text(
            text = "Focus Session",
            style = MaterialTheme.typography.labelLarge,
            color = MutedGreyBrown,
            modifier = Modifier.padding(top = 16.dp)
        )

        // --- Center: Character & Timer ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            // Character Visualization (Focus Mode)
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(WarmCream)
                    .border(4.dp, BurntOrange.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for 'Focusing' animation
                Text(
                    text = "Character\n(Writing...)",
                    textAlign = TextAlign.Center,
                    color = DarkWarmBrown,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // The Massive Timer
            Text(
                text = uiState.formattedTime,
                style = MaterialTheme.typography.displayLarge,
                color = DarkWarmBrown
            )

            Text(
                text = if (uiState.isRunning) "Keep going..." else "Paused",
                style = MaterialTheme.typography.bodyMedium,
                color = if (uiState.isRunning) MutedMoss else MutedGreyBrown
            )
        }

        // --- Bottom: Controls ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Pause / Resume Button
            Button(
                onClick = { viewModel.toggleTimer() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BurntOrange,
                    contentColor = WarmCream
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = if (uiState.isRunning) "Pause" else "Resume Focus",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Stop / Give Up (Subtle)
            TextButton(
                onClick = onStopClicked
            ) {
                Text(
                    text = "End Session Early",
                    color = MutedGreyBrown,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}