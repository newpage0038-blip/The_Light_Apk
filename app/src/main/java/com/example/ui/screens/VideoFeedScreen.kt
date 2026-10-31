package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
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
    val context = LocalContext.current
    val videos by viewModel.allVideos.collectAsState()
    val selectedCategory by viewModel.videoCategoryFilter.collectAsState()
    var isUploadDialogOpen by remember { mutableStateOf(false) }
    var isOffline by remember { mutableStateOf(!checkInternetConnection(context)) }

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
        if (isOffline) {
            OfflineVideoState(
                onRetry = {
                    isOffline = !checkInternetConnection(context)
                }
            )
        } else if (filteredVideos.isEmpty()) {
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

                TikTokVideoPageItem(
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
                .padding(top = 10.dp)
        )

        // Upload Video Floating Button
        FloatingActionButton(
            onClick = { isUploadDialogOpen = true },
            containerColor = LightSunriseOrange,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 18.dp)
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

@OptIn(UnstableApi::class)
@Composable
private fun TikTokVideoPageItem(
    video: VideoItem,
    isActive: Boolean,
    onLikeToggle: () -> Unit,
    onFollowToggle: () -> Unit,
    onCommentClick: () -> Unit
) {
    val context = LocalContext.current

    var isPlayingState by remember { mutableStateOf(true) }
    var isBuffering by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var playbackError by remember { mutableStateOf<String?>(null) }
    var showPauseOverlay by remember { mutableStateOf(false) }

    // Media3 ExoPlayer Instance per video page
    val exoPlayer = remember(context, video.videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
            val mediaItem = MediaItem.fromUri(Uri.parse(video.videoUrl))
            setMediaItem(mediaItem)
            prepare()
        }
    }

    // Playback state synchronization with Pager (Auto play when visible, pause when swiped away)
    LaunchedEffect(isActive, isPlayingState) {
        if (isActive && isPlayingState) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    // Volume handling
    LaunchedEffect(isMuted) {
        exoPlayer.volume = if (isMuted) 0f else 1f
    }

    // Lifecycle and Player Event Listener
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                isBuffering = (state == Player.STATE_BUFFERING)
                if (state == Player.STATE_READY) {
                    playbackError = null
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isPlayingState = isPlaying
                showPauseOverlay = !isPlaying
            }

            override fun onPlayerError(error: PlaybackException) {
                isBuffering = false
                playbackError = error.message ?: "Unable to stream video"
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // Rotating vinyl disc animation
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
                // Tap to toggle play/pause immediately
                if (exoPlayer.isPlaying) {
                    exoPlayer.pause()
                } else {
                    exoPlayer.play()
                }
            }
    ) {
        // Fullscreen ExoPlayer Surface
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { playerView ->
                playerView.player = exoPlayer
            },
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Buffering Indicator
        if (isBuffering && playbackError == null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = LightSunGold,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Tap-to-play Paused Icon Overlay
        AnimatedVisibility(
            visible = showPauseOverlay && !isBuffering,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.Black.copy(alpha = 0.55f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Paused - Tap to play",
                    tint = Color.White,
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        // Playback Error fallback banner with retry
        if (playbackError != null) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.8f)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SignalCellularConnectedNoInternet0Bar,
                        contentDescription = null,
                        tint = LightSunriseOrange,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Buffering stream...",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            playbackError = null
                            isBuffering = true
                            exoPlayer.prepare()
                            exoPlayer.play()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
                    ) {
                        Text("Reload Video")
                    }
                }
            }
        }

        // Left Bottom Creator & Movie Information
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.76f)
                .padding(start = 16.dp, bottom = 22.dp)
        ) {
            // Regional Category Badge
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
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Creator Name and Handle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = video.creator,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = video.handle,
                    color = LightSunGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Video Title & Caption
            Text(
                text = video.title,
                color = Color.White.copy(alpha = 0.95f),
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Sound Track info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = LightSunGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = video.soundTitle,
                    color = LightSunGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right Vertical TikTok Action Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Creator Avatar with Follow Button
            Box(
                modifier = Modifier.size(52.dp),
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
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                if (!video.isFollowed) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(19.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE53935))
                            .clickable(onClick = onFollowToggle)
                            .testTag("btn_follow_${video.id}"),
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
                icon = if (video.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                count = video.likesCount.toString(),
                tint = if (video.isLiked) Color(0xFFE53935) else Color.White,
                onClick = onLikeToggle,
                tag = "btn_like_${video.id}"
            )

            // Comments Action
            ActionIconButton(
                icon = Icons.Outlined.ChatBubbleOutline,
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
                        putExtra(Intent.EXTRA_TEXT, "Watch '${video.title}' on THE LIGHT OF AKWA IBOM! “BRIGHTER WE SHINE!”")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Video via"))
                },
                tag = "btn_share_${video.id}"
            )

            // Mute / Unmute Audio Toggle
            IconButton(
                onClick = { isMuted = !isMuted },
                modifier = Modifier
                    .size(38.dp)
                    .background(Color.Black.copy(alpha = 0.35f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = if (isMuted) "Unmute" else "Mute",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Rotating Vinyl Disc
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .rotate(if (isPlayingState) angle else 0f)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E1E)),
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
            .background(Color.Black.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
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
    icon: ImageVector,
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
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = count,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun OfflineVideoState(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = null,
            tint = LightSunGold,
            modifier = Modifier.size(68.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "You are currently offline",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Connect to the internet to stream high-definition entertainment videos from Akwa Ibom, Africa, China, and the Philippines.",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Retry Connection")
        }
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
            fontSize = 13.sp,
            textAlign = TextAlign.Center
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
    var videoUrl by remember { mutableStateOf("https://www.w3schools.com/html/mov_bbb.mp4") }
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
                    label = { Text("Video Stream URL (MP4 / HLS)") },
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

private fun checkInternetConnection(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return true
    val activeNetwork = cm.activeNetwork ?: return false
    val capabilities = cm.getNetworkCapabilities(activeNetwork) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
