package com.barkat.barkatsevings.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barkat.barkatsevings.data.Saving
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.ItemSavingBinding

/**
 * Created by Sajid Ali Suthar.
 */

class SavingsAdapter : ListAdapter<Saving, SavingsAdapter.TestViewHolder>(MyDiffUtil()) {


    class TestViewHolder(var binding: ItemSavingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        return TestViewHolder(
            ItemSavingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val saving = getItem(position)
        saving.amount = holder.binding.root.context.getString(R.string.savings, saving.amount)
        holder.binding.data = saving
    }


    class MyDiffUtil :
        DiffUtil.ItemCallback<Saving>() {

        override fun areItemsTheSame(oldItem: Saving, newItem: Saving): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Saving, newItem: Saving): Boolean {
            return oldItem == newItem
        }
    }
}