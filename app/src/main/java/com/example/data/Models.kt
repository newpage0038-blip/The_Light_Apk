package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoItem(
    @PrimaryKey val id: String,
    val title: String,
    val creator: String,
    val handle: String,
    val category: String, // "African", "Chinese", "Philippine", "AkwaIbom"
    val videoUrl: String,
    val thumbnailDescription: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val isFollowed: Boolean = false,
    val soundTitle: String = "Original Sound - The Light",
    val durationText: String = "0:45"
)

@Entity(tableName = "posts")
data class CommunityPost(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorLga: String, // e.g. "Uyo", "Ikot Ekpene", "Eket", "Oron"
    val content: String,
    val category: String, // "Culture", "Tech", "News", "Entertainment", "Lifestyle"
    val timestamp: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false
)

@Entity(tableName = "comments")
data class CommentItem(
    @PrimaryKey val id: String,
    val parentId: String,
    val authorName: String,
    val content: String,
    val timestamp: String
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderName: String,
    val content: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val isVoiceNote: Boolean = false,
    val voiceDurationSec: Int = 0,
    val isRead: Boolean = true,
    val isIbibioZone: Boolean = false
)

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey val id: String,
    val name: String,
    val subtitle: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false,
    val isIbibioZone: Boolean = false
)

@Entity(tableName = "dating_profiles")
data class DatingProfile(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val location: String, // LGA or city
    val profession: String,
    val bio: String,
    val interests: String, // comma-separated
    val isLiked: Boolean = false,
    val isMatched: Boolean = false,
    val isSuperLiked: Boolean = false,
    val isPassed: Boolean = false,
    val isBlocked: Boolean = false,
    val isReported: Boolean = false,
    val verified: Boolean = true
)

@Entity(tableName = "ibibio_phrases")
data class IbibioPhrase(
    @PrimaryKey val id: String,
    val ibibioText: String,
    val englishTranslation: String,
    val pronunciationGuide: String,
    val category: String // "Greetings", "Respect", "Culture", "Proverbs", "Daily"
)

@Entity(tableName = "sports_fixtures")
data class SportsFixture(
    @PrimaryKey val id: String,
    val league: String, // "EPL", "UCL"
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val status: String, // "LIVE", "FT", "UPCOMING"
    val minute: String, // e.g., "78'", "FT", "20:00"
    val dateStr: String,
    val venue: String
)

@Entity(tableName = "entertainment_items")
data class EntertainmentItem(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // "Nollywood", "Music", "Celebrity", "Events"
    val author: String,
    val snippet: String,
    val dateStr: String,
    val readTime: String,
    val sourceName: String
)

@Entity(tableName = "opportunities")
data class OpportunityItem(
    @PrimaryKey val id: String,
    val title: String,
    val organization: String,
    val category: String, // "Jobs", "Scholarships", "Business Grants", "Skills Training", "Tourism & Heritage"
    val location: String,
    val deadline: String,
    val description: String,
    val requirements: String,
    val contactOrUrl: String,
    val isBookmarked: Boolean = false
)

@Entity(tableName = "reports")
data class ReportItem(
    @PrimaryKey val id: String,
    val targetType: String, // "Post", "DatingProfile", "Video", "ChatMessage"
    val targetId: String,
    val title: String,
    val reason: String,
    val timestamp: String,
    val status: String = "Pending" // "Pending", "Dismissed", "Banned"
)

@Entity(tableName = "announcements")
data class AnnouncementItem(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val badge: String,
    val dateStr: String,
    val isPinned: Boolean = true
)
