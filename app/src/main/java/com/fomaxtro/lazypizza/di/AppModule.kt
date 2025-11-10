package com.fomaxtro.lazypizza.di

import com.fomaxtro.lazypizza.app.AppViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::AppViewModel)
}