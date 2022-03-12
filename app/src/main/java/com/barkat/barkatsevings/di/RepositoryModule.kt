package com.barkat.barkatsevings.di

import com.barkat.barkatsevings.helper.FirebaseHelper
import com.barkat.barkatsevings.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Sajid Ali Suthar.
 */

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(firebaseHelper: FirebaseHelper) =
        MainRepository(firebaseHelper)
}