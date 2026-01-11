package com.pranali.focus.ui.screens.insight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pranali.focus.ui.theme.*
import com.pranali.focus.util.RhythmManager
import com.pranali.focus.util.RhythmState

@Composable
fun ReflectionScreen(
    totalSessions: Int,
    rhythmState: RhythmState, // New Input
    onReturnHome: () -> Unit
) {
    // Get the advice text based on the state
    val reflectionMessage = RhythmManager.getReflectionMessage(rhythmState)

    // Determine visual style based on state
    val (stateTitle, stateIcon, stateColor) = when (rhythmState) {
        RhythmState.Balanced -> Triple("Rhythm: Balanced", "🌿", MutedMoss)
        RhythmState.Strained -> Triple("Rhythm: Strained", "🔥", BurntOrange)
        RhythmState.Exhausted -> Triple("Rhythm: Exhausted", "🌙", MutedGreyBrown)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBeige)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- Top: Header ---
        Text(
            text = "Cycle Complete",
            style = MaterialTheme.typography.labelLarge,
            color = MutedGreyBrown,
            modifier = Modifier.padding(top = 16.dp)
        )

        // --- Center: Insight ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            // State Visualization
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(WarmCream)
                    .border(4.dp, stateColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stateIcon,
                    fontSize = 64.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stateTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = stateColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "You completed $totalSessions sessions.",
                style = MaterialTheme.typography.bodyLarge,
                color = DarkWarmBrown,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // The Dynamic Advice
            Text(
                text = reflectionMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedGreyBrown,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // --- Bottom: Finish ---
        Button(
            onClick = onReturnHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = stateColor, // Button matches the state color
                contentColor = WarmCream
            )
        ) {
            Text(
                text = "Finish & Return Home",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}