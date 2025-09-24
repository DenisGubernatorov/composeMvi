package com.example.workmatetest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.workmatetest.data.local.converters.Converters

@Entity(tableName = "users")
@TypeConverters(Converters::class)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surname: String,
    val name: String,
    val favoriteRecipeIds: List<Int>? = null // Можно добавить список любимых рецептов.
)