package com.example.mad.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Reservation::class,Sport::class,Playgrounds::class], version = 1, exportSchema = true)
abstract class UserDatabase: RoomDatabase() {
    abstract fun reservationDao(): ReservationDao
    abstract fun sportDao(): SportDao
    abstract fun playgroundsDao(): PlaygroundsDao
    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getDatabase(context: Context): UserDatabase =
            (INSTANCE ?:
            synchronized(this) {
                val i = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "userDatabase")
                    .createFromAsset("database/user.db")
                    .build()
                INSTANCE = i
                INSTANCE
            })!!
    }
}

