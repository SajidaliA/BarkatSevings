package com.barkat.barkatsevings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.barkat.barkatsevings.data.Saving
import com.barkat.barkatsevings.data.User
import com.barkat.barkatsevings.repository.HomeRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: HomeRepository,
    application: Application
) :
    AndroidViewModel(application) {

    fun getSavings() = mainRepository.getUserSavings()
    fun getAllSavings() = mainRepository.getAllSavings()

    fun getUserTotalSavings(savingList : ArrayList<Saving>?) : String {
        var totalSavings = 0
        savingList?.map { saving ->
            if(saving.amount != null){
                totalSavings += saving.amount?.toInt()!!
            }
        }
        return totalSavings.toString()
    }

    fun getCurrentUser() : FirebaseUser? = mainRepository.getCurrentUser()

    fun updateUserData(user: User) = mainRepository.updateUser(user)

}