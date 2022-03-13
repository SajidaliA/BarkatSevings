package com.barkat.barkatsevings.repository

import com.barkat.barkatsevings.helper.FirebaseHelper
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
@ActivityRetainedScoped
class LoginRepository @Inject constructor(val firebaseHelper: FirebaseHelper) {
}