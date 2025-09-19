package com.example.movieapp.api

import com.example.movieapp.data.AddUserRequest  // <-- New import
import com.example.movieapp.data.AddUserResponse // <-- New import
import com.example.movieapp.data.UserResponse
import retrofit2.http.Body // <-- New import
import retrofit2.http.GET
import retrofit2.http.POST // <-- New import
import retrofit2.http.Query

interface ApiService {

    @GET("api/users")
    suspend fun getUsers(@Query("page") page: Int): UserResponse

    // --- NEW FUNCTION TO ADD ---
    @POST("api/users")
    suspend fun addUser(@Body addUserRequest: AddUserRequest): AddUserResponse
}