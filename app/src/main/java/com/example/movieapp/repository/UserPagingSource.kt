package com.example.movieapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.api.ApiService
import com.example.movieapp.data.User
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        // The page number to request. If params.key is null, it's the first page, so we load page 1.
        val page = params.key ?: 1

        return try {
            // Make the API call to fetch users for the current page.
            val response = apiService.getUsers(page)
            val users = response.data

            // We successfully loaded data.
            LoadResult.Page(
                data = users,
                // The key for the previous page, or null if this is the first page.
                prevKey = if (page == 1) null else page - 1,
                // The key for the next page, or null if we have reached the end of the data.
                nextKey = if (users.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            // IOException means there was a network error.
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            // HttpException means there was a non-2xx response from the server.
            LoadResult.Error(exception)
        }
    }

    /**
     * This function is used to determine which page to load when data needs to be refreshed.
     */
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}