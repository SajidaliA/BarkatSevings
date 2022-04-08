package com.barkat.barkatsevings.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.barkat.barkatsevings.data.Saving
import com.barkat.barkatsevings.helper.FirebaseHelper
import com.barkat.barkatsevings.utils.PreferenceProvider
import com.barkat.barkatsevings.utils.hideKeyboard
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.FragmentAddNewSavingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddNewSavingFragment : Fragment() {

    @Inject
    lateinit var mPreferenceProvider: PreferenceProvider
    private lateinit var mBinding: FragmentAddNewSavingBinding
    private lateinit var firebaseHelper: FirebaseHelper
    private var selectedUserId: String? = null
    private var selectedMonth: String? = null
    private var selectedYear: String? = null
    private var userArray: ArrayList<String> = ArrayList()
    private var monthArray: Array<String> = arrayOf(
        "Select Month",
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    private val mCalendar: Calendar = Calendar.getInstance()
    private val year: Int = mCalendar.get(Calendar.YEAR)
    private val month: Int = mCalendar.get(Calendar.MONTH)
    private val dayOfMonth: Int = mCalendar.get(Calendar.DAY_OF_MONTH)

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
        mBinding = FragmentAddNewSavingBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setData()
        firebaseHelper.getUserListFromServer().observe(viewLifecycleOwner) { it ->
            it?.let { userList ->
                userArray.add("Select User")
                userArray = userList.map { it.id.toString() } as ArrayList<String>
                context?.let {
                    ArrayAdapter(it, R.layout.spinner_item_user, userArray)
                        .also { adapter ->
                            adapter.setDropDownViewResource(R.layout.spinner_item_user)
                            mBinding.spinnerUser.adapter = adapter
                        }
                }
            }
        }

        mBinding.spinnerUser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!userArray.isNullOrEmpty()) {
                    selectedUserId = userArray[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
        mBinding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMonth = monthArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun setData() {
        context?.let {
            ArrayAdapter(it, R.layout.spinner_item_user, monthArray)
                .also { adapter ->
                    adapter.setDropDownViewResource(R.layout.spinner_item_user)
                    mBinding.spinnerMonth.adapter = adapter
                }
        }
        val selectedDate: String =
            DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.time)
        mBinding.txtDate.text = selectedDate
        setYearSelection(year.toString())
    }

    private fun setClickListeners() {
        mBinding.apply {
            btnAdd.setOnClickListener {
                when {
                    selectedUserId.isNullOrEmpty() -> {
                        Toast.makeText(context, "Please select user", Toast.LENGTH_SHORT).show()
                    }
                    selectedMonth == "Select Month" -> {
                        Toast.makeText(context, "Please select month", Toast.LENGTH_SHORT).show()
                    }
                    edtAmount.text.toString().isEmpty() -> {
                        edtAmount.requestFocus()
                        edtAmount.error = "Please enter amount"
                    }
                    else -> {
                        val saving = Saving(
                            0,
                            selectedMonth,
                            selectedYear,
                            txtDate.text.toString(),
                            "Done",
                            edtAmount.text.toString()
                        )
                        context?.let { it1 -> firebaseHelper.addSaving(selectedUserId, saving, it1) }
                        activity?.hideKeyboard()
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
            }
            txt2021.setOnClickListener {
                setYearSelection(getString(R.string._2021))

            }
            txt2022.setOnClickListener {
                setYearSelection(getString(R.string._2022))
            }
            txt2023.setOnClickListener {
                setYearSelection(getString(R.string._2023))
            }
            txtDate.setOnClickListener {
                context?.let { it1 ->
                    DatePickerDialog(
                        it1, { _, year, month, dayOfMonth ->
                            mCalendar[Calendar.YEAR] = year
                            mCalendar[Calendar.MONTH] = month
                            mCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                            val selectedDate: String =
                                DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.time)
                            txtDate.text = selectedDate
                        },
                        year,
                        month,
                        dayOfMonth
                    ).show()
                }
            }
        }

    }

    private fun setYearSelection(selectedYear: String) {
        this.selectedYear = selectedYear
        mBinding.apply {
            context?.let {
                txt2021.setTextColor(ContextCompat.getColor(it, R.color.color_main))
                txt2022.setTextColor(ContextCompat.getColor(it, R.color.color_main))
                txt2023.setTextColor(ContextCompat.getColor(it, R.color.color_main))
                txt2021.background = ContextCompat.getDrawable(
                    it,
                    R.drawable.shape_border_green_20
                )
                txt2022.background = ContextCompat.getDrawable(
                    it,
                    R.drawable.shape_border_green_20
                )
                txt2023.background = ContextCompat.getDrawable(
                    it,
                    R.drawable.shape_border_green_20
                )

                when (selectedYear) {
                    getString(R.string._2021) -> {
                        txt2021.background = ContextCompat.getDrawable(
                            it,
                            R.drawable.shape_border_blue_selected_20
                        )
                        txt2021.setTextColor(ContextCompat.getColor(it, R.color.white))
                    }

                    getString(R.string._2022) -> {
                        txt2022.background = ContextCompat.getDrawable(
                            it,
                            R.drawable.shape_border_blue_selected_20
                        )
                        txt2022.setTextColor(ContextCompat.getColor(it, R.color.white))
                    }

                    getString(R.string._2023) -> {
                        txt2023.background = ContextCompat.getDrawable(
                            it,
                            R.drawable.shape_border_blue_selected_20
                        )
                        txt2023.setTextColor(ContextCompat.getColor(it, R.color.white))
                    }
                }
            }
        }
    }
}