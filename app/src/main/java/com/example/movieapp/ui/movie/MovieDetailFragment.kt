package com.example.movieapp.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModels()
    private val posterBaseUrl = "https://image.tmdb.org/t/p/w500" // Use a larger image size

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieState.collectLatest { state ->
                // Control visibility based on state
                binding.progressBar.isVisible = state is MovieUiState.Loading
                binding.contentGroup.isVisible = state is MovieUiState.Success
                binding.tvError.isVisible = state is MovieUiState.Error

                if (state is MovieUiState.Success) {
                    val movie = state.movie
                    binding.apply {
                        ivMoviePoster.load(posterBaseUrl + movie.posterPath) {
                            crossfade(true)
                            placeholder(android.R.drawable.ic_menu_gallery)
                        }
                        tvMovieTitle.text = movie.title
                        tvReleaseDate.text = "Released: ${movie.releaseDate}"
                        tvOverview.text = movie.overview
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