package com.example.movieapp.ui.user

import android.content.Context // New import
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints // New import
import androidx.work.NetworkType // New import
import androidx.work.OneTimeWorkRequestBuilder // New import
import androidx.work.WorkManager // New import
import com.example.movieapp.repository.UserRepository
import com.example.movieapp.worker.SyncUserWorker // New import
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext // New import
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context // <-- ADD THIS
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun saveUser(name: String, job: String) {
        viewModelScope.launch {
            if (name.isBlank() || job.isBlank()) {
                _eventFlow.emit(UiEvent.ShowToast("Name and job cannot be empty"))
                return@launch
            }

            val wasSynced = userRepository.addUser(name, job)

            if (wasSynced) {
                _eventFlow.emit(UiEvent.ShowToast("User saved successfully and synced to server"))
                _eventFlow.emit(UiEvent.NavigateBack)
            } else {
                _eventFlow.emit(UiEvent.ShowToast("User saved locally, will sync when online"))
                scheduleSync() // <-- CALL THE NEW METHOD
                _eventFlow.emit(UiEvent.NavigateBack)
            }
        }
    }

    // --- NEW METHOD TO ADD ---
    private fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when there's an internet connection
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncUserWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object NavigateBack : UiEvent()
    }
}