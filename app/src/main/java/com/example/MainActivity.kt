package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.GlobalSearchDialog
import com.example.ui.components.LightBottomNav
import com.example.ui.components.LightTopBar
import com.example.ui.components.NotificationsDialog
import com.example.ui.screens.*
import com.example.ui.theme.TheLightAppTheme
import com.example.viewmodel.AppTab
import com.example.viewmodel.LightViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: LightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheLightAppTheme {
                TheLightMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TheLightMainApp(viewModel: LightViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isSearchOpen by viewModel.isSearchOpen.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    var isNotificationsOpen by remember { mutableStateOf(false) }

    val selectedVideoForComments by viewModel.selectedVideoForComments.collectAsState()
    val videoComments by viewModel.currentVideoComments.collectAsState()

    val selectedPostForComments by viewModel.selectedPostForComments.collectAsState()
    val postComments by viewModel.currentPostComments.collectAsState()

    val allPosts by viewModel.allPosts.collectAsState()
    val allVideos by viewModel.allVideos.collectAsState()

    // BackHandler: If on any screen other than VIDEOS (Home), return to Home without closing app!
    BackHandler(enabled = currentTab != AppTab.VIDEOS) {
        viewModel.selectTab(AppTab.VIDEOS)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LightTopBar(
                currentTab = currentTab,
                unreadNotificationCount = notifications.size,
                onSearchClick = { viewModel.setSearchOpen(true) },
                onNotificationClick = { isNotificationsOpen = true },
                onAdminClick = {
                    if (currentTab == AppTab.ADMIN) viewModel.selectTab(AppTab.PROFILE)
                    else viewModel.selectTab(AppTab.ADMIN)
                }
            )
        },
        bottomBar = {
            LightBottomNav(
                currentTab = currentTab,
                onTabSelected = { tab -> viewModel.selectTab(tab) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.VIDEOS -> VideoFeedScreen(viewModel = viewModel)
                AppTab.COMMUNITY -> CommunityScreen(viewModel = viewModel)
                AppTab.CHAT -> ChatScreen(viewModel = viewModel)
                AppTab.DATING -> DatingScreen(viewModel = viewModel)
                AppTab.IBIBIO -> IbibioZoneScreen(viewModel = viewModel)
                AppTab.SPORTS -> SportsScreen(viewModel = viewModel)
                AppTab.OPPORTUNITIES -> OpportunitiesScreen(viewModel = viewModel)
                AppTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                AppTab.ADMIN -> AdminScreen(viewModel = viewModel)
            }
        }
    }

    // Video Comments Bottom Sheet
    if (selectedVideoForComments != null) {
        CommentsBottomSheet(
            title = selectedVideoForComments!!.title,
            comments = videoComments,
            onDismiss = { viewModel.openVideoComments(null) },
            onAddComment = { text ->
                viewModel.addVideoComment(selectedVideoForComments!!.id, text)
            }
        )
    }

    // Post Comments Bottom Sheet
    if (selectedPostForComments != null) {
        CommentsBottomSheet(
            title = selectedPostForComments!!.content,
            comments = postComments,
            onDismiss = { viewModel.openPostComments(null) },
            onAddComment = { text ->
                viewModel.addPostComment(selectedPostForComments!!.id, text)
            }
        )
    }

    // Global Search Modal
    if (isSearchOpen) {
        GlobalSearchDialog(
            query = searchQuery,
            onQueryChange = { viewModel.setSearchQuery(it) },
            onDismiss = { viewModel.setSearchOpen(false) },
            posts = allPosts,
            videos = allVideos
        )
    }

    // Notifications Modal
    if (isNotificationsOpen) {
        NotificationsDialog(
            notifications = notifications,
            onDismiss = { isNotificationsOpen = false }
        )
    }
}
