package io.athan

import android.app.Application
import io.athan.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AthanApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AthanApplication)
            modules(appModule)
        }
    }
}