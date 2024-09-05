package com.example.mvvmpracticetask.ui.login_feature.fragments

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvvmpracticetask.R
import com.example.mvvmpracticetask.constants.SharedPreferenceKeys
import com.example.mvvmpracticetask.databinding.FragmentEnterUserDetailsBinding
import com.example.mvvmpracticetask.db.models.User
import com.example.mvvmpracticetask.utils.AppUtils
import com.example.mvvmpracticetask.utils.DatabaseResponse
import com.example.mvvmpracticetask.utils.FormValidator
import com.example.mvvmpracticetask.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.prefs.Preferences
import javax.inject.Inject

@AndroidEntryPoint
class EnterUserDetailsFragment : Fragment() {

    companion object {
        const val TAG = "EnterUserDetailsFragment"
    }

    @Inject
    lateinit var appSharedPreferences: SharedPreferences

    private lateinit var viewBinding : FragmentEnterUserDetailsBinding

    private val mainViewModel : MainViewModel by activityViewModels()

    private var datePickerDialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentEnterUserDetailsBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserAlreadyLoggedIn()
        configureUI()
    }

    private fun checkUserAlreadyLoggedIn() {
        val isUserLoggedIn = appSharedPreferences.getBoolean(SharedPreferenceKeys.IS_USER_LOGGED_IN_KEY,false)
        if(isUserLoggedIn == true) {
            navigateToShowUserDetails()
        }
    }

    private fun configureUI() {
        setSaveButtonState(false)
        initializeDatePickerDialog()
        viewBinding.btnSave.setOnClickListener {
            // save user details
            saveUserDetails()
        }

        viewBinding.datePickerButton.setOnClickListener {
            showCalenderPicker()
        }
    }

    private fun saveUserDetails() {
        if(!isDetailsValid()) {
            var errorMsg = buildErrorMsg()
            AppUtils.showToastLong(this.requireContext(),errorMsg)
        } else {
            val name = viewBinding.editTextUserName.text.toString()
            val age = getAgeBySelectedDate()
            val dobInMillis = getDatePickerTimeInMillis()
            val address = viewBinding.editTextAddress.text.toString()
            mainViewModel.addNewUserDetails(
                User(
                    name = name,
                    age = age,
                    dateOfBirthTimeInMillis = dobInMillis,
                    address = address
                )
            )
            lifecycleScope.launch {
                mainViewModel.addUserDetails.collect {
                    when(it) {
                        is DatabaseResponse.Loading -> {
                            setSaveButtonState(true)
                        }
                        is DatabaseResponse.Success -> {
                            setSaveButtonState(false)
                            setUserLoggedIn()
                            AppUtils.showToastLong(this@EnterUserDetailsFragment.requireContext(),it.data.toString())
                            navigateToShowUserDetails()
                        }
                        is DatabaseResponse.Error -> {
                            setSaveButtonState(false)
                            AppUtils.showToastLong(this@EnterUserDetailsFragment.requireContext(),it.message.toString())
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setUserLoggedIn() {
        appSharedPreferences.edit().putBoolean(SharedPreferenceKeys.IS_USER_LOGGED_IN_KEY,true).commit()
    }

    private fun navigateToShowUserDetails() {
        findNavController().navigate(R.id.action_enterUserDetailsFragment_to_showUserDetailsFragment)
    }

    private fun setSaveButtonState(isLoading : Boolean) {
        if(isLoading) {
            viewBinding.progressBar.visibility = View.VISIBLE
            viewBinding.btnSave.visibility = View.GONE
        } else {
            viewBinding.progressBar.visibility = View.GONE
            viewBinding.btnSave.visibility = View.VISIBLE
        }
    }

    private fun buildErrorMsg(): String {
        var errorMsg = ""
        if(viewBinding.editTextUserName.text.isNullOrEmpty()) {
            errorMsg += "Username should not be empty!"
        }
        if(viewBinding.editTextAddress.text.isNullOrEmpty()) {
            if(!errorMsg.isNullOrEmpty()) {
                errorMsg += "\n"
            }
            errorMsg += "Address should not be empty!"
        }
        if(getAgeBySelectedDate() < 18) {
            if(!errorMsg.isNullOrEmpty()) {
                errorMsg += "\n"
            }
            errorMsg += "Age should be 18 or greater!"
        }
        return errorMsg
    }

    private fun isDetailsValid(): Boolean {
        return FormValidator.isFormValid(viewBinding.editTextUserName.text.toString(),viewBinding.editTextAddress.text.toString(),getDatePickerTimeInMillis())
    }

    private fun getAgeBySelectedDate(): Int {
        val timeInMillis = getDatePickerTimeInMillis()
        return AppUtils.calculateAge(timeInMillis)
    }

    private fun getDatePickerTimeInMillis() : Long {
        val dayOfMonth = datePickerDialog?.datePicker?.dayOfMonth!!
        val month = datePickerDialog?.datePicker?.month!!
        val year = datePickerDialog?.datePicker?.year!!
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, dayOfMonth)

        val timeInMillis = selectedCalendar.timeInMillis
        return timeInMillis
    }

    private fun initializeDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var monthName = DateFormatSymbols().months[month]
        val currentDate = "${monthName.substring(0,3)} ${day.toString()} ${year}"
        viewBinding.datePickerButton.text = currentDate

        datePickerDialog = DatePickerDialog(this.requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                monthName = DateFormatSymbols().months[selectedMonth]

                val selectedDate = "${monthName.substring(0,3)} ${selectedDay.toString()} ${selectedYear}"
                viewBinding.datePickerButton.text = selectedDate
            }, year, month, day)
        datePickerDialog?.datePicker?.maxDate = calendar.timeInMillis
    }

    private fun showCalenderPicker() {
        datePickerDialog?.show()
    }
}