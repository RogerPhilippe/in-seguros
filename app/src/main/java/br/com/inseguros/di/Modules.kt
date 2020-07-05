package br.com.inseguros.di

import br.com.inseguros.ui.home.HomeViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { HomeViewModel() }
}