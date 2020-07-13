package br.com.inseguros

import android.app.Application
import br.com.inseguros.data.model.AppSession
import br.com.inseguros.di.daoModule
import br.com.inseguros.di.dbModule
import br.com.inseguros.di.repositoryModule
import br.com.inseguros.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(viewModelModules, dbModule, daoModule, repositoryModule)
            )
        }

        AppSession.setMainMenuItems()

    }

}