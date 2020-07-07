package br.com.inseguros.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.model.AppSession
import br.com.inseguros.data.model.MainMenu

class HomeViewModel : ViewModel() {

    private var currentMainMenuItem = MutableLiveData<List<MainMenu>>()

    fun getMainMenuRemoteConfig() {

        AppSession.getMainMenuStatus().observeForever(object : Observer<Boolean>{
            override fun onChanged(t: Boolean?) {
                currentMainMenuItem.value = AppSession.getMainMenuItems()
                AppSession.getMainMenuStatus().removeObserver(this)
            }
        })

    }

    fun getCurrentMainMenuItem() = this.currentMainMenuItem

}