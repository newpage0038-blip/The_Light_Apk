package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EntertainmentItem
import com.example.data.SportsFixture
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.LightViewModel

@Composable
fun SportsScreen(
    viewModel: LightViewModel,
    modifier: Modifier = Modifier
) {
    val eplFixtures by viewModel.eplFixtures.collectAsState()
    val uclFixtures by viewModel.uclFixtures.collectAsState()
    val entertainmentNews by viewModel.entertainmentNews.collectAsState()

    var activeTab by remember { mutableStateOf("EPL") } // "EPL", "UCL", "News"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("sports_screen")
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = when (activeTab) {
                "EPL" -> 0
                "UCL" -> 1
                else -> 2
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = LightEmeraldGreen
        ) {
            Tab(
                selected = activeTab == "EPL",
                onClick = { activeTab = "EPL" },
                text = { Text("Premier League", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeTab == "UCL",
                onClick = { activeTab = "UCL" },
                text = { Text("Champions League", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeTab == "News",
                onClick = { activeTab = "News" },
                text = { Text("Entertainment", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        // Live Audio & Broadcast Disclaimer Note
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Radio,
                    contentDescription = null,
                    tint = LightEmeraldGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Authorized live scores, match stats & audio commentary. Tap fixture for details.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        when (activeTab) {
            "EPL" -> FootballLeagueView(leagueName = "English Premier League", fixtures = eplFixtures)
            "UCL" -> FootballLeagueView(leagueName = "UEFA Champions League", fixtures = uclFixtures)
            "News" -> EntertainmentNewsView(news = entertainmentNews)
        }
    }
}

@Composable
private fun FootballLeagueView(
    leagueName: String,
    fixtures: List<SportsFixture>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$leagueName Fixtures & Results",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightEmeraldGreen
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = LightSunriseOrange.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "Season 2026",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightSunriseOrange,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        items(fixtures) { fixture ->
            FixtureCard(fixture = fixture)
        }

        // League Standings Table
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "$leagueName Standings",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Pos  Club", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("P", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(28.dp))
                        Text("GD", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(32.dp))
                        Text("Pts", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(32.dp))
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    val tableRows = if (leagueName.contains("Premier")) {
                        listOf(
                            Triple("1.  Arsenal", "28", "+36  68"),
                            Triple("2.  Liverpool", "28", "+32  65"),
                            Triple("3.  Manchester City", "28", "+30  63"),
                            Triple("4.  Chelsea", "28", "+18  54"),
                            Triple("5.  Newcastle United", "28", "+12  49")
                        )
                    } else {
                        listOf(
                            Triple("1.  Real Madrid", "8", "+14  21"),
                            Triple("2.  Bayern Munich", "8", "+12  19"),
                            Triple("3.  Paris Saint-Germain", "8", "+9  18"),
                            Triple("4.  Barcelona", "8", "+8  17")
                        )
                    }

                    tableRows.forEach { row ->
                        val parts = row.third.split("  ")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(row.first, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            Text(row.second, fontSize = 12.sp, modifier = Modifier.width(28.dp))
                            Text(parts[0], fontSize = 12.sp, modifier = Modifier.width(32.dp))
                            Text(parts[1], fontSize = 12.sp, fontWeight = FontWeight.Bold, color = LightEmeraldGreen, modifier = Modifier.width(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FixtureCard(fixture: SportsFixture) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("fixture_${fixture.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Status Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (fixture.status) {
                        "LIVE" -> Color(0xFFD32F2F)
                        "FT" -> MaterialTheme.colorScheme.surfaceVariant
                        else -> LightSunGold.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        text = if (fixture.status == "LIVE") "● LIVE ${fixture.minute}" else fixture.status,
                        color = if (fixture.status == "LIVE") Color.White else MaterialTheme.colorScheme.onSurface,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "${fixture.dateStr} • ${fixture.venue}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Teams & Scoreline
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Home Team
                Text(
                    text = fixture.homeTeam,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )

                // Score or Time
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (fixture.status == "LIVE") LightEmeraldGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = if (fixture.status == "UPCOMING") fixture.minute else "${fixture.homeScore}  -  ${fixture.awayScore}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = if (fixture.status == "LIVE") LightEmeraldGreen else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                // Away Team
                Text(
                    text = fixture.awayTeam,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EntertainmentNewsView(news: List<EntertainmentItem>) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Nollywood, Music & Celebrity News",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = LightEmeraldGreen
            )
        }

        items(news) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Open external browser or share
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "${item.title}\n\nRead more on THE LIGHT of Akwa Ibom")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share News via"))
                    },
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
                            shape = RoundedCornerShape(6.dp),
                            color = when (item.category.lowercase()) {
                                "nollywood" -> LightSunriseOrange
                                "music" -> LightSunGold
                                else -> LightEmeraldGreen
                            }
                        ) {
                            Text(
                                text = item.category.uppercase(),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "${item.dateStr} • ${item.readTime}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = item.snippet,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Source: ${item.sourceName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = LightEmeraldGreen
                        )
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
