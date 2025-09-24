package com.example.workmatetest.data.local.db

import com.example.workmatetest.data.local.converters.Converters
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.workmatetest.data.local.dao.UserDao
import com.example.workmatetest.data.local.entity.UserEntity
import com.example.workmatetest.data.local.entity.RecipeEntity

@Database(entities = [UserEntity::class, RecipeEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "workmate_database"
                ).addTypeConverter(Converters()).build().also { INSTANCE = it }
            }
        }
    }
}