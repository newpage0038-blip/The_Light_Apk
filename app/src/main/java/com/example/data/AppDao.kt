package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // --- Videos ---
    @Query("SELECT * FROM videos")
    fun getAllVideos(): Flow<List<VideoItem>>

    @Query("SELECT * FROM videos WHERE category = :category")
    fun getVideosByCategory(category: String): Flow<List<VideoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoItem)

    @Update
    suspend fun updateVideo(video: VideoItem)

    // --- Community Posts ---
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<CommunityPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<CommunityPost>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: CommunityPost)

    @Update
    suspend fun updatePost(post: CommunityPost)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: String)

    // --- Comments ---
    @Query("SELECT * FROM comments WHERE parentId = :parentId")
    fun getComments(parentId: String): Flow<List<CommentItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentItem)

    // --- Chat & Messages ---
    @Query("SELECT * FROM conversations WHERE isIbibioZone = :isIbibio")
    fun getConversations(isIbibio: Boolean): Flow<List<Conversation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<Conversation>)

    @Query("SELECT * FROM chat_messages WHERE conversationId = :convId ORDER BY id ASC")
    fun getMessagesForConversation(convId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    // --- Dating ---
    @Query("SELECT * FROM dating_profiles WHERE isBlocked = 0 AND isPassed = 0")
    fun getAvailableDatingProfiles(): Flow<List<DatingProfile>>

    @Query("SELECT * FROM dating_profiles WHERE isMatched = 1")
    fun getMatchedDatingProfiles(): Flow<List<DatingProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDatingProfiles(profiles: List<DatingProfile>)

    @Update
    suspend fun updateDatingProfile(profile: DatingProfile)

    // --- Ibibio ---
    @Query("SELECT * FROM ibibio_phrases")
    fun getIbibioPhrases(): Flow<List<IbibioPhrase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIbibioPhrases(phrases: List<IbibioPhrase>)

    // --- Sports ---
    @Query("SELECT * FROM sports_fixtures WHERE league = :league")
    fun getFixturesByLeague(league: String): Flow<List<SportsFixture>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixtures(fixtures: List<SportsFixture>)

    @Update
    suspend fun updateFixture(fixture: SportsFixture)

    // --- Entertainment ---
    @Query("SELECT * FROM entertainment_items")
    fun getEntertainmentItems(): Flow<List<EntertainmentItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntertainment(items: List<EntertainmentItem>)

    // --- Opportunities ---
    @Query("SELECT * FROM opportunities")
    fun getAllOpportunities(): Flow<List<OpportunityItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunities(opportunities: List<OpportunityItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunity(opportunity: OpportunityItem)

    @Update
    suspend fun updateOpportunity(opportunity: OpportunityItem)

    // --- Reports ---
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportItem)

    @Update
    suspend fun updateReport(report: ReportItem)

    @Query("DELETE FROM reports WHERE id = :reportId")
    suspend fun deleteReport(reportId: String)

    // --- Announcements ---
    @Query("SELECT * FROM announcements ORDER BY id DESC")
    fun getAnnouncements(): Flow<List<AnnouncementItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementItem)
}
