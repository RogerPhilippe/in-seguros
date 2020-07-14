package br.com.inseguros

import android.app.Application
import androidx.preference.PreferenceManager
import br.com.inseguros.data.AppSession
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

        /**
         * auto_login
         * notification_active
         * app_desc
         * app_version
         */
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        AppSession.setMainMenuItems()

    }

}