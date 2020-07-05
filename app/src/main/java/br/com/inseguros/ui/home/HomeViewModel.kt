package br.com.inseguros.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.R
import br.com.inseguros.data.model.MainMenu
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class HomeViewModel : ViewModel() {

    private var currentMainMenuItem = MutableLiveData<List<MainMenu>>()

    fun getMainMenuRemoteConfig() {

        val firebaseRConfig = FirebaseRemoteConfig.getInstance()

        firebaseRConfig.setDefaultsAsync(R.xml.default_config)

        firebaseRConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val items = mutableListOf<MainMenu>()
                    var mustFill = true
                    var cont = 1
                    do {
                        val currentTitle = firebaseRConfig.getString("main_menu_title_item_$cont")
                        val currentIcon = firebaseRConfig.getString("main_menu_icon_item_$cont")
                        if (currentTitle.isNotEmpty() && currentIcon.isNotEmpty()) {
                            items.add(MainMenu(currentTitle, currentIcon))
                            cont ++
                        } else {
                            mustFill = false
                        }
                    } while (mustFill)

                    currentMainMenuItem.postValue(items)

                }
            }

    }

    fun getCurrentMainMenuItem() = this.currentMainMenuItem

}