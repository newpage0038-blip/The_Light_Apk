package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppTab(val title: String, val tag: String) {
    VIDEOS("Videos", "tab_videos"),
    COMMUNITY("Community", "tab_community"),
    CHAT("Messages", "tab_chat"),
    DATING("Dating 18+", "tab_dating"),
    IBIBIO("Ibibio Zone", "tab_ibibio"),
    SPORTS("Sports & News", "tab_sports"),
    OPPORTUNITIES("Opportunities", "tab_opportunities"),
    PROFILE("Profile", "tab_profile"),
    ADMIN("Admin Panel", "tab_admin")
}

data class UserProfile(
    val name: String = "Aniekpeno Akpan",
    val handle: String = "@aniekpeno_light",
    val lga: String = "Uyo",
    val bio: String = "Proud Akwa Ibom son. Tech enthusiast, cultural advocate & entrepreneur. Brighter We Shine!",
    val isVerified: Boolean = true,
    val isAdmin: Boolean = true,
    val postsCount: Int = 12,
    val followersCount: Int = 840,
    val followingCount: Int = 320
)

data class InAppNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false
)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class LightViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val dao = database.appDao()

    // Navigation State
    private val _currentTab = MutableStateFlow(AppTab.VIDEOS)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Welcome / Get Started State
    private val _hasStarted = MutableStateFlow(false)
    val hasStarted: StateFlow<Boolean> = _hasStarted.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Guarantee fresh working video stream URLs are populated in Room
            dao.insertVideos(PreseedData.initialVideos)
        }
    }

    fun onGetStarted() {
        _hasStarted.value = true
        _currentTab.value = AppTab.VIDEOS
    }

    fun showGetStarted() {
        _hasStarted.value = false
    }

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Search and Notifications
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchOpen = MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    private val _notifications = MutableStateFlow(
        listOf(
            InAppNotification("1", "New Live EPL Score", "Arsenal scored! Now Arsenal 2 - 1 Man City.", "5m ago"),
            InAppNotification("2", "Community Like", "Ekaette Bassey liked your post on Tech Innovation.", "20m ago"),
            InAppNotification("3", "New Match!", "You have a new mutual match on Light Dating 18+!", "1h ago"),
            InAppNotification("4", "Official Announcement", "State Undergraduate Scholarship applications open today.", "3h ago")
        )
    )
    val notifications: StateFlow<List<InAppNotification>> = _notifications.asStateFlow()

    // --- VIDEOS ---
    val allVideos: StateFlow<List<VideoItem>> = dao.getAllVideos()
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialVideos)

    private val _videoCategoryFilter = MutableStateFlow("All")
    val videoCategoryFilter: StateFlow<String> = _videoCategoryFilter.asStateFlow()

    private val _selectedVideoForComments = MutableStateFlow<VideoItem?>(null)
    val selectedVideoForComments: StateFlow<VideoItem?> = _selectedVideoForComments.asStateFlow()

    val currentVideoComments: StateFlow<List<CommentItem>> = _selectedVideoForComments
        .flatMapLatest { video ->
            if (video == null) flowOf(emptyList())
            else dao.getComments(video.id)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // --- COMMUNITY ---
    val allPosts: StateFlow<List<CommunityPost>> = dao.getAllPosts()
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialPosts)

    private val _postCategoryFilter = MutableStateFlow("All")
    val postCategoryFilter: StateFlow<String> = _postCategoryFilter.asStateFlow()

    private val _selectedPostForComments = MutableStateFlow<CommunityPost?>(null)
    val selectedPostForComments: StateFlow<CommunityPost?> = _selectedPostForComments.asStateFlow()

    val currentPostComments: StateFlow<List<CommentItem>> = _selectedPostForComments
        .flatMapLatest { post ->
            if (post == null) flowOf(emptyList())
            else dao.getComments(post.id)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // --- CHAT ---
    val regularConversations: StateFlow<List<Conversation>> = dao.getConversations(false)
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialConversations.filter { !it.isIbibioZone })

    val ibibioConversations: StateFlow<List<Conversation>> = dao.getConversations(true)
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialConversations.filter { it.isIbibioZone })

    private val _activeConversation = MutableStateFlow<Conversation?>(null)
    val activeConversation: StateFlow<Conversation?> = _activeConversation.asStateFlow()

    val activeMessages: StateFlow<List<ChatMessage>> = _activeConversation
        .flatMapLatest { conv ->
            if (conv == null) flowOf(emptyList())
            else dao.getMessagesForConversation(conv.id)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // --- DATING ---
    val availableDatingProfiles: StateFlow<List<DatingProfile>> = dao.getAvailableDatingProfiles()
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialDatingProfiles)

    val matchedDatingProfiles: StateFlow<List<DatingProfile>> = dao.getMatchedDatingProfiles()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isAdultConfirmed = MutableStateFlow(true)
    val isAdultConfirmed: StateFlow<Boolean> = _isAdultConfirmed.asStateFlow()

    private val _matchCelebration = MutableStateFlow<DatingProfile?>(null)
    val matchCelebration: StateFlow<DatingProfile?> = _matchCelebration.asStateFlow()

    // --- IBIBIO ---
    val ibibioPhrases: StateFlow<List<IbibioPhrase>> = dao.getIbibioPhrases()
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialIbibioPhrases)

    private val _ibibioSearch = MutableStateFlow("")
    val ibibioSearch: StateFlow<String> = _ibibioSearch.asStateFlow()

    // --- SPORTS & ENTERTAINMENT ---
    val eplFixtures: StateFlow<List<SportsFixture>> = dao.getFixturesByLeague("EPL")
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialFixtures.filter { it.league == "EPL" })

    val uclFixtures: StateFlow<List<SportsFixture>> = dao.getFixturesByLeague("UCL")
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialFixtures.filter { it.league == "UCL" })

    val entertainmentNews: StateFlow<List<EntertainmentItem>> = dao.getEntertainmentItems()
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialEntertainment)

    // --- OPPORTUNITIES ---
    val allOpportunities: StateFlow<List<OpportunityItem>> = dao.getAllOpportunities()
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialOpportunities)

    private val _oppCategoryFilter = MutableStateFlow("All")
    val oppCategoryFilter: StateFlow<String> = _oppCategoryFilter.asStateFlow()

    private val _selectedOpportunity = MutableStateFlow<OpportunityItem?>(null)
    val selectedOpportunity: StateFlow<OpportunityItem?> = _selectedOpportunity.asStateFlow()

    // --- ADMIN & REPORTS ---
    val allReports: StateFlow<List<ReportItem>> = dao.getAllReports()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val announcements: StateFlow<List<AnnouncementItem>> = dao.getAnnouncements()
        .stateIn(viewModelScope, SharingStarted.Lazily, PreseedData.initialAnnouncements)

    // ================= ACTIONS =================

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setSearchOpen(isOpen: Boolean) {
        _isSearchOpen.value = isOpen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // --- Video Actions ---
    fun setVideoCategoryFilter(category: String) {
        _videoCategoryFilter.value = category
    }

    fun toggleVideoLike(video: VideoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = video.copy(
                isLiked = !video.isLiked,
                likesCount = if (video.isLiked) video.likesCount - 1 else video.likesCount + 1
            )
            dao.updateVideo(updated)
        }
    }

    fun toggleVideoFollow(video: VideoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = video.copy(isFollowed = !video.isFollowed)
            dao.updateVideo(updated)
        }
    }

    fun openVideoComments(video: VideoItem?) {
        _selectedVideoForComments.value = video
    }

    fun addVideoComment(videoId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val comment = CommentItem(
                id = UUID.randomUUID().toString(),
                parentId = videoId,
                authorName = _userProfile.value.name,
                content = text.trim(),
                timestamp = "Just now"
            )
            dao.insertComment(comment)
            // update comments count on video
            _selectedVideoForComments.value?.let { v ->
                dao.updateVideo(v.copy(commentsCount = v.commentsCount + 1))
            }
        }
    }

    fun uploadVideo(title: String, category: String, url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newVideo = VideoItem(
                id = "vid_custom_" + System.currentTimeMillis(),
                title = title.ifBlank { "Akwa Ibom Vibrant Vibes" },
                creator = _userProfile.value.name,
                handle = _userProfile.value.handle,
                category = category,
                videoUrl = url.ifBlank { "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4" },
                thumbnailDescription = "User uploaded entertainment video from " + _userProfile.value.lga,
                likesCount = 1,
                commentsCount = 0,
                sharesCount = 0,
                isLiked = true,
                soundTitle = "The Light Original Audio - " + _userProfile.value.lga,
                durationText = "0:30"
            )
            dao.insertVideo(newVideo)
        }
    }

    // --- Community Actions ---
    fun setPostCategoryFilter(category: String) {
        _postCategoryFilter.value = category
    }

    fun togglePostLike(post: CommunityPost) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = post.copy(
                isLiked = !post.isLiked,
                likesCount = if (post.isLiked) post.likesCount - 1 else post.likesCount + 1
            )
            dao.updatePost(updated)
        }
    }

    fun togglePostSave(post: CommunityPost) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updatePost(post.copy(isSaved = !post.isSaved))
        }
    }

    fun openPostComments(post: CommunityPost?) {
        _selectedPostForComments.value = post
    }

    fun addPostComment(postId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val comment = CommentItem(
                id = UUID.randomUUID().toString(),
                parentId = postId,
                authorName = _userProfile.value.name,
                content = text.trim(),
                timestamp = "Just now"
            )
            dao.insertComment(comment)
            _selectedPostForComments.value?.let { p ->
                dao.updatePost(p.copy(commentsCount = p.commentsCount + 1))
            }
        }
    }

    fun createPost(content: String, category: String) {
        if (content.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val post = CommunityPost(
                id = "post_" + System.currentTimeMillis(),
                authorName = _userProfile.value.name,
                authorHandle = _userProfile.value.handle,
                authorLga = _userProfile.value.lga,
                content = content.trim(),
                category = category,
                timestamp = "Just now",
                likesCount = 0,
                commentsCount = 0
            )
            dao.insertPost(post)
        }
    }

    // --- Chat Actions ---
    fun openConversation(conv: Conversation?) {
        _activeConversation.value = conv
    }

    fun sendMessage(convId: String, text: String, isVoice: Boolean = false, voiceDuration: Int = 0) {
        if (text.isBlank() && !isVoice) return
        viewModelScope.launch(Dispatchers.IO) {
            val isIbibio = _activeConversation.value?.isIbibioZone == true
            val msg = ChatMessage(
                id = "msg_" + System.currentTimeMillis(),
                conversationId = convId,
                senderName = _userProfile.value.name,
                content = if (isVoice) "Voice message (${voiceDuration}s)" else text.trim(),
                timestamp = "Just now",
                isFromMe = true,
                isVoiceNote = isVoice,
                voiceDurationSec = voiceDuration,
                isIbibioZone = isIbibio
            )
            dao.insertMessage(msg)
        }
    }

    fun createGroupChat(name: String, subtitle: String, isIbibio: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val group = Conversation(
                id = "conv_group_" + System.currentTimeMillis(),
                name = name.trim(),
                subtitle = subtitle.trim(),
                lastMessage = "Group created",
                timestamp = "Just now",
                unreadCount = 0,
                isGroup = true,
                isIbibioZone = isIbibio
            )
            dao.insertConversations(listOf(group))
            _activeConversation.value = group
        }
    }

    // --- Dating Actions ---
    fun confirmAdult(confirmed: Boolean) {
        _isAdultConfirmed.value = confirmed
    }

    fun likeDatingProfile(profile: DatingProfile, isSuper: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = profile.copy(
                isLiked = true,
                isSuperLiked = isSuper,
                isMatched = true // Match!
            )
            dao.updateDatingProfile(updated)
            _matchCelebration.value = updated
        }
    }

    fun passDatingProfile(profile: DatingProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateDatingProfile(profile.copy(isPassed = true))
        }
    }

    fun dismissMatchCelebration() {
        _matchCelebration.value = null
    }

    fun reportDatingProfile(profile: DatingProfile, reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateDatingProfile(profile.copy(isReported = true, isBlocked = true))
            val report = ReportItem(
                id = "rep_" + System.currentTimeMillis(),
                targetType = "DatingProfile",
                targetId = profile.id,
                title = "Dating Report: ${profile.name} (${profile.age})",
                reason = reason,
                timestamp = "Just now"
            )
            dao.insertReport(report)
        }
    }

    fun blockDatingProfile(profile: DatingProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateDatingProfile(profile.copy(isBlocked = true))
        }
    }

    // --- Ibibio Actions ---
    fun setIbibioSearch(query: String) {
        _ibibioSearch.value = query
    }

    fun reportIbibioRoomNonCompliance(msg: ChatMessage) {
        viewModelScope.launch(Dispatchers.IO) {
            val report = ReportItem(
                id = "rep_" + System.currentTimeMillis(),
                targetType = "ChatMessage",
                targetId = msg.id,
                title = "Non-Ibibio message in Ibibio Lounge",
                reason = "Message: '${msg.content}' is not in Ibibio language.",
                timestamp = "Just now"
            )
            dao.insertReport(report)
        }
    }

    // --- Opportunities Actions ---
    fun setOppCategoryFilter(cat: String) {
        _oppCategoryFilter.value = cat
    }

    fun selectOpportunity(opp: OpportunityItem?) {
        _selectedOpportunity.value = opp
    }

    fun toggleOpportunityBookmark(opp: OpportunityItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateOpportunity(opp.copy(isBookmarked = !opp.isBookmarked))
        }
    }

    fun createOpportunity(
        title: String,
        org: String,
        category: String,
        location: String,
        deadline: String,
        desc: String,
        reqs: String,
        contact: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = OpportunityItem(
                id = "opp_" + System.currentTimeMillis(),
                title = title.trim(),
                organization = org.trim(),
                category = category,
                location = location.trim(),
                deadline = deadline.trim().ifBlank { "Open" },
                description = desc.trim(),
                requirements = reqs.trim(),
                contactOrUrl = contact.trim()
            )
            dao.insertOpportunity(item)
        }
    }

    // --- Admin Actions ---
    fun dismissReport(report: ReportItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateReport(report.copy(status = "Dismissed"))
        }
    }

    fun resolveAndBan(report: ReportItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateReport(report.copy(status = "Banned"))
            if (report.targetType == "Post") {
                dao.deletePost(report.targetId)
            }
        }
    }

    fun broadcastAnnouncement(title: String, content: String, badge: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = AnnouncementItem(
                id = "ann_" + System.currentTimeMillis(),
                title = title.trim(),
                content = content.trim(),
                badge = badge,
                dateStr = "Just now"
            )
            dao.insertAnnouncement(item)
        }
    }

    // --- Profile Actions ---
    fun updateUserProfile(name: String, lga: String, bio: String) {
        _userProfile.value = _userProfile.value.copy(
            name = name.trim(),
            lga = lga.trim(),
            bio = bio.trim()
        )
    }
}
