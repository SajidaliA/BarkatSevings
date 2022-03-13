package com.barkat.barkatsevings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.barkat.barkatsevings.data.Saving
import com.barkat.barkatsevings.data.User
import com.barkat.barkatsevings.repository.MainRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application
) :
    AndroidViewModel(application) {

    fun getSavings() = mainRepository.getUserSavings()
    fun getAllSavings() = mainRepository.getAllSavings()

    fun getUserTotalSavings(savingList : ArrayList<Saving>) : String {
        var totalSavings = 0
        savingList.map {
            totalSavings += it.amount?.toInt()!!
        }
        return totalSavings.toString()
    }

    fun getCurrentUser() : FirebaseUser? = mainRepository.getCurrentUser()

    fun updateUserData(user: User) = mainRepository.updateUser(user)

}