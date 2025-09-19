package com.example.movieapp.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentUserListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()

    // --- THIS IS THE KEY CHANGE ---
    // Initialize the adapter with a click listener lambda.
    private val userAdapter = UserAdapter { user ->
        // When a user is clicked, navigate using the action we defined.
        // We can optionally pass user data here in the future if needed.
        findNavController().navigate(R.id.action_userListFragment_to_movieListFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeUsers()
        observeLoadingState()

        binding.fabAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_userListFragment_to_addUserFragment)
        }
    }

    private fun setupRecyclerView() {
        binding.userRecyclerView.apply {
            adapter = userAdapter
        }
    }
    //... the rest of the file is unchanged ...
    private fun observeUsers() {
        // Use viewLifecycleOwner.lifecycleScope for Flows in Fragments
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.users.collectLatest { pagingData ->
                userAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            userAdapter.loadStateFlow.collectLatest { loadStates ->
                // Show progress bar only on the initial load.
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}