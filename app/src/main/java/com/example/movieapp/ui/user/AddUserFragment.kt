package com.example.movieapp.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.databinding.FragmentAddUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddUserFragment : Fragment() {

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!

    // Get an instance of the AddUserViewModel using Hilt's delegate.
    private val viewModel: AddUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeEvents()
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val job = binding.etJob.text.toString()
            // Tell the ViewModel to save the user
            viewModel.saveUser(name, job)
        }
    }

    private fun observeEvents() {
        // Observe the one-time events from the ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AddUserViewModel.UiEvent.ShowToast -> {
                        // Show a toast message
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }
                    is AddUserViewModel.UiEvent.NavigateBack -> {
                        // Navigate back to the previous screen (the user list)
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}