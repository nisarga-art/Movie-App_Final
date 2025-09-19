package com.example.movieapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.BuildConfig // <-- ADD THIS IMPORT
import com.example.movieapp.api.TmdbApiService
import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieDetailResponse // <-- ADD THIS IMPORT
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val tmdbApiService: TmdbApiService) {
    // ... getTrendingMovies() function is here ...
    fun getTrendingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(tmdbApiService) }
        ).flow
    }

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailResponse> {
        return try {
            val response = tmdbApiService.getMovieDetails(
                movieId = movieId,
                apiKey = BuildConfig.TMDB_API_KEY
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}