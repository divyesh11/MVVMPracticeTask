package com.example.mvvmpracticetask.utils

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.util.Calendar

object AppUtils {
    fun showToast(context : Context,msg : String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
    fun showToastLong(context : Context,msg : String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    fun calculateAge(selectedTimeInMillis: Long): Int {
        val selectedDate = Calendar.getInstance()
        selectedDate.timeInMillis = selectedTimeInMillis

        val currentDate = Calendar.getInstance()

        val selectedYear = selectedDate.get(Calendar.YEAR)
        val selectedMonth = selectedDate.get(Calendar.MONTH)
        val selectedDay = selectedDate.get(Calendar.DAY_OF_MONTH)

        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)

        var age = currentYear - selectedYear

        if (currentMonth < selectedMonth || (currentMonth == selectedMonth && currentDay < selectedDay)) {
            age--
        }

        return age
    }

    fun getFormattedDateOfBirth(timeInMillis : Long?) : String {
        if(timeInMillis == null) {
            return "N/A"
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var monthName = DateFormatSymbols().months[month]
        val formattedDate = "${monthName.substring(0,3)} ${day.toString()} ${year}"
        return formattedDate
    }
}