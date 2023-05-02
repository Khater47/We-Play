package com.example.mad

import android.content.Context
import com.example.mad.model.PlaygroundsDao
import com.example.mad.model.ReservationDao
import com.example.mad.model.SportDao
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
    fun provideDao(@ApplicationContext context: Context): ReservationDao {
        return UserDatabase.getDatabase(context).reservationDao()
    }
    @Singleton
    @Provides
    fun provideSportDao(@ApplicationContext context: Context): SportDao {
        return UserDatabase.getDatabase(context).sportDao()
    }

    @Singleton
    @Provides
    fun providePlaygroundsDao(@ApplicationContext context: Context): PlaygroundsDao {
        return UserDatabase.getDatabase(context).playgroundsDao()
    }

}