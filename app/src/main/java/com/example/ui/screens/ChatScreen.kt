package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.data.ChatMessage
import com.example.data.Conversation
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.LightViewModel

@Composable
fun ChatScreen(
    viewModel: LightViewModel,
    modifier: Modifier = Modifier
) {
    val regularConversations by viewModel.regularConversations.collectAsState()
    val activeConversation by viewModel.activeConversation.collectAsState()
    val activeMessages by viewModel.activeMessages.collectAsState()
    var isNewGroupDialogOpen by remember { mutableStateOf(false) }

    // If a conversation is active, display the chat thread; otherwise display the conversation list
    if (activeConversation != null) {
        BackHandler {
            viewModel.openConversation(null)
        }
        ChatThreadView(
            conversation = activeConversation!!,
            messages = activeMessages,
            onBack = { viewModel.openConversation(null) },
            onSendMessage = { text, isVoice, duration ->
                viewModel.sendMessage(activeConversation!!.id, text, isVoice, duration)
            },
            modifier = modifier
        )
    } else {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag("conversations_list_screen"),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { isNewGroupDialogOpen = true },
                    containerColor = LightEmeraldGreen,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("btn_new_group_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = "New Group Chat"
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    Text(
                        text = "Messages & Community Groups",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                items(regularConversations, key = { it.id }) { conv ->
                    ConversationItemRow(
                        conversation = conv,
                        onClick = { viewModel.openConversation(conv) }
                    )
                }
            }
        }
    }

    if (isNewGroupDialogOpen) {
        CreateGroupDialog(
            onDismiss = { isNewGroupDialogOpen = false },
            onCreate = { name, subtitle ->
                viewModel.createGroupChat(name, subtitle, false)
                isNewGroupDialogOpen = false
            }
        )
    }
}

@Composable
private fun ConversationItemRow(
    conversation: Conversation,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("conv_row_${conversation.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(if (conversation.isGroup) LightEmeraldGreen else LightSunriseOrange),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (conversation.isGroup) Icons.Default.Groups else Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversation.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = conversation.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                if (conversation.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(LightEmeraldGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversation.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(start = 80.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}

@Composable
fun ChatThreadView(
    conversation: Conversation,
    messages: List<ChatMessage>,
    onBack: () -> Unit,
    onSendMessage: (text: String, isVoice: Boolean, duration: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var voiceDuration by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("chat_thread_view")
    ) {
        // Chat Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("btn_chat_back")) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (conversation.isGroup) LightEmeraldGreen else LightSunriseOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (conversation.isGroup) Icons.Default.Groups else Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = conversation.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1
                    )
                    Text(
                        text = if (conversation.isGroup) "Group • Active members" else "Online • The Light Community",
                        fontSize = 11.sp,
                        color = LightEmeraldGreen
                    )
                }

                IconButton(onClick = { /* Voice Call simulation */ }) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Voice Call", tint = LightEmeraldGreen)
                }
                IconButton(onClick = { /* Video Call simulation */ }) {
                    Icon(imageVector = Icons.Default.Videocam, contentDescription = "Video Call", tint = LightEmeraldGreen)
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                MessageBubble(message = msg)
            }
        }

        // Input Bottom Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_chat_message"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Voice Note Record Simulator or Send Button
                if (messageText.isBlank()) {
                    IconButton(
                        onClick = {
                            // Instant 5-second simulated voice note
                            onSendMessage("", true, 5)
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(LightSunriseOrange, CircleShape)
                            .testTag("btn_voice_record")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Send Voice Note",
                            tint = Color.White
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            onSendMessage(messageText, false, 0)
                            messageText = ""
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(LightEmeraldGreen, CircleShape)
                            .testTag("btn_send_chat_message")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Message",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isMe = message.isFromMe
    val bubbleColor = if (isMe) LightEmeraldGreen else MaterialTheme.colorScheme.surface
    val textColor = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 2.dp,
                bottomEnd = if (isMe) 2.dp else 16.dp
            ),
            color = bubbleColor,
            shadowElevation = 1.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                if (!isMe && message.senderName.isNotBlank()) {
                    Text(
                        text = message.senderName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightSunriseOrange
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                if (message.isVoiceNote) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play voice note",
                            tint = if (isMe) Color.White else LightEmeraldGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Voice note (${message.voiceDurationSec}s)  |||||||||||",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor
                        )
                    }
                } else {
                    Text(
                        text = message.content,
                        fontSize = 14.sp,
                        color = textColor
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.timestamp,
                        fontSize = 10.sp,
                        color = if (isMe) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Delivered",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreateGroupDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, subtitle: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Community Group Chat", fontWeight = FontWeight.Bold, color = LightEmeraldGreen) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Group Name") },
                    placeholder = { Text("e.g. Eket Entrepreneurs Hub") },
                    modifier = Modifier.fillMaxWidth().testTag("input_group_name")
                )
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Description / Topic") },
                    placeholder = { Text("e.g. Networking, business & collaborations") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(name, subtitle) },
                colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen),
                enabled = name.isNotBlank(),
                modifier = Modifier.testTag("btn_submit_group")
            ) {
                Text("Create Group")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
