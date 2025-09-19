package com.example.movieapp.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.movieapp.data.User
import com.example.movieapp.databinding.ItemUserBinding

// Note the change in the constructor here
class UserAdapter(private val onUserClicked: (User) -> Unit) : PagingDataAdapter<User, UserAdapter.UserViewHolder>(UserComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // This setup block now handles the click
        init {
            binding.root.setOnClickListener {
                // Get the user at the current adapter position
                getItem(bindingAdapterPosition)?.let { user ->
                    // Invoke the lambda passed into the adapter
                    onUserClicked(user)
                }
            }
        }

        fun bind(user: User) {
            binding.apply {
                tvFirstName.text = user.first_name
                tvLastName.text = user.last_name
                ivAvatar.load(user.avatar) {
                    crossfade(true)
                    placeholder(android.R.drawable.ic_menu_gallery)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

    object UserComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}