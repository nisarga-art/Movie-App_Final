package com.example.movieapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.api.ApiService
import com.example.movieapp.data.AddUserRequest
import com.example.movieapp.data.User
import com.example.movieapp.db.UserDao
import com.example.movieapp.db.UserEntity
import com.example.movieapp.util.NetworkConnectivity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao, // <-- ADDED
    private val networkConnectivity: NetworkConnectivity // <-- ADDED
) {

    fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 6, enablePlaceholders = false),
            pagingSourceFactory = { UserPagingSource(apiService) }
        ).flow
    }

    // --- NEW FUNCTION TO ADD ---
    /**
     * Attempts to add a user.
     * @return a Boolean indicating if the user was synced with the remote API.
     */
    suspend fun addUser(name: String, job: String): Boolean {
        return if (networkConnectivity.isOnline()) {
            // Online: Make the API call
            try {
                val request = AddUserRequest(name, job)
                apiService.addUser(request)
                true // Synced successfully
            } catch (e: Exception) {
                // API call failed, save locally as a fallback
                saveUserLocally(name, job)
                false // Not synced
            }
        } else {
            // Offline: Save to local database
            saveUserLocally(name, job)
            false // Not synced
        }
    }

    private suspend fun saveUserLocally(name: String, job: String) {
        val userEntity = UserEntity(name = name, job = job, isSynced = false)
        userDao.insertUser(userEntity)
    }
    suspend fun syncLocalUsers(): Boolean {
        val unsyncedUsers = userDao.getUnsyncedUsers()
        if (unsyncedUsers.isEmpty()) {
            return true // Nothing to sync
        }

        try {
            for (userEntity in unsyncedUsers) {
                val request = AddUserRequest(name = userEntity.name, job = userEntity.job)
                apiService.addUser(request) // Post to the server
                // If successful, mark it as synced in the local database
                userDao.markUserAsSynced(userEntity.id)
            }
            return true
        } catch (e: Exception) {
            // If any network call fails, return false
            return false
        }
    }
}