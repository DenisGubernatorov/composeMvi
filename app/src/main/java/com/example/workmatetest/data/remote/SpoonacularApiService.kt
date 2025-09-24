package com.example.workmatetest.data.remote


import com.example.workmatetest.BuildConfig
import com.example.workmatetest.data.local.entity.RecipeEntity
import com.example.workmatetest.domain.model.Recipe
import com.example.workmatetest.domain.model.RecipesResponse
import retrofit2.Response


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApiService {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 100,
        @Query("apiKey") apiKey: String = BuildConfig.SPOONACULAR_API_KEY,
        @Query("includeNutrition") includeNutrition: Boolean = false
    ): Response<RecipesResponse>


    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): Response<Recipe>
}