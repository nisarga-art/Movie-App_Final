package com.example.movieapp.data

import com.google.gson.annotations.SerializedName

// This class defines the detailed movie response object
data class MovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String
)