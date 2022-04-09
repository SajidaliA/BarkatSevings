package com.barkat.barkatsevings.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkat.barkatsevings.data.Saving
import com.barkat.barkatsevings.data.User
import com.barkat.barkatsevings.utils.*
import com.barkat.barkatsevings.view.adapter.SavingsAdapter
import com.barkat.barkatsevings.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var userName: String? = ""
    private var userEmail: String? = ""

    @Inject
    lateinit var mPreferenceProvider: PreferenceProvider
    private lateinit var mBinding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val savingList: ArrayList<Saving> = ArrayList()
    private val allSavingList: ArrayList<Saving> = ArrayList()

    private val savingsAdapter = SavingsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkInternet()
        checkUserDetails()
        setupObserver()
        setClickListeners()
    }

    private fun checkInternet() {
        if (context?.let { checkConnection(it) } == false) showSnackBar(mBinding.root, "No internet connection")
    }

    private fun checkUserDetails() {
        viewModel.getCurrentUser()?.apply {
            if (displayName.isNullOrEmpty() && phoneNumber.isNullOrEmpty()) {
                showAlert(getString(R.string.please_add_your_name_and_mobile_number_form_profile))
            }
            context?.let {
                Glide.with(it).load(photoUrl).circleCrop()
                    .placeholder(R.drawable.ic_user_placeholder).into(mBinding.imgUser)
            }
            if (email.toString() == ADMIN_EMAIL) {
                mBinding.btnAddNewSaving.show()
                mBinding.lineBottom.show()
            } else {
                mBinding.btnAddNewSaving.hide()
                mBinding.lineBottom.hide()
            }
            mBinding.txtUserName.text = displayName
            userName = displayName
            userEmail = email
        }
    }

    private fun setupObserver() {
        viewModel.getSavings().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty() && it[0].amount != null) {
                savingList.clear()
                savingList.addAll(it)
                mBinding.txtUserTotalSaving.text =
                    getString(R.string.savings, viewModel.getUserTotalSavings(it))
                setView()
                updateUserData(viewModel.getUserTotalSavings(it))
                mBinding.imgNoData.hide()
                mBinding.txtNoData.hide()
                mBinding.scrollView.hide()
            } else {
                mBinding.imgNoData.show()
                mBinding.txtNoData.show()
                mBinding.scrollView.show()
            }
            mBinding.progressBar.hide()
        }

        viewModel.getAllSavings().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                allSavingList.clear()
                allSavingList.addAll(it)
                mBinding.txtGroupSaving.text =
                    getString(R.string.savings, viewModel.getUserTotalSavings(it))
                mBinding.progressBar.hide()
            }
        }
    }

    private fun updateUserData(totalSavings: String) {
        viewModel.updateUserData(
            User(
                mPreferenceProvider.getValue(USER_ID, ""),
                userEmail,
                "",
                userName,
                totalSavings,
                "",
                ""
            )
        )
    }

    private fun setClickListeners() {
        mBinding.apply {
            btnAddNewSaving.setOnClickListener {
                btnAddNewSaving.isEnabled = false
                activity?.addReplaceFragment(
                    R.id.container_main, AddNewSavingFragment(), true,
                    addToBackStack = true, true
                )
                btnAddNewSaving.isEnabled = true
            }
        }
    }

    private fun setView() {
        mBinding.rvSavings.layoutManager = LinearLayoutManager(context)
        mBinding.rvSavings.adapter = savingsAdapter
        updateData()
    }

    private fun updateData() {
        savingsAdapter.submitList(savingList)
    }

    override fun onResume() {
        super.onResume()
        checkInternet()
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(context).apply {
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
        activity?.addReplaceFragment(
            R.id.container_main, ProfileFragment(), true,
            addToBackStack = true, true
        )
    }
}