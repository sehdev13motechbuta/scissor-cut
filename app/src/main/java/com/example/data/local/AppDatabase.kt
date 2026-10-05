package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Appointment
import com.example.data.model.ReviewItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Appointment::class, ReviewItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "scissor_cut_salon.db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }

            private suspend fun populateInitialData(database: AppDatabase) {
                val reviewDao = database.reviewDao()
                val initialReviews = listOf(
                    ReviewItem(
                        authorName = "Adarsh Choudhary",
                        rating = 5,
                        timeAgo = "1 month ago",
                        comment = "Friendly staff, excellent service, and great attention to detail. Got my beard grooming and haircut done here. Top tier experience!",
                        serviceUsed = "Haircut & Beard Styling",
                        source = "Google Review"
                    ),
                    ReviewItem(
                        authorName = "Archita Mahajan",
                        rating = 5,
                        timeAgo = "2 months ago",
                        comment = "Came to this place in a hurry; will come back again and again. Their staff attended immediately and gave me the best blowout and nail styling.",
                        serviceUsed = "Nail Art & Hair Styling",
                        source = "Google Review"
                    ),
                    ReviewItem(
                        authorName = "Isha Dhiman",
                        rating = 5,
                        timeAgo = "3 months ago",
                        comment = "Great experience of haircut or any other services. Very clean and hygienic salon on Airport Road.",
                        serviceUsed = "Advanced Haircut & Spa",
                        source = "Google Review"
                    ),
                    ReviewItem(
                        authorName = "Simranjit Kaur",
                        rating = 5,
                        timeAgo = "Recently",
                        comment = "Booked their Bridal HD makeup package and gel nail extensions for my cousin's wedding. Absolutely flawless and long lasting! 10/10 recommend.",
                        serviceUsed = "HD Bridal Makeup & Nails",
                        source = "Google Review"
                    ),
                    ReviewItem(
                        authorName = "Vikram Batra",
                        rating = 5,
                        timeAgo = "3 weeks ago",
                        comment = "Best salon in Sector 117 Mohali. Professional stylists who actually listen to what you want. Keratin treatment result was stunning.",
                        serviceUsed = "Keratin Smoothing Treatment",
                        source = "Justdial 5/5"
                    )
                )
                reviewDao.insertAll(initialReviews)
            }
        }
    }
}
