package com.barkat.barkatsevings.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barkat.barkatsevings.data.User
import com.barkat.barkatsevings.utils.ADMIN_EMAIL
import com.barkat.barkatsevings.utils.hide
import com.barkat.barkatsevings.utils.show
import com.example.barkatsevings.R
import com.example.barkatsevings.databinding.ItemUserBinding

/**
 * Created by Sajid Ali Suthar.
 */

class UserAdapter : ListAdapter<User, UserAdapter.TestViewHolder>(UserDiffUtil()) {

    class TestViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        return TestViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val user = getItem(position)
        if (user.totalSavings?.contains(holder.binding.root.context.getString(R.string.rupee_symbol)) == false){
            user.totalSavings = holder.binding.root.context.getString(R.string.savings, user.totalSavings)
        }
        holder.binding.data = user
    }


    class UserDiffUtil :
        DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}