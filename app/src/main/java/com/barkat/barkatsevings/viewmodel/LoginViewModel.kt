package com.barkat.barkatsevings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.barkat.barkatsevings.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository,
                                         @ApplicationContext context: Application) :
    AndroidViewModel(context) {
}