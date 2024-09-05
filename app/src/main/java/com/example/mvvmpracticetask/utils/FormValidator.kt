package com.example.mvvmpracticetask.utils

object FormValidator {
    fun isNameValid(name: String?): Boolean {
        return !name.isNullOrBlank()
    }

    fun isAgeValid(dobInMillis: Long) : Boolean {
        return AppUtils.calculateAge(dobInMillis) >= 18
    }

    fun isFormValid(name: String?, address : String?, dobInMillis : Long): Boolean {
        return isNameValid(name) && isAgeValid(dobInMillis) && isAddressValid(address)
    }

    private fun isAddressValid(address: String?): Boolean {
        return !address.isNullOrEmpty()
    }
}