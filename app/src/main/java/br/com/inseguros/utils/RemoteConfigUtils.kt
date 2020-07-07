package br.com.inseguros.utils

import br.com.inseguros.BuildConfig
import br.com.inseguros.R
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

object RemoteConfigUtils {

    fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 }

        remoteConfig.setDefaultsAsync(R.xml.default_config_main_menu)
        remoteConfig.setConfigSettingsAsync(configSettings)
        val cacheExpiration = if (BuildConfig.DEBUG) 0L else 720L
        remoteConfig.fetch(cacheExpiration)
        return remoteConfig
    }

    fun fetchAndActive(): Task<Boolean> {
        return getFirebaseRemoteConfig().activate()
    }

}