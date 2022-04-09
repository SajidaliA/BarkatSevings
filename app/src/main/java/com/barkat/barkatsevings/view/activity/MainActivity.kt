package com.barkat.barkatsevings.view.activity

import android.os.Bundle
import com.barkat.barkatsevings.utils.HOME_TAB
import com.barkat.barkatsevings.utils.MEMBERS_TAB
import com.barkat.barkatsevings.utils.PROFILE_TAB
import com.barkat.barkatsevings.utils.addReplaceFragment
import com.barkat.barkatsevings.view.fragment.HomeFragment
import com.barkat.barkatsevings.view.fragment.MembersFragment
import com.barkat.barkatsevings.view.fragment.ProfileFragment
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.ActivityMainBottomNavBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBottomNavBinding
    private var currentFragment: String = HOME_TAB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBottomNavBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        loadHomeFragment()

        mBinding.navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    if (currentFragment != HOME_TAB) loadHomeFragment()
                    true
                }
                R.id.navigation_members -> {
                    if (currentFragment != MEMBERS_TAB){
                        addReplaceFragment(
                            R.id.container_bottom_nav, MembersFragment(), false,
                            addToBackStack = false, false
                        )
                        currentFragment = MEMBERS_TAB
                    }
                    true
                }
                R.id.navigation_profile -> {
                    if (currentFragment != PROFILE_TAB){
                        addReplaceFragment(
                            R.id.container_bottom_nav, ProfileFragment(), false,
                            addToBackStack = false, false
                        )
                        currentFragment = PROFILE_TAB
                    }
                    true
                }
                else -> {
                    loadHomeFragment()
                    true
                }
            }
        }
    }

    private fun loadHomeFragment() {
        addReplaceFragment(
            R.id.container_bottom_nav, HomeFragment(), false,
            addToBackStack = false, false
        )
        currentFragment = HOME_TAB
    }

    override fun onBackPressed() {
        if (currentFragment != HOME_TAB){
            loadHomeFragment()
        }else{
            super.onBackPressed()
        }
    }
}