package com.barkat.barkatsevings.view.activity

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBottomNavBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        loadHomeFragment()

        mBinding.navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                   loadHomeFragment()
                    true
                }
                R.id.navigation_members -> {
                    addReplaceFragment(
                        R.id.container_bottom_nav, MembersFragment(), false,
                        addToBackStack = true, false
                    )
                    true
                }
                R.id.navigation_profile -> {
                    addReplaceFragment(
                        R.id.container_bottom_nav, ProfileFragment(), false,
                        addToBackStack = true, false
                    )
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
            addToBackStack = true, false
        )
    }
}