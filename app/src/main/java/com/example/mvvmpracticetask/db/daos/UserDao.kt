package com.example.mvvmpracticetask.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmpracticetask.constants.DatabaseConstants
import com.example.mvvmpracticetask.db.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user : User)

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_NAME_USER} ORDER BY NAME ASC LIMIT 1")
    suspend fun getCurrentUser(): User

    @Delete
    suspend fun deleteUser(user : User)
}