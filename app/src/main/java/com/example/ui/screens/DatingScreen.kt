package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DatingProfile
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.LightViewModel

@Composable
fun DatingScreen(
    viewModel: LightViewModel,
    modifier: Modifier = Modifier
) {
    val profiles by viewModel.availableDatingProfiles.collectAsState()
    val matches by viewModel.matchedDatingProfiles.collectAsState()
    val isAdultConfirmed by viewModel.isAdultConfirmed.collectAsState()
    val matchCelebration by viewModel.matchCelebration.collectAsState()

    var activeTab by remember { mutableStateOf("Discover") } // "Discover" or "Matches"
    var currentCardIndex by remember { mutableStateOf(0) }
    var reportingProfile by remember { mutableStateOf<DatingProfile?>(null) }
    var isFilterDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("dating_screen")
    ) {
        // Adult 18+ Verification Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFB71C1C)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIGHT DATING • STRICTLY 18+ ADULTS ONLY",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                IconButton(
                    onClick = { isFilterDialogOpen = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Preferences",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Sub Tabs: Discover & Matches
        TabRow(
            selectedTabIndex = if (activeTab == "Discover") 0 else 1,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = LightEmeraldGreen
        ) {
            Tab(
                selected = activeTab == "Discover",
                onClick = { activeTab = "Discover" },
                text = { Text("Discover Cards", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == "Matches",
                onClick = { activeTab = "Matches" },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("My Matches", fontWeight = FontWeight.Bold)
                        if (matches.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Badge(containerColor = LightSunriseOrange) {
                                Text("${matches.size}")
                            }
                        }
                    }
                }
            )
        }

        if (activeTab == "Discover") {
            val available = profiles.drop(currentCardIndex)
            if (available.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = LightSunriseOrange,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No more profiles nearby!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Check back soon or explore your matches to start conversations.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { currentCardIndex = 0 },
                            colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
                        ) {
                            Text("Reset Discovery")
                        }
                    }
                }
            } else {
                val profile = available.first()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag("dating_profile_card"),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Artistic Background Banner representing dating photo
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                LightEmeraldGreen,
                                                Color(0xFF003816),
                                                Color.Black
                                            )
                                        )
                                    )
                            ) {
                                // Large Initials Avatar placeholder for photo
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(120.dp)
                                        .background(Color.White.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = profile.name.take(2).uppercase(),
                                        fontSize = 44.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }

                                // Safety Report / Block button
                                IconButton(
                                    onClick = { reportingProfile = profile },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(12.dp)
                                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                        .testTag("btn_report_dating_profile")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "Report or Block",
                                        tint = Color.White
                                    )
                                }

                                // Verified & Age Badge
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFD32F2F)
                                    ) {
                                        Text(
                                            text = "18+ ADULT",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    if (profile.verified) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = LightSunGold
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "VERIFIED",
                                                    color = Color.Black,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Profile Bio & Details Overlay
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.95f))
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "${profile.name}, ${profile.age}",
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = LightSunGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = profile.location,
                                        fontSize = 13.sp,
                                        color = LightSunGold,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(
                                        imageVector = Icons.Default.Work,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = profile.profession,
                                        fontSize = 13.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = profile.bio,
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Interests chips
                                val tags = profile.interests.split(",").map { it.trim() }
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(tags) { tag ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color.White.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = tag,
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Controls: Pass, Super Like, Match Like
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pass (Cancel)
                        IconButton(
                            onClick = {
                                viewModel.passDatingProfile(profile)
                                currentCardIndex++
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.White, CircleShape)
                                .testTag("btn_dating_pass")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Pass",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        // Super Like (Star)
                        IconButton(
                            onClick = {
                                viewModel.likeDatingProfile(profile, isSuper = true)
                                currentCardIndex++
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .background(LightSunGold, CircleShape)
                                .testTag("btn_dating_superlike")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Super Like",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Like / Match (Heart)
                        IconButton(
                            onClick = {
                                viewModel.likeDatingProfile(profile, isSuper = false)
                                currentCardIndex++
                            },
                            modifier = Modifier
                                .size(64.dp)
                                .background(LightEmeraldGreen, CircleShape)
                                .testTag("btn_dating_like")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Like",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // Matches View
            if (matches.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = LightSunGold,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matches yet!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Keep swiping on Discover cards to find your match.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(matches) { match ->
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .testTag("match_card_${match.id}"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .background(LightEmeraldGreen, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = match.name.take(2).uppercase(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${match.name}, ${match.age}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = match.location,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        // Switch to chat with this match
                                        viewModel.createGroupChat(
                                            name = "Dating: ${match.name}",
                                            subtitle = "Private 18+ conversation",
                                            isIbibio = false
                                        )
                                        viewModel.selectTab(com.example.viewmodel.AppTab.CHAT)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("Chat", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Match Celebration Modal
    if (matchCelebration != null) {
        val matched = matchCelebration!!
        AlertDialog(
            onDismissRequest = { viewModel.dismissMatchCelebration() },
            title = {
                Text(
                    "IT'S A MUTUAL MATCH! 🎉",
                    fontWeight = FontWeight.Black,
                    color = LightEmeraldGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "You and ${matched.name} (${matched.age}) from ${matched.location} liked each other!",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "“BRIGHTER WE SHINE!”",
                        color = LightSunriseOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissMatchCelebration()
                        viewModel.createGroupChat(
                            name = "Dating: ${matched.name}",
                            subtitle = "Private 18+ conversation",
                            isIbibio = false
                        )
                        viewModel.selectTab(com.example.viewmodel.AppTab.CHAT)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen),
                    modifier = Modifier.testTag("btn_start_match_chat")
                ) {
                    Text("Send Message Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissMatchCelebration() }) {
                    Text("Keep Swiping")
                }
            }
        )
    }

    // Report / Block Dialog
    if (reportingProfile != null) {
        val target = reportingProfile!!
        var reportReason by remember { mutableStateOf("Inappropriate content") }

        AlertDialog(
            onDismissRequest = { reportingProfile = null },
            title = {
                Text("Safety: Report or Block", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select reason for reporting ${target.name}:")
                    val reasons = listOf("Inappropriate content", "Underage suspicion (under 18)", "Fake profile", "Harassment / Spam")
                    reasons.forEach { r ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { reportReason = r }
                        ) {
                            RadioButton(
                                selected = reportReason == r,
                                onClick = { reportReason = r }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(r, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.reportDatingProfile(target, reportReason)
                        reportingProfile = null
                        currentCardIndex++
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Report & Block")
                }
            },
            dismissButton = {
                TextButton(onClick = { reportingProfile = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Preferences & Filter Dialog
    if (isFilterDialogOpen) {
        AlertDialog(
            onDismissRequest = { isFilterDialogOpen = false },
            title = { Text("Dating Preferences (18+)", fontWeight = FontWeight.Bold, color = LightEmeraldGreen) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Location Scope: Akwa Ibom & Global Diaspora", fontSize = 13.sp)
                    Text("Age Filter: 18 - 45 years", fontSize = 13.sp)
                    Text("Privacy: Incognito browsing available in settings.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                Button(
                    onClick = { isFilterDialogOpen = false },
                    colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
                ) {
                    Text("Save Preferences")
                }
            }
        )
    }
}
