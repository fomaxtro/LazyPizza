package com.fomaxtro.core.presentation.screen.home

sealed interface HomeAction {
    data class OnSearchChange(val search: String) : HomeAction
}