package com.fomaxtro.lazypizza

import android.app.Application
import com.fomaxtro.core.data.di.dataModule
import com.fomaxtro.core.domain.di.domainModule
import com.fomaxtro.core.presentation.di.presentationModule
import com.fomaxtro.lazypizza.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class LazyPizzaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@LazyPizzaApp)
            androidLogger()

            modules(
                dataModule,
                domainModule,
                presentationModule,
                appModule
            )
        }
    }
}