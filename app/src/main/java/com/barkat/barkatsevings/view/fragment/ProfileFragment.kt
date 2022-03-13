package com.barkat.barkatsevings.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.barkat.barkatsevings.helper.FirebaseHelper
import com.barkat.barkatsevings.utils.PreferenceProvider
import com.bumptech.glide.Glide
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.FragmentProfieBinding
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var mPreferenceProvider: PreferenceProvider
    private lateinit var mBinding: FragmentProfieBinding
    private lateinit var firebaseHelper: FirebaseHelper
    private var photoUrl = "add storage url here"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseHelper = FirebaseHelper(mPreferenceProvider)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentProfieBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            firebaseHelper.checkUserLoggedIn()?.apply {
                txtEmail.text = email
                edtName.setText(displayName)
                edtMobile.setText(phoneNumber)
                context?.let { Glide.with(it).load(photoUrl).circleCrop().placeholder(R.drawable.ic_user_placeholder).into(imgUserProfile) }
            }
        }
        setOnClickListener()
    }

    private fun setOnClickListener() {
        mBinding.btnUpdate.setOnClickListener {
            val name = mBinding.edtName.text.toString()
//            val mobileNumber = mBinding.edtMobile.text.toString()
            when {
                name.isEmpty() -> {
                    mBinding.edtName.error = "please enter your name"
                    mBinding.edtName.requestFocus()
                }
//                mobileNumber.isEmpty() -> {
//                    mBinding.edtMobile.error = "please enter mobile number"
//                    mBinding.edtName.requestFocus()
//                }
//                mobileNumber.length<10 -> {
//                    mBinding.edtMobile.error = "please enter valid mobile number"
//                    mBinding.edtName.requestFocus()
//                }
            }
            firebaseHelper.updateNameAndMobileNumber(name, photoUrl)
        }
    }

}