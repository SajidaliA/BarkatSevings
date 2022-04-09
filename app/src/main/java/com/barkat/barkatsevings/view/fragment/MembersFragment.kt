package com.barkat.barkatsevings.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkat.barkatsevings.data.User
import com.barkat.barkatsevings.helper.FirebaseHelper
import com.barkat.barkatsevings.utils.PreferenceProvider
import com.barkat.barkatsevings.view.adapter.UserAdapter
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.FragmentMembersBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MembersFragment : Fragment() {
    @Inject
    lateinit var mPreferenceProvider: PreferenceProvider
    private lateinit var mBinding: FragmentMembersBinding
    private lateinit var firebaseHelper: FirebaseHelper
    private var mUserList: ArrayList<User> = ArrayList()
    private var userAdapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseHelper = FirebaseHelper(mPreferenceProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMembersBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUsrList()
    }

    private fun getUsrList() {
        firebaseHelper.getUserListFromServer().observe(viewLifecycleOwner) { it ->
            it?.let { userList ->
                mUserList.clear()
                mUserList.addAll(userList)
                setView()
            }
        }
    }

    private fun setView() {
        mBinding.rvUsers.layoutManager = LinearLayoutManager(context)
        mBinding.rvUsers.adapter = userAdapter
        updateData()
    }

    private fun updateData() {
        userAdapter.submitList(mUserList)
    }
}