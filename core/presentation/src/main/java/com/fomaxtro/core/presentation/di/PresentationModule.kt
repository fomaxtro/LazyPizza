package com.fomaxtro.core.presentation.di

import com.fomaxtro.core.presentation.screen.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::HomeViewModel)
}