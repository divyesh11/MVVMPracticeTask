package com.example.mvvmpracticetask.ui.login_feature.fragments

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvvmpracticetask.constants.SharedPreferenceKeys
import com.example.mvvmpracticetask.databinding.FragmentShowUserDetailsBinding
import com.example.mvvmpracticetask.db.models.User
import com.example.mvvmpracticetask.utils.AppUtils
import com.example.mvvmpracticetask.utils.DatabaseResponse
import com.example.mvvmpracticetask.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShowUserDetailsFragment : Fragment() {

    companion object {
        const val TAG = "ShowUserDetailsFragment"
    }

    @Inject
    lateinit var appSharedPreferences: SharedPreferences

    private val mainViewModel : MainViewModel by activityViewModels()

    private lateinit var viewBinding : FragmentShowUserDetailsBinding

    private var currentUser : User? = null

    private var logoutDialog : AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentShowUserDetailsBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
        getUserDetails()
    }

    private fun configureUI() {
        viewBinding.btnLogout.setOnClickListener {
            confirmLogout {
                logoutUser()
            }
        }
    }

    private fun confirmLogout(onConfirmLogout : () -> Unit) {
        if(logoutDialog != null) {
            logoutDialog?.show()
            return
        }
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")

        builder.setPositiveButton("Logout") { dialog, _ ->
            onConfirmLogout()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        logoutDialog = builder.create()
        if(logoutDialog != null) {
            logoutDialog?.show()
        }
    }

    private fun logoutUser() {
        if(currentUser == null) {
            navigateBackToEnterUserDetails()
            return
        }
        mainViewModel.deleteUser(currentUser!!)
        lifecycleScope.launch {
            mainViewModel.deleteUserResponse.collect {
                when(it) {
                    is DatabaseResponse.Loading -> {
                        setLogOutButtonState(true)
                    }
                    is DatabaseResponse.Success -> {
                        setLogOutButtonState(false)
                        if(it.data!= null && it.data == true) {
                            navigateBackToEnterUserDetails()
                        } else {
                            AppUtils.showToastLong(this@ShowUserDetailsFragment.requireContext(),it.message.toString())
                        }
                    }
                    is DatabaseResponse.Error -> {
                        setLogOutButtonState(false)
                        AppUtils.showToastLong(this@ShowUserDetailsFragment.requireContext(),it.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setLogOutButtonState(isLoading: Boolean) {
        if(isLoading) {
            viewBinding.progressBar.visibility = View.VISIBLE
            viewBinding.btnLogout.visibility = View.GONE
        } else {
            viewBinding.progressBar.visibility = View.GONE
            viewBinding.btnLogout.visibility = View.VISIBLE
        }
    }

    private fun navigateBackToEnterUserDetails() {
        appSharedPreferences.edit().putBoolean(SharedPreferenceKeys.IS_USER_LOGGED_IN_KEY,false).apply()
        findNavController().navigateUp()
    }

    private fun getUserDetails() {
        mainViewModel.getCurrentUser()
        lifecycleScope.launch {
            mainViewModel.currentUser.collect {
                when(it) {
                    is DatabaseResponse.Loading -> {
                        setUserDetailsState(true)
                    }
                    is DatabaseResponse.Success -> {
                        currentUser = it.data
                        showUserDetails()
                    }
                    is DatabaseResponse.Error -> {
                        AppUtils.showToastLong(this@ShowUserDetailsFragment.requireContext(),it.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showUserDetails() {
        var userDetails = ""
        userDetails += "Name : ${currentUser?.name.toString()}\n"
        userDetails += "Age : ${currentUser?.age.toString()}\n"
        userDetails += "DOB : ${AppUtils.getFormattedDateOfBirth(currentUser?.dateOfBirthTimeInMillis)}\n"
        userDetails += "Address : ${currentUser?.address.toString()}"
        viewBinding.tvUserDetails.text = userDetails
    }

    private fun setUserDetailsState(b: Boolean) {
        if(b) {
            viewBinding.tvUserDetails.text = "User Details Loading..."
        }
    }
}