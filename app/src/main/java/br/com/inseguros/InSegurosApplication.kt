package br.com.inseguros

import android.app.Application
import br.com.inseguros.di.daoModule
import br.com.inseguros.di.dbModule
import br.com.inseguros.di.repositoryModule
import br.com.inseguros.di.viewModelModules
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class InSegurosApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Fabric.with(
            this,
            Crashlytics.Builder().core(
                CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
            ).build()
        )

        startKoin {
            androidLogger()
            androidContext(this@InSegurosApplication)
            modules(
                listOf(viewModelModules, dbModule, daoModule, repositoryModule)
            )
        }

    }

}