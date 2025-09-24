package com.example.workmatetest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workmatetest.domain.model.Recipe

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val image: String,
    val summary: String
) {
    fun toDomainRecipe(): Recipe = Recipe(
        id = this.id,
        title = this.title,
        image = this.image,
        summary = this.summary,
        servings = 0,
        readyInMinutes = 0
    )
}