package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
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
import com.example.data.CommunityPost
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.LightViewModel

@Composable
fun CommunityScreen(
    viewModel: LightViewModel,
    modifier: Modifier = Modifier
) {
    val posts by viewModel.allPosts.collectAsState()
    val selectedCategory by viewModel.postCategoryFilter.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    var isCreatePostOpen by remember { mutableStateOf(false) }

    val filteredPosts = remember(posts, selectedCategory) {
        if (selectedCategory == "All") posts
        else posts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("community_screen"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreatePostOpen = true },
                containerColor = LightEmeraldGreen,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("btn_create_post_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Create Post",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // "What's on your mind?" card inspired by Facebook
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .testTag("create_post_bar"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clickable { isCreatePostOpen = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(LightEmeraldGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProfile.name.take(2).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "What is on your mind, ${userProfile.name.split(" ").first()}?",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { isCreatePostOpen = true }) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = "Add Media",
                                tint = LightSunriseOrange
                            )
                        }
                    }
                }
            }

            // Category Filter Chips
            item {
                val categories = listOf("All", "Tech", "Culture", "News", "Entertainment", "Lifestyle")
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { viewModel.setPostCategoryFilter(cat) },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = LightEmeraldGreen,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("chip_cat_$cat")
                        )
                    }
                }
            }

            // Community Posts List
            items(filteredPosts, key = { it.id }) { post ->
                PostItemCard(
                    post = post,
                    onLike = { viewModel.togglePostLike(post) },
                    onSave = { viewModel.togglePostSave(post) },
                    onComment = { viewModel.openPostComments(post) }
                )
            }
        }
    }

    if (isCreatePostOpen) {
        CreatePostDialog(
            userLga = userProfile.lga,
            onDismiss = { isCreatePostOpen = false },
            onSubmit = { content, category ->
                viewModel.createPost(content, category)
                isCreatePostOpen = false
            }
        )
    }
}

@Composable
private fun PostItemCard(
    post: CommunityPost,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onComment: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("post_card_${post.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Author Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            when (post.category.lowercase()) {
                                "culture" -> LightSunriseOrange
                                "tech" -> LightEmeraldGreen
                                else -> Color(0xFF1976D2)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorName.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = LightEmeraldGreen,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = LightSunGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${post.authorLga} LGA",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF8C5300),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "•  ${post.timestamp}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = post.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightEmeraldGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Content text
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(6.dp))

            // Action Buttons (Like, Comment, Save, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onLike,
                    modifier = Modifier.testTag("btn_post_like_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "Like",
                        tint = if (post.isLiked) LightEmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.likesCount}",
                        fontSize = 12.sp,
                        color = if (post.isLiked) LightEmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = onComment,
                    modifier = Modifier.testTag("btn_post_comment_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Comment,
                        contentDescription = "Comment",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.commentsCount}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = onSave,
                    modifier = Modifier.testTag("btn_post_save_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (post.isSaved) LightSunGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                TextButton(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "${post.content}\n\n- ${post.authorName} on THE LIGHT of Akwa Ibom")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Post via"))
                    }
                ) {
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

@Composable
fun CreatePostDialog(
    userLga: String,
    onDismiss: () -> Unit,
    onSubmit: (content: String, category: String) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Tech") }
    val categories = listOf("Tech", "Culture", "News", "Entertainment", "Lifestyle")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Create Community Post",
                fontWeight = FontWeight.Bold,
                color = LightEmeraldGreen
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Posting from $userLga LGA, Akwa Ibom",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text("Select Category:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("What is on your mind?") },
                    placeholder = { Text("Share an idea, community update, or cultural story...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .testTag("input_post_content")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(content, category) },
                colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen),
                enabled = content.isNotBlank(),
                modifier = Modifier.testTag("btn_submit_post")
            ) {
                Text("Publish Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
