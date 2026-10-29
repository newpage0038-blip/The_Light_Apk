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
import com.example.data.ChatMessage
import com.example.data.IbibioPhrase
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.LightViewModel

@Composable
fun IbibioZoneScreen(
    viewModel: LightViewModel,
    modifier: Modifier = Modifier
) {
    val ibibioPhrases by viewModel.ibibioPhrases.collectAsState()
    val ibibioConversations by viewModel.ibibioConversations.collectAsState()
    val searchQuery by viewModel.ibibioSearch.collectAsState()

    var activeTab by remember { mutableStateOf("Lounge") } // "Lounge", "Dictionary", "Proverbs", "VoiceRoom"
    var isReportingRuleOpen by remember { mutableStateOf(false) }
    var showPlayingAudioText by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("ibibio_zone_screen")
    ) {
        // Cultural Banner Explaining Ibibio-only requirement
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = LightEmeraldGreen
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "IBIBIO LANGUAGE ZONE",
                            color = LightSunGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "“KÈM USEM IBIBIO KPỌT MI!”",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = { isReportingRuleOpen = true },
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Language Rules",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Strictly Ibibio communication is required here. Help preserve our beautiful cultural heritage.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp
                )
            }
        }

        // Sub Tabs
        TabRow(
            selectedTabIndex = when (activeTab) {
                "Lounge" -> 0
                "Dictionary" -> 1
                "Proverbs" -> 2
                else -> 3
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = LightEmeraldGreen
        ) {
            Tab(
                selected = activeTab == "Lounge",
                onClick = { activeTab = "Lounge" },
                text = { Text("Nneme (Chat)", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == "Dictionary",
                onClick = { activeTab = "Dictionary" },
                text = { Text("Ikọ (Phrases)", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == "Proverbs",
                onClick = { activeTab = "Proverbs" },
                text = { Text("Nke (Wisdom)", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == "VoiceRoom",
                onClick = { activeTab = "VoiceRoom" },
                text = { Text("Voice Room", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
        }

        when (activeTab) {
            "Lounge" -> {
                IbibioLoungeView(
                    viewModel = viewModel,
                    onOpenRules = { isReportingRuleOpen = true }
                )
            }
            "Dictionary" -> {
                IbibioDictionaryView(
                    phrases = ibibioPhrases.filter { it.category != "Proverbs" },
                    searchQuery = searchQuery,
                    onSearchChange = { viewModel.setIbibioSearch(it) },
                    onPlayAudio = { showPlayingAudioText = it }
                )
            }
            "Proverbs" -> {
                IbibioProverbsView(
                    proverbs = ibibioPhrases.filter { it.category == "Proverbs" },
                    onPlayAudio = { showPlayingAudioText = it }
                )
            }
            "VoiceRoom" -> {
                IbibioVoiceCallingRoom()
            }
        }
    }

    if (showPlayingAudioText != null) {
        AlertDialog(
            onDismissRequest = { showPlayingAudioText = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, tint = LightEmeraldGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ibibio Pronunciation", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "Playing authentic Ibibio audio for: “$showPlayingAudioText”. Listen and repeat to practice!",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showPlayingAudioText = null },
                    colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
                ) {
                    Text("Done")
                }
            }
        )
    }

    if (isReportingRuleOpen) {
        AlertDialog(
            onDismissRequest = { isReportingRuleOpen = false },
            title = {
                Text("Ibibio Zone Rules & Moderation", fontWeight = FontWeight.Bold, color = LightEmeraldGreen)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. Speak, text, or send voice messages strictly in Ibibio language.")
                    Text("2. English translations are permitted in the phrasebook for learners.")
                    Text("3. Mutual respect: 'Emem ye Ima' (Peace & Love).")
                    Text("4. Community moderators review flagged non-Ibibio posts.")
                }
            },
            confirmButton = {
                Button(
                    onClick = { isReportingRuleOpen = false },
                    colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
                ) {
                    Text("I Understand")
                }
            }
        )
    }
}

@Composable
private fun IbibioLoungeView(
    viewModel: LightViewModel,
    onOpenRules: () -> Unit
) {
    val ibibioConvs by viewModel.ibibioConversations.collectAsState()
    var ibibioInput by remember { mutableStateOf("") }
    val quickPhrases = listOf("Amesiere!", "Idem mfo?", "Sosongo!", "Abasi ọdiọñ fi!", "Emem ye ima!")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Quick Ibibio response chips
        Text(
            text = "Kèm nọ mi (Quick Responses):",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(quickPhrases) { phrase ->
                SuggestionChip(
                    onClick = { ibibioInput = phrase },
                    label = { Text(phrase, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Open existing Ibibio Lounge room
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nneme Ibibio Lounge #1",
                        fontWeight = FontWeight.Bold,
                        color = LightEmeraldGreen,
                        fontSize = 14.sp
                    )
                    TextButton(onClick = onOpenRules) {
                        Text("Moderation Guide", fontSize = 11.sp)
                    }
                }

                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Surface(
                            color = LightEmeraldGreen.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Ubong: Amesire nditọ Akwa Ibom! Idem mbufo? (Good morning people of Akwa Ibom! How are you?)",
                                fontSize = 13.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    item {
                        Surface(
                            color = LightSunriseOrange.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Ekaette: Idem mi ọsọn! Emem odu ye afo. (I am strong! Peace be with you.)",
                                fontSize = 13.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                // Input bar in Ibibio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = ibibioInput,
                        onValueChange = { ibibioInput = it },
                        placeholder = { Text("Wet ikọ Ibibio mi...") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_ibibio_lounge"),
                        shape = RoundedCornerShape(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (ibibioInput.isNotBlank()) {
                                viewModel.sendMessage("conv_ibibio_1", ibibioInput)
                                ibibioInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(LightEmeraldGreen, CircleShape)
                            .testTag("btn_send_ibibio")
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun IbibioDictionaryView(
    phrases: List<IbibioPhrase>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onPlayAudio: (String) -> Unit
) {
    val filtered = phrases.filter {
        it.ibibioText.contains(searchQuery, ignoreCase = true) ||
        it.englishTranslation.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search Ibibio phrase or English meaning...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_ibibio_dictionary"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { phrase ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = phrase.ibibioText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = LightEmeraldGreen
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = phrase.englishTranslation,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Pronunciation: ${phrase.pronunciationGuide}",
                                fontSize = 11.sp,
                                color = LightSunriseOrange
                            )
                        }
                        IconButton(
                            onClick = { onPlayAudio(phrase.ibibioText) },
                            modifier = Modifier
                                .background(LightEmeraldGreen.copy(alpha = 0.15f), CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Play Pronunciation",
                                tint = LightEmeraldGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IbibioProverbsView(
    proverbs: List<IbibioPhrase>,
    onPlayAudio: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Nke ye Nke (Ibibio Wisdom & Proverbs)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = LightEmeraldGreen
            )
        }
        items(proverbs) { p ->
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
                        Text(
                            text = "PROVERB",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = LightSunriseOrange
                        )
                        IconButton(onClick = { onPlayAudio(p.ibibioText) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Play", tint = LightEmeraldGreen)
                        }
                    }
                    Text(
                        text = "“${p.ibibioText}”",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Meaning: ${p.englishTranslation}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun IbibioVoiceCallingRoom() {
    var isCallActive by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(if (isCallActive) LightEmeraldGreen else Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.RecordVoiceOver,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = if (isCallActive) "Connected to Ibibio Practice Audio Lounge" else "Ibibio Live Audio Call Lounge",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (isCallActive) "Microphone active. Speak with fellow Ibibio speakers in real-time."
                else "Join voice room to practice spoken Ibibio and engage in live oral storytelling.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { isCallActive = !isCallActive },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCallActive) Color(0xFFD32F2F) else LightEmeraldGreen
                )
            ) {
                Icon(
                    imageVector = if (isCallActive) Icons.Default.CallEnd else Icons.Default.Call,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isCallActive) "Leave Call Room" else "Join Audio Lounge")
            }
        }
    }
}
