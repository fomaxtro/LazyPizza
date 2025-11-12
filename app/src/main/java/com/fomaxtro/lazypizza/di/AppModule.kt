package com.fomaxtro.lazypizza.di

import com.fomaxtro.lazypizza.app.AppViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import timber.log.Timber

val appModule = module {
    viewModelOf(::AppViewModel)
    single<CoroutineScope> {
        CoroutineScope(
            Dispatchers.Default +
                    SupervisorJob() +
                    CoroutineExceptionHandler { _, throwable ->
                        Timber.tag("ApplicationScope").e(throwable)
                    }
        )
    }
}