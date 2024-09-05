package com.example.mvvmpracticetask.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmpracticetask.db.daos.UserDao
import com.example.mvvmpracticetask.db.models.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao() : UserDao
}