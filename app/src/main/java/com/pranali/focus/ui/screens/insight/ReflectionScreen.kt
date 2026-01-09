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
import com.pranali.focus.ui.theme.*

@Composable
fun ReflectionScreen(
    totalSessions: Int,
    onReturnHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBeige) // Gentle background
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- Top: Header ---
        Text(
            text = "Session Complete",
            style = MaterialTheme.typography.labelLarge,
            color = MutedGreyBrown,
            modifier = Modifier.padding(top = 16.dp)
        )

        // --- Center: Reward/Insight ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            // Character Visualization (Celebration/Content State)
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(WarmCream)
                    .border(4.dp, MutedMoss.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Character\n(Smiling/Content)",
                    textAlign = TextAlign.Center,
                    color = MutedMoss,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Rhythm Balanced",
                style = MaterialTheme.typography.headlineMedium,
                color = DarkWarmBrown
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "You completed $totalSessions focus sessions.",
                style = MaterialTheme.typography.bodyLarge,
                color = MutedGreyBrown
            )

            Text(
                text = "Take a moment to appreciate your effort.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedGreyBrown,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
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
                containerColor = BurntOrange,
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