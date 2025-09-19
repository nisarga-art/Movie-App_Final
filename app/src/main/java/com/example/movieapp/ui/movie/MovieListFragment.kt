package com.example.movieapp.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf // <-- New Import for creating a bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController // <-- New Import
import androidx.paging.LoadState
import com.example.movieapp.R // <-- New Import
import com.example.movieapp.databinding.FragmentMovieListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieListViewModel by viewModels()

    // --- THIS IS THE UPDATED PART ---
    // Initialize the adapter with a click listener lambda.
    private val movieAdapter = MovieAdapter { movie ->
        // When a movie is clicked, create a bundle to hold the movieId.
        // The key "movieId" MUST match the 'android:name' of the argument
        // we defined in the nav_graph.xml.
        val bundle = bundleOf("movieId" to movie.id)

        // Navigate using the action ID and pass the bundle as an argument.
        findNavController().navigate(R.id.action_movieListFragment_to_movieDetailFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeMovies()
        observeLoadingState()
    }

    private fun setupRecyclerView() {
        binding.movieRecyclerView.adapter = movieAdapter
    }

    private fun observeMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies.collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}