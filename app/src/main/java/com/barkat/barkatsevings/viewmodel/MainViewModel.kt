package com.barkat.barkatsevings.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.barkat.barkatsevings.STATUS_DONE
import com.barkat.barkatsevings.data.Saving
import com.barkat.barkatsevings.repository.LoginRepository
import com.example.barkatsevings.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    application: Application
) :
    AndroidViewModel(application) {

    fun setData(context: Context): ArrayList<Saving> {
        val savingList: ArrayList<Saving> = ArrayList()
        savingList.add(
            Saving(
                0,
                "January",
                "2021",
                "02/01/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )
        savingList.add(
            Saving(
                0,
                "February",
                "2021",
                "03/02/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )
        savingList.add(
            Saving(
                0,
                "March",
                "2021",
                "02/03/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )
        savingList.add(
            Saving(
                0,
                "April",
                "2021",
                "05/04/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )
        savingList.add(
            Saving(
                0,
                "May",
                "2021",
                "02/05/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )
        savingList.add(
            Saving(
                0,
                "Jun",
                "2021",
                "02/06/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )
        savingList.add(
            Saving(
                0,
                "July",
                "2021",
                "02/07/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )
        savingList.add(
            Saving(
                0,
                "January",
                "2021",
                "02/02/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )
        savingList.add(
            Saving(
                0,
                "January",
                "2021",
                "02/02/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )
        savingList.add(
            Saving(
                0,
                "January",
                "2021",
                "02/02/2021",
                STATUS_DONE,
                "${context.getString(R.string.rupee_symbol)}1000"
            )
        )

        return savingList
    }
}