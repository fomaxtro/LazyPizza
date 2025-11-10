package com.fomaxtro.lazypizza.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppViewModel(
    authRepository: AuthRepository
) : ViewModel() {
    val state = authRepository.isAuthenticated()
        .map { isAuthenticated ->
            AppState(
                isAuthenticated = isAuthenticated,
                isAuthChecked = true
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            AppState()
        )
}