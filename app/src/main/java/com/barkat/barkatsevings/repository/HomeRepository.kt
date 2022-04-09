package com.barkat.barkatsevings.repository

import androidx.lifecycle.LiveData
import com.barkat.barkatsevings.data.Saving
import com.barkat.barkatsevings.data.User
import com.barkat.barkatsevings.helper.FirebaseHelper
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
@ActivityRetainedScoped
class HomeRepository @Inject constructor(private val firebaseHelper: FirebaseHelper) {
    fun getUserSavings():LiveData<ArrayList<Saving>> = firebaseHelper.getUserSavingsFromServer()
    fun getAllSavings():LiveData<ArrayList<Saving>> = firebaseHelper.getTotalSavingsFromServer()
    fun getCurrentUser():FirebaseUser? = firebaseHelper.checkUserLoggedIn()
    fun updateUser(user: User) = firebaseHelper.updateUser(user)

}