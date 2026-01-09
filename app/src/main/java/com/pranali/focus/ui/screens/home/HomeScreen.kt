package com.pranali.focus.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pranali.focus.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onStartFocus: (focusMin: Int, breakMin: Int, sessionCount: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // --- Top Section: Character ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ready to focus?",
                style = MaterialTheme.typography.titleMedium,
                color = MutedGreyBrown
            )

            Spacer(modifier = Modifier.height(32.dp))

            // PLACEHOLDER FOR BODY DOUBLE CHARACTER
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(WarmCream)
                    .border(2.dp, BurntOrange.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Character\n(Idle State)",
                    textAlign = TextAlign.Center,
                    color = BurntOrange,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // --- Middle Section: Configuration ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            // 1. Timer Presets
            ConfigSection(title = "Focus / Break") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PresetCard(
                        focus = 25, breakTime = 5,
                        isSelected = uiState.selectedPreset.focusMinutes == 25,
                        onClick = { viewModel.selectPreset(25, 5) },
                        modifier = Modifier.weight(1f)
                    )
                    PresetCard(
                        focus = 45, breakTime = 10,
                        isSelected = uiState.selectedPreset.focusMinutes == 45,
                        onClick = { viewModel.selectPreset(45, 10) },
                        modifier = Modifier.weight(1f)
                    )
                    PresetCard(
                        focus = 50, breakTime = 10,
                        isSelected = uiState.selectedPreset.focusMinutes == 50,
                        onClick = { viewModel.selectPreset(50, 10) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 2. Session Planner (UPDATED to Stepper)
            ConfigSection(title = "Planned Sessions") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus Button
                    IconButton(
                        onClick = { viewModel.decrementSessions() },
                        modifier = Modifier
                            .size(48.dp)
                            .background(WarmCream, CircleShape)
                            .border(1.dp, InactiveGrey, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease sessions",
                            tint = DarkWarmBrown
                        )
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    // Count Display
                    Text(
                        text = "${uiState.plannedSessions}",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = DarkWarmBrown
                    )

                    Spacer(modifier = Modifier.width(32.dp))

                    // Plus Button
                    IconButton(
                        onClick = { viewModel.incrementSessions() },
                        modifier = Modifier
                            .size(48.dp)
                            .background(BurntOrange, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase sessions",
                            tint = WarmCream
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // --- Bottom Section: CTA ---
        Button(
            onClick = {
                onStartFocus(
                    uiState.selectedPreset.focusMinutes,
                    uiState.selectedPreset.breakMinutes,
                    uiState.plannedSessions
                )
            },
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
                text = "Start Focus",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// --- Reusable Components ---

@Composable
fun ConfigSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MutedGreyBrown,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        content()
    }
}

@Composable
fun PresetCard(
    focus: Int,
    breakTime: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) BurntOrange else WarmCream
    val textColor = if (isSelected) WarmCream else DarkWarmBrown
    val borderColor = if (isSelected) BurntOrange else InactiveGrey

    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$focus",
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
            Text(
                text = "/$breakTime",
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.8f)
            )
        }
    }
}