package com.fomaxtro.core.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    authRepository: AuthRepository
) : ViewModel() {
    val state = authRepository.isAuthenticated()
        .map { isAuthenticated ->
            HistoryState(
                isAuthenticated = isAuthenticated
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HistoryState()
        )
}