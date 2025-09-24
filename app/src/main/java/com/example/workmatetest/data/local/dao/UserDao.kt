package com.example.workmatetest.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workmatetest.data.local.entity.UserEntity
import com.example.workmatetest.data.local.entity.RecipeEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Int): RecipeEntity?

    @Query("SELECT id FROM recipes")
    suspend fun getAllRecipeIds(): List<Int>

    @Query("DELETE FROM recipes")
    suspend fun clearRecipes()

    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun getAllRecipesPaging(): PagingSource<Int, RecipeEntity>
}