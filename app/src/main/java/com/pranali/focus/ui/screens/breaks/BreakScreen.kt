package com.pranali.focus.ui.screens.breaks

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pranali.focus.ui.theme.*

@Composable
fun BreakScreen(
    breakMinutes: Int,
    viewModel: BreakViewModel = viewModel(),
    onStartNextFocus: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = breakMinutes) {
        viewModel.startBreak(breakMinutes)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream) // Lighter background for breaks
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top status
        Text(
            text = "Rest & Recover",
            style = MaterialTheme.typography.labelLarge,
            color = MutedMoss, // Greenish for relaxation
            modifier = Modifier.padding(top = 16.dp)
        )

        // Center Character & Timer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Character Visualization (Relaxed Mode)
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(SoftBeige)
                    .border(4.dp, MutedMoss.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Character\n(Sipping Tea)",
                    textAlign = TextAlign.Center,
                    color = MutedMoss,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = uiState.formattedTime,
                style = MaterialTheme.typography.displayLarge,
                color = MutedMoss
            )

            Text(
                text = "Take a deep breath...",
                style = MaterialTheme.typography.bodyLarge,
                color = MutedGreyBrown
            )
        }

        // Bottom Controls
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onStartNextFocus,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MutedMoss,
                    contentColor = WarmCream
                )
            ) {
                Text(
                    text = "Start Next Focus",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            TextButton(onClick = onStartNextFocus) {
                Text("Skip Break", color = MutedGreyBrown)
            }
        }
    }
}