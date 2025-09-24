package com.example.workmatetest.data.di

import com.example.workmatetest.data.local.db.UserDatabase
import com.example.workmatetest.data.remote.SpoonacularApiService
import com.example.workmatetest.domain.repository.RecipeRepository
import com.example.workmatetest.domain.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeRepositoryModule {
    @Provides
    @Singleton
    fun provideRecipeRepository(
        apiService: SpoonacularApiService,
        userDatabase: UserDatabase
    ): RecipeRepository {
        return RecipeRepositoryImpl(apiService, userDatabase.userDao())
    }
}