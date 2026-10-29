package com.example.ui.screens

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.VideoItem
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.LightViewModel

@Composable
fun VideoFeedScreen(
    viewModel: LightViewModel,
    modifier: Modifier = Modifier
) {
    val videos by viewModel.allVideos.collectAsState()
    val selectedCategory by viewModel.videoCategoryFilter.collectAsState()
    var isUploadDialogOpen by remember { mutableStateOf(false) }

    val filteredVideos = remember(videos, selectedCategory) {
        if (selectedCategory == "All") videos
        else videos.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("video_feed_screen")
    ) {
        if (filteredVideos.isEmpty()) {
            EmptyVideoState(
                category = selectedCategory,
                onResetCategory = { viewModel.setVideoCategoryFilter("All") },
                onUploadClick = { isUploadDialogOpen = true }
            )
        } else {
            val pagerState = rememberPagerState(pageCount = { filteredVideos.size })

            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1
            ) { page ->
                val video = filteredVideos[page]
                val isCurrentPage = pagerState.currentPage == page

                VideoPageItem(
                    video = video,
                    isActive = isCurrentPage,
                    onLikeToggle = { viewModel.toggleVideoLike(video) },
                    onFollowToggle = { viewModel.toggleVideoFollow(video) },
                    onCommentClick = { viewModel.openVideoComments(video) }
                )
            }
        }

        // Top Category Selector Overlay
        CategoryHeaderTabs(
            selectedCategory = selectedCategory,
            onSelectCategory = { viewModel.setVideoCategoryFilter(it) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        )

        // Upload Video Floating Button
        FloatingActionButton(
            onClick = { isUploadDialogOpen = true },
            containerColor = LightSunriseOrange,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 20.dp)
                .testTag("btn_upload_video")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Upload Video",
                modifier = Modifier.size(26.dp)
            )
        }

        if (isUploadDialogOpen) {
            UploadVideoDialog(
                onDismiss = { isUploadDialogOpen = false },
                onUpload = { title, category, url ->
                    viewModel.uploadVideo(title, category, url)
                    isUploadDialogOpen = false
                }
            )
        }
    }
}

@Composable
private fun VideoPageItem(
    video: VideoItem,
    isActive: Boolean,
    onLikeToggle: () -> Unit,
    onFollowToggle: () -> Unit,
    onCommentClick: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    var mediaPlayerRef by remember { mutableStateOf<MediaPlayer?>(null) }

    // Rotate animation for sound disc
    val infiniteTransition = rememberInfiniteTransition(label = "disc")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_angle"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                videoViewRef?.let { vv ->
                    if (vv.isPlaying) {
                        vv.pause()
                        isPlaying = false
                    } else {
                        vv.start()
                        isPlaying = true
                    }
                }
            }
    ) {
        // Video View with auto-looping playback
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setVideoURI(Uri.parse(video.videoUrl))
                    setOnPreparedListener { mp ->
                        mediaPlayerRef = mp
                        mp.isLooping = true
                        if (isMuted) mp.setVolume(0f, 0f) else mp.setVolume(1f, 1f)
                        if (isActive) {
                            start()
                            isPlaying = true
                        }
                    }
                    setOnErrorListener { _, _, _ ->
                        // Graceful recovery for unavailable stream
                        true
                    }
                    videoViewRef = this
                }
            },
            update = { vv ->
                if (isActive) {
                    if (!vv.isPlaying && isPlaying) {
                        vv.start()
                    }
                } else {
                    if (vv.isPlaying) {
                        vv.pause()
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Gradient Shadow Overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Pause indicator overlay
        if (!isPlaying) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(68.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Paused",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        // Bottom Creator & Content Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 24.dp)
        ) {
            // Category Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when (video.category.lowercase()) {
                    "african" -> LightSunriseOrange
                    "chinese" -> Color(0xFFC62828)
                    "philippine" -> Color(0xFF1565C0)
                    else -> LightEmeraldGreen
                }
            ) {
                Text(
                    text = "${video.category.uppercase()} ENTERTAINMENT",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Creator Handle
            Text(
                text = video.handle,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Title & Description
            Text(
                text = video.title,
                color = Color.White.copy(alpha = 0.95f),
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Sound Title & Vinyl Animation
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = LightSunGold,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = video.soundTitle,
                    color = LightSunGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right Side Vertical Action Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Creator Avatar with Follow Button
            Box(
                modifier = Modifier.size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(LightEmeraldGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = video.creator.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                if (!video.isFollowed) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                            .clickable(onClick = onFollowToggle),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Follow",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Like Action
            ActionIconButton(
                icon = if (video.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                count = video.likesCount.toString(),
                tint = if (video.isLiked) Color.Red else Color.White,
                onClick = onLikeToggle,
                tag = "btn_like_${video.id}"
            )

            // Comments Action
            ActionIconButton(
                icon = Icons.Default.ChatBubbleOutline,
                count = video.commentsCount.toString(),
                tint = Color.White,
                onClick = onCommentClick,
                tag = "btn_comments_${video.id}"
            )

            // Share Action
            ActionIconButton(
                icon = Icons.Default.Share,
                count = video.sharesCount.toString(),
                tint = Color.White,
                onClick = {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Watch '${video.title}' on THE LIGHT of Akwa Ibom! Brighter We Shine!")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Video via"))
                },
                tag = "btn_share_${video.id}"
            )

            // Mute / Unmute Toggle
            IconButton(
                onClick = {
                    isMuted = !isMuted
                    mediaPlayerRef?.let { mp ->
                        if (isMuted) mp.setVolume(0f, 0f) else mp.setVolume(1f, 1f)
                    }
                },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = if (isMuted) "Unmute" else "Mute",
                    tint = Color.White
                )
            }

            // Rotating Vinyl Disc
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .rotate(if (isPlaying) angle else 0f)
                    .clip(CircleShape)
                    .background(Color(0xFF212121)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(LightSunGold)
                )
            }
        }
    }
}

@Composable
private fun CategoryHeaderTabs(
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "African", "Chinese", "Philippine", "AkwaIbom")

    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { cat ->
            val isSelected = selectedCategory.equals(cat, ignoreCase = true)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) LightEmeraldGreen else Color.Transparent)
                    .clickable { onSelectCategory(cat) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .testTag("tab_cat_$cat")
            ) {
                Text(
                    text = if (cat == "AkwaIbom") "Akwa Ibom" else cat,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.75f),
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ActionIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    tint: Color,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = count,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EmptyVideoState(
    category: String,
    onResetCategory: () -> Unit,
    onUploadClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.VideoLibrary,
            contentDescription = null,
            tint = LightSunGold,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No videos in '$category' yet",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Be the first to share an entertaining clip with the Akwa Ibom community!",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onResetCategory,
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Show All")
            }
            Button(
                onClick = onUploadClick,
                colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
            ) {
                Text("Upload Video")
            }
        }
    }
}

@Composable
fun UploadVideoDialog(
    onDismiss: () -> Unit,
    onUpload: (title: String, category: String, url: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("AkwaIbom") }
    var videoUrl by remember { mutableStateOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4") }
    val categories = listOf("AkwaIbom", "African", "Chinese", "Philippine")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Upload Entertainment Video",
                fontWeight = FontWeight.Bold,
                color = LightEmeraldGreen
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Video Title") },
                    placeholder = { Text("e.g. Uyo Dance Challenge / Short Comedy") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_upload_title")
                )

                Text("Select Category:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = videoUrl,
                    onValueChange = { videoUrl = it },
                    label = { Text("Video Stream URL (MP4)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_upload_url")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onUpload(title, category, videoUrl) },
                colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen),
                modifier = Modifier.testTag("btn_submit_upload")
            ) {
                Text("Upload & Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
