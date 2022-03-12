package com.barkat.barkatsevings.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.FragmentAddNewSavingBinding


class AddNewSavingFragment : Fragment() {
    private lateinit var mBinding: FragmentAddNewSavingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddNewSavingBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            ArrayAdapter.createFromResource(it, R.array.planets_array, R.layout.spinner_item_user)
                .also { adapter ->
                    adapter.setDropDownViewResource(R.layout.spinner_item_user)
                    mBinding.spinnerUser.adapter = adapter
                }
        }

        mBinding.spinnerUser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }


}