package com.example.movieapp.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MovieDetailResponse
import com.example.movieapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle // Hilt provides this automatically
) : ViewModel() {

    private val _movieState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val movieState = _movieState.asStateFlow()

    init {
        // Retrieve the movieId passed through navigation
        val movieId: Int? = savedStateHandle["movieId"]
        if (movieId != null) {
            fetchMovieDetails(movieId)
        } else {
            _movieState.value = MovieUiState.Error("Movie ID not found")
        }
    }

    private fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            repository.getMovieDetails(movieId)
                .onSuccess { movieDetails ->
                    _movieState.value = MovieUiState.Success(movieDetails)
                }
                .onFailure { error ->
                    _movieState.value = MovieUiState.Error(error.localizedMessage ?: "An error occurred")
                }
        }
    }
}

// Sealed interface to represent the different UI states
sealed interface MovieUiState {
    object Loading : MovieUiState
    data class Success(val movie: MovieDetailResponse) : MovieUiState
    data class Error(val message: String) : MovieUiState
}