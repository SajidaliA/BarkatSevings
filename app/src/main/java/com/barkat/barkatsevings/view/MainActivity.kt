package com.barkat.barkatsevings.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkat.barkatsevings.data.Saving
import com.barkat.barkatsevings.data.User
import com.barkat.barkatsevings.utils.*
import com.barkat.barkatsevings.view.adapter.SavingsAdapter
import com.barkat.barkatsevings.view.fragment.AddNewSavingFragment
import com.barkat.barkatsevings.view.fragment.ProfileFragment
import com.barkat.barkatsevings.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    @Inject
    lateinit var mPreferenceProvider: PreferenceProvider
    private lateinit var mBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val savingList: ArrayList<Saving> = ArrayList()
    private val allSavingList: ArrayList<Saving> = ArrayList()

    private val savingsAdapter = SavingsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        checkUserDetails()
        setupObserver()
        setClickListeners()
    }

    private fun checkUserDetails() {
        viewModel.getCurrentUser()?.apply {
            if (displayName.isNullOrEmpty() && phoneNumber.isNullOrEmpty()) {
                showAlert(getString(R.string.please_add_your_name_and_mobile_number_form_profile))
            }
            Glide.with(this@MainActivity).load(photoUrl).circleCrop()
                .placeholder(R.drawable.ic_user_placeholder).into(mBinding.imgUser)
            if (email.toString() == ADMIN_EMAIL) {
                mBinding.btnAddNewSaving.show()
            } else {
                mBinding.btnAddNewSaving.hide()
            }
            viewModel.updateUserData(
                User(
                    mPreferenceProvider.getValue(USER_ID, ""),
                    email,
                    "",
                    displayName,
                    "0",
                    "",
                    ""
                )
            )
        }
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.complete_your_details))
            setMessage(message)
            setPositiveButton(
                getString(R.string.add)
            ) { dialog, _ ->
                dialog.dismiss()
                redirectToProfile()
            }
                .setCancelable(false)
                .show()
        }
    }

    private fun redirectToProfile() {
        addReplaceFragment(
            R.id.container_main, ProfileFragment(), true,
            addToBackStack = true
        )
    }

    private fun setupObserver() {
        viewModel.getSavings().observe(this) {
            if (!it.isNullOrEmpty()) {
                savingList.clear()
                savingList.addAll(it)
                mBinding.txtUserTotalSaving.text =
                    getString(R.string.savings, viewModel.getUserTotalSavings(it))
                setView()
                mBinding.imgNoData.hide()
                mBinding.txtNoData.hide()
            } else {
                mBinding.imgNoData.show()
                mBinding.txtNoData.show()
            }
            mBinding.progressBar.hide()
        }

        viewModel.getAllSavings().observe(this) {
            if (!it.isNullOrEmpty()) {
                allSavingList.clear()
                allSavingList.addAll(it)
                mBinding.txtGroupSaving.text =
                    getString(R.string.savings, viewModel.getUserTotalSavings(it))
            }
        }
    }

    private fun setClickListeners() {
        mBinding.apply {
            btnAddNewSaving.setOnClickListener {
                btnAddNewSaving.isEnabled = false
                addReplaceFragment(
                    R.id.container_main, AddNewSavingFragment(), true,
                    addToBackStack = true
                )
                btnAddNewSaving.isEnabled = true
            }
            imgUser.setOnClickListener {
                redirectToProfile()
            }
        }
    }

    private fun setView() {
        mBinding.rvSavings.layoutManager = LinearLayoutManager(this)
        mBinding.rvSavings.adapter = savingsAdapter
        updateData()
    }

    private fun updateData() {
        savingsAdapter.submitList(savingList)
    }
}