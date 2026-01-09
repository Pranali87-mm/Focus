package com.pranali.focus.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pranali.focus.ui.theme.*

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBeige)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Philosophy / Intro
        Text(
            text = "Welcome to Focus",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkWarmBrown
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Card for the Philosophy
        Card(
            colors = CardDefaults.cardColors(containerColor = WarmCream),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Presence, not pressure.",
                    style = MaterialTheme.typography.titleMedium,
                    color = BurntOrange
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This app helps you focus without burning out. \n\n" +
                            "We observe your rhythm—balancing work with rest—so you can stay consistent without the crash.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MutedGreyBrown,
                    textAlign = TextAlign.Center,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Begin Button
        Button(
            onClick = onFinishOnboarding,
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
                text = "Begin Journey",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}