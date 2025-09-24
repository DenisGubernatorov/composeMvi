package com.example.workmatetest.data.di

import android.content.Context
import com.example.workmatetest.data.local.db.UserDatabase
import com.example.workmatetest.data.local.converters.Converters
import com.example.workmatetest.presentation.mvi.SplashReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase {
        return UserDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UserDatabase) = database.userDao()


    @Provides
    @Singleton
    fun provideConverters(): Converters = Converters()
}