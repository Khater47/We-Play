package com.example.mad

import android.content.Context
import com.example.mad.model.PlaygroundsDao
import com.example.mad.model.ProfileDao
import com.example.mad.model.ProfileRatingDao
import com.example.mad.model.ProfileSportDao
import com.example.mad.model.ReservationDao
import com.example.mad.model.TimeSlotDao
import com.example.mad.model.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideReservationDao(@ApplicationContext context: Context): ReservationDao {
        return UserDatabase.getDatabase(context).reservationDao()
    }
    @Singleton
    @Provides
    fun providePlaygroundsDao(@ApplicationContext context: Context): PlaygroundsDao {
        return UserDatabase.getDatabase(context).playgroundsDao()
    }

    @Singleton
    @Provides
    fun provideProfile(@ApplicationContext context: Context): ProfileDao {
        return UserDatabase.getDatabase(context).profileDao()
    }

    @Singleton
    @Provides
    fun provideProfileSportDao(@ApplicationContext context: Context): ProfileSportDao {
        return UserDatabase.getDatabase(context).profileSportDao()
    }

    @Singleton
    @Provides
    fun provideProfileRatingDao(@ApplicationContext context: Context): ProfileRatingDao {
        return UserDatabase.getDatabase(context).profileRatingDao()
    }

    @Singleton
    @Provides
    fun providesTimeSlot(@ApplicationContext context: Context): TimeSlotDao {
        return UserDatabase.getDatabase(context).timeSlotDao()
    }

}