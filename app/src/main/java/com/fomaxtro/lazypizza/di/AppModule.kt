package com.fomaxtro.lazypizza.di

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import timber.log.Timber

val appModule = module {
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