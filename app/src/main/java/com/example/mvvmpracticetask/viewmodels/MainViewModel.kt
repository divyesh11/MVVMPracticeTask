package com.example.mvvmpracticetask.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmpracticetask.db.models.User
import com.example.mvvmpracticetask.repositories.MainRepository
import com.example.mvvmpracticetask.utils.DatabaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    app : Application,
    private val mainRepository: MainRepository
) : AndroidViewModel(app){

    private val _addUserDetails =
        MutableStateFlow<DatabaseResponse<String>>(DatabaseResponse.Loading())
    val addUserDetails = _addUserDetails.asStateFlow()

    fun addNewUserDetails(user : User) {
        viewModelScope.launch {
            try {
                var response = mainRepository.addUser(user)
                _addUserDetails.value = response
            } catch (e : Exception) {
                _addUserDetails.value = DatabaseResponse.Error(e.message.toString());
            }
        }
    }

    private val _currentUser = MutableStateFlow<DatabaseResponse<User>>(DatabaseResponse.Loading())
    val currentUser = _currentUser.asStateFlow()

    fun getCurrentUser() {
        viewModelScope.launch {
            try {
                var user = mainRepository.getCurrentUser()
                _currentUser.value = user
            } catch (e : Exception) {
                _currentUser.value = DatabaseResponse.Error(e.message.toString());
            }
        }
    }

    private val _deleteUserResponse =
        MutableStateFlow<DatabaseResponse<Boolean>>(DatabaseResponse.Loading())
    val deleteUserResponse = _deleteUserResponse.asStateFlow()

    fun deleteUser(user : User) {
        viewModelScope.launch {
            try {
                var response = mainRepository.deleteUser(user)
                _deleteUserResponse.value = response
            } catch (e : Exception) {
                _deleteUserResponse.value = DatabaseResponse.Error(e.message.toString());
            }
        }
    }
}