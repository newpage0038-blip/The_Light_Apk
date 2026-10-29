package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.data.ReportItem
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.LightViewModel

@Composable
fun AdminScreen(
    viewModel: LightViewModel,
    modifier: Modifier = Modifier
) {
    val reports by viewModel.allReports.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    var isBroadcastDialogOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(14.dp)
            .testTag("admin_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Admin Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = LightEmeraldGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Admin & Security Dashboard",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "The Light of Akwa Ibom • System Management",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = { isBroadcastDialogOpen = true },
                    colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_broadcast_announcement")
                ) {
                    Icon(imageVector = Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Broadcast", fontSize = 12.sp)
                }
            }
        }

        // Metrics Grid Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Total Users",
                    value = "1,480",
                    subtitle = "Akwa Ibom & Global",
                    color = LightEmeraldGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Pending Reports",
                    value = "${reports.count { it.status == "Pending" }}",
                    subtitle = "Content Moderation",
                    color = Color(0xFFD32F2F),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Database Sync",
                    value = "100%",
                    subtitle = "Room Encrypted Local",
                    color = LightSunGold,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Announcements",
                    value = "${announcements.size}",
                    subtitle = "State Bulletins",
                    color = LightSunriseOrange,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Content Moderation Queue
        item {
            Text(
                text = "Content Moderation Queue",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (reports.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = LightEmeraldGreen,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Moderation Queue is Clear!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "No flagged community posts, dating profiles, or messages at this time.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(reports) { report ->
                ReportCardItem(
                    report = report,
                    onDismiss = { viewModel.dismissReport(report) },
                    onBan = { viewModel.resolveAndBan(report) }
                )
            }
        }

        // Security & Privacy Audit Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Security & Privacy Protocols",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightEmeraldGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Zero-trust authentication active\n• 18+ strict age restriction verified on Light Dating\n• Anti-harassment moderation automated\n• Room SQLite persistence with transactional write safety",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }

    if (isBroadcastDialogOpen) {
        BroadcastAnnouncementDialog(
            onDismiss = { isBroadcastDialogOpen = false },
            onBroadcast = { title, content, badge ->
                viewModel.broadcastAnnouncement(title, content, badge)
                isBroadcastDialogOpen = false
            }
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Black, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ReportCardItem(
    report: ReportItem,
    onDismiss: () -> Unit,
    onBan: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFD32F2F).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = report.targetType.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (report.status == "Pending") LightSunriseOrange.copy(alpha = 0.15f) else LightEmeraldGreen.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = report.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (report.status == "Pending") LightSunriseOrange else LightEmeraldGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = report.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Reason: ${report.reason}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (report.status == "Pending") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Dismiss")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onBan,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text("Enforce Ban / Remove")
                    }
                }
            }
        }
    }
}

@Composable
fun BroadcastAnnouncementDialog(
    onDismiss: () -> Unit,
    onBroadcast: (title: String, content: String, badge: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var badge by remember { mutableStateOf("STATE BULLETIN") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Broadcast State Announcement", fontWeight = FontWeight.Bold, color = LightEmeraldGreen) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Announcement Title") },
                    placeholder = { Text("e.g. State Youth Council Notice") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Message Body") },
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onBroadcast(title, content, badge) },
                colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen),
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Broadcast Live")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
