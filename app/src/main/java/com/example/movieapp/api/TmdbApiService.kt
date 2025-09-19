package com.example.movieapp.api

import com.example.movieapp.data.MovieDetailResponse
import com.example.movieapp.data.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("3/trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String,
        // --- THIS LINE IS THE FIX ---
        @Query("page") page: Int // It was incorrectly @Query.Query("page")
    ): MovieListResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailResponse
}