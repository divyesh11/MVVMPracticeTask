package com.example.mvvmpracticetask.repositories

import com.example.mvvmpracticetask.db.daos.UserDao
import com.example.mvvmpracticetask.db.models.User
import com.example.mvvmpracticetask.utils.DatabaseResponse
import javax.inject.Inject

class MainRepository @Inject constructor(
    val userDao : UserDao
){
    suspend fun getCurrentUser() : DatabaseResponse<User> {
        try {
            var currentUser = userDao.getCurrentUser()
            return DatabaseResponse.Success(currentUser)
        } catch (e : Exception) {
            return DatabaseResponse.Error(e.message.toString())
        }
    }

    suspend fun deleteUser(user : User) : DatabaseResponse<Boolean> {
        try {
            userDao.deleteUser(user)
            return DatabaseResponse.Success(true)
        } catch (e : Exception) {
            return DatabaseResponse.Error(e.message.toString())
        }
    }

    suspend fun addUser(user : User) : DatabaseResponse<String> {
        try {
            userDao.insertUser(user)
            return DatabaseResponse.Success("User details added successfully!")
        } catch (e : Exception) {
            return DatabaseResponse.Error(e.message.toString())
        }
    }

}