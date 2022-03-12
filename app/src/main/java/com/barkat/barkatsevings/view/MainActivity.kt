package com.barkat.barkatsevings.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkat.barkatsevings.STATUS_DONE
import com.barkat.barkatsevings.data.Saving
import com.barkat.barkatsevings.utils.addReplaceFragment
import com.barkat.barkatsevings.view.adapter.SavingsAdapter
import com.barkat.barkatsevings.view.fragment.AddNewSavingFragment
import com.barkat.barkatsevings.viewmodel.MainViewModel
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var mBinding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val savingList: ArrayList<Saving> = ArrayList()

    private val savingsAdapter = SavingsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        savingList.addAll(viewModel.setData(this))
        setView()
        setClickListeners()
    }

    private fun setClickListeners() {
        mBinding.apply {
            btnAddNewSaving.setOnClickListener {
                addReplaceFragment(R.id.container_main, AddNewSavingFragment(), true,
                    addToBackStack = true
                )
            }
            imgUser.setOnClickListener {
                Snackbar.make(root, "Module not ready yet!", Snackbar.LENGTH_SHORT).show()
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