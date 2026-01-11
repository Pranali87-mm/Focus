package com.pranali.focus.ui.screens.rewards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pranali.focus.data.repository.SessionRepository
import com.pranali.focus.ui.theme.*

// Data Model for a Reward
data class RewardItem(
    val id: Int,
    val title: String,
    val requiredSessions: Int // Unlock threshold
)

@Composable
fun RewardsScreen(repository: SessionRepository) {
    // 1. Fetch Progress
    val sessions by repository.allSessions.collectAsState(initial = emptyList())
    val totalSessionsCompleted = sessions.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBeige)
    ) {
        // --- Header ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "My Journey",
                style = MaterialTheme.typography.headlineMedium,
                color = DarkWarmBrown
            )
            Text(
                text = "Moments Collected: $totalSessionsCompleted / 30",
                style = MaterialTheme.typography.labelLarge,
                color = BurntOrange
            )
        }

        // --- Grid Content ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            // FIX: Added weight(1f) to allow the grid to scroll within the Column
            modifier = Modifier.weight(1f)
        ) {
            items(MasterRewardList) { reward ->
                RewardCard(
                    reward = reward,
                    isUnlocked = totalSessionsCompleted >= reward.requiredSessions
                )
            }
        }
    }
}

@Composable
fun RewardCard(reward: RewardItem, isUnlocked: Boolean) {
    val context = LocalContext.current

    // Dynamic Resource Lookup: Finds "reward_1", "reward_2", etc.
    val imageResId = remember(reward.id) {
        context.resources.getIdentifier(
            "reward_${reward.id}",
            "drawable",
            context.packageName
        )
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) WarmCream else Color(0xFFEBE5DE)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Check if image exists
            if (imageResId != 0) {
                // Show Image (With different styles for Locked vs Unlocked)
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = reward.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        // If locked, make it transparent/grayscale
                        .alpha(if (isUnlocked) 1f else 0.3f),
                    // Optional: Grayscale filter for locked items
                    colorFilter = if (!isUnlocked) ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }) else null
                )
            } else {
                // Fallback if file is missing in drawable folder
                if (isUnlocked) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = BurntOrange)
                        Text("Add reward_${reward.id}.png", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Overlays
            if (isUnlocked) {
                // Title Overlay for Unlocked Items
                if (imageResId != 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = reward.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1
                        )
                    }
                }
            } else {
                // Lock Icon Overlay for Locked Items
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.1f)) // Slight dim
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = DarkWarmBrown,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Unlock at\nSession ${reward.requiredSessions}",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkWarmBrown,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// --- MASTER DATA LIST (30 Character Moments) ---
val MasterRewardList = listOf(
    RewardItem(1, "Studying at a messy desk", 1),
    RewardItem(2, "Asleep on books", 2),
    RewardItem(3, "Coffee + dark circles", 3),
    RewardItem(4, "Hoodie on rainy day", 4),
    RewardItem(5, "Headphones, zoning out", 5),
    RewardItem(6, "Celebrating tiny win", 6),
    RewardItem(7, "Staring at night sky", 7),
    RewardItem(8, "Surrounded by floating thoughts", 8),
    RewardItem(9, "On bed with open laptop", 9),
    RewardItem(10, "Procrastinating on phone", 10),
    RewardItem(11, "Stressed before deadline", 11),
    RewardItem(12, "Calm during golden hour", 12),
    RewardItem(13, "Journaling", 13),
    RewardItem(14, "Walking in autumn leaves", 14),
    RewardItem(15, "In library chaos", 15),
    RewardItem(16, "Sticky notes all over wall", 16),
    RewardItem(17, "Stretching during break", 17),
    RewardItem(18, "Late-night grind scene", 18),
    RewardItem(19, "In café with books", 19),
    RewardItem(20, "Lying on floor, tired", 20),
    RewardItem(21, "Smiling at progress", 21),
    RewardItem(22, "Under blanket with laptop", 22),
    RewardItem(23, "In exam hall mood", 23),
    RewardItem(24, "Daydreaming in class", 24),
    RewardItem(25, "Reorganizing desk", 25),
    RewardItem(26, "Rainy bus ride", 26),
    RewardItem(27, "Sunset balcony scene", 27),
    RewardItem(28, "Overwhelmed but trying", 28),
    RewardItem(29, "Peaceful morning setup", 29),
    RewardItem(30, "Looking at “Done for today” screen", 30)
)