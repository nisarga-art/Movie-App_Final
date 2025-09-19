package com.example.movieapp.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.data.User
import com.example.movieapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * HiltViewModel tells Hilt that this is a ViewModel and that it can provide
 * dependencies to it.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * This public property will expose the stream of user data to the UI.
     * We get the Flow from the repository.
     */
    val users: Flow<PagingData<User>> = getUsersFlow()

    private fun getUsersFlow(): Flow<PagingData<User>> {
        /**
         * The `cachedIn(viewModelScope)` operator is crucial. It caches the data loaded by Paging
         * in the viewModelScope. This means that if the user rotates the screen, the
         * existing data is immediately available and doesn't need to be fetched again.
         */
        return userRepository.getUsers().cachedIn(viewModelScope)
    }
}