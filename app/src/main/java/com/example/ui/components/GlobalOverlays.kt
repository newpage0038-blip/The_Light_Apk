package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.data.CommentItem
import com.example.data.CommunityPost
import com.example.data.VideoItem
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.InAppNotification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    title: String,
    comments: List<CommentItem>,
    onDismiss: () -> Unit,
    onAddComment: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var newCommentText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("comments_bottom_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Comments (${comments.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (comments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No comments yet. Be the first to share your thoughts!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(comments, key = { it.id }) { comment ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(LightEmeraldGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = comment.authorName.take(2).uppercase(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = comment.authorName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = comment.timestamp,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = comment.content,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    placeholder = { Text("Add a comment...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_new_comment"),
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (newCommentText.isNotBlank()) {
                            onAddComment(newCommentText)
                            newCommentText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(LightEmeraldGreen, CircleShape)
                        .testTag("btn_submit_comment")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Comment",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun GlobalSearchDialog(
    query: String,
    onQueryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    posts: List<CommunityPost>,
    videos: List<VideoItem>
) {
    val filteredPosts = remember(query, posts) {
        if (query.isBlank()) emptyList()
        else posts.filter { it.content.contains(query, ignoreCase = true) || it.authorName.contains(query, ignoreCase = true) }
    }

    val filteredVideos = remember(query, videos) {
        if (query.isBlank()) emptyList()
        else videos.filter { it.title.contains(query, ignoreCase = true) || it.creator.contains(query, ignoreCase = true) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search THE LIGHT...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("global_search_input"),
                singleLine = true
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
            ) {
                if (query.isBlank()) {
                    Text(
                        text = "Type to search community discussions, short videos, opportunities and Ibibio culture.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else if (filteredPosts.isEmpty() && filteredVideos.isEmpty()) {
                    Text(
                        text = "No results found for “$query”.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (filteredVideos.isNotEmpty()) {
                            item {
                                Text("Videos (${filteredVideos.size})", fontWeight = FontWeight.Bold, color = LightEmeraldGreen, fontSize = 13.sp)
                            }
                            items(filteredVideos) { v ->
                                Text("🎬 ${v.title} by ${v.creator}", fontSize = 12.sp)
                            }
                        }

                        if (filteredPosts.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Community Posts (${filteredPosts.size})", fontWeight = FontWeight.Bold, color = LightSunriseOrange, fontSize = 13.sp)
                            }
                            items(filteredPosts) { p ->
                                Text("💬 ${p.authorName} (${p.authorLga}): ${p.content.take(50)}...", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
            ) {
                Text("Done")
            }
        }
    )
}

@Composable
fun NotificationsDialog(
    notifications: List<InAppNotification>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = LightEmeraldGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Notifications & Alerts", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notifications) { notif ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(notif.time, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(notif.message, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
            ) {
                Text("Close")
            }
        }
    )
}
