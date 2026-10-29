package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        VideoItem::class,
        CommunityPost::class,
        CommentItem::class,
        ChatMessage::class,
        Conversation::class,
        DatingProfile::class,
        IbibioPhrase::class,
        SportsFixture::class,
        EntertainmentItem::class,
        OpportunityItem::class,
        ReportItem::class,
        AnnouncementItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "the_light_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.appDao())
                    }
                }
            }

            suspend fun populateDatabase(dao: AppDao) {
                dao.insertVideos(PreseedData.initialVideos)
                dao.insertPosts(PreseedData.initialPosts)
                dao.insertConversations(PreseedData.initialConversations)
                for (msg in PreseedData.initialMessages) {
                    dao.insertMessage(msg)
                }
                dao.insertDatingProfiles(PreseedData.initialDatingProfiles)
                dao.insertIbibioPhrases(PreseedData.initialIbibioPhrases)
                dao.insertFixtures(PreseedData.initialFixtures)
                dao.insertEntertainment(PreseedData.initialEntertainment)
                dao.insertOpportunities(PreseedData.initialOpportunities)
                for (ann in PreseedData.initialAnnouncements) {
                    dao.insertAnnouncement(ann)
                }
            }
        }
    }
}
