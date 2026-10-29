package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.AppTab
import com.example.viewmodel.LightViewModel

@Composable
fun ProfileScreen(
    viewModel: LightViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var isEditProfileOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(14.dp)
            .testTag("profile_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(LightEmeraldGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.take(2).uppercase(),
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = userProfile.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = LightEmeraldGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = userProfile.handle,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = LightSunGold.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "${userProfile.lga} LGA, Akwa Ibom State",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8C5300),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = userProfile.bio,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStatItem(count = "${userProfile.postsCount}", label = "Posts")
                        ProfileStatItem(count = "${userProfile.followersCount}", label = "Followers")
                        ProfileStatItem(count = "${userProfile.followingCount}", label = "Following")
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { isEditProfileOpen = true },
                        colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .testTag("btn_edit_profile")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Profile")
                    }
                }
            }
        }

        // Akwa Ibom Identity Badges
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Cultural Identity & Badges",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightEmeraldGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val badges = listOf(
                        "🌟 The Light Ambassador",
                        "🌴 Proud Akwa Ibomite",
                        "🗣️ Ibibio Native Speaker",
                        "🔞 18+ Age Verified",
                        "⚽ Sports Fanatic"
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(badges) { badge ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = badge,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Navigation Links
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Management & Tools",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectTab(AppTab.ADMIN) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = LightEmeraldGreen
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Admin & Moderation Panel", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Manage users, videos, content reports & bulletins", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectTab(AppTab.DATING) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Light Dating (18+)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Privacy controls, matches and discover cards", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            }
        }

        // Build & Version Information
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "THE LIGHT • App Specifications",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = LightEmeraldGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Version: 1.0.0 (Release APK Signed)\n• Motto: “BRIGHTER WE SHINE!”\n• Backend: Offline-First SQLite Room DB with real-time state synchronization\n• Media: Vertical short movie player with multi-regional support (African, Chinese, Philippine, Akwa Ibom)\n• Language: Dedicated Ibibio cultural communication suite",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }

    if (isEditProfileOpen) {
        EditProfileDialog(
            currentName = userProfile.name,
            currentLga = userProfile.lga,
            currentBio = userProfile.bio,
            onDismiss = { isEditProfileOpen = false },
            onSave = { name, lga, bio ->
                viewModel.updateUserProfile(name, lga, bio)
                isEditProfileOpen = false
            }
        )
    }
}

@Composable
private fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontWeight = FontWeight.Black,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EditProfileDialog(
    currentName: String,
    currentLga: String,
    currentBio: String,
    onDismiss: () -> Unit,
    onSave: (name: String, lga: String, bio: String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var lga by remember { mutableStateOf(currentLga) }
    var bio by remember { mutableStateOf(currentBio) }

    val lgas = listOf("Uyo", "Ikot Ekpene", "Eket", "Oron", "Abak", "Etinan", "Ibeno", "Ikono", "Diaspora")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Akwa Ibom Profile", fontWeight = FontWeight.Bold, color = LightEmeraldGreen) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_name")
                )

                Text("Select Local Government Area (LGA):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(lgas) { l ->
                        FilterChip(
                            selected = lga == l,
                            onClick = { lga = l },
                            label = { Text(l, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth().height(80.dp).testTag("input_edit_bio")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, lga, bio) },
                colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
            ) {
                Text("Save Profile")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
