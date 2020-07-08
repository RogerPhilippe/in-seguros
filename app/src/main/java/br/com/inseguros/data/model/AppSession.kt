package br.com.inseguros.data.model

import androidx.lifecycle.MutableLiveData
import br.com.inseguros.utils.RemoteConfigUtils

object AppSession {

    private var mainMenuStatusLiveData = MutableLiveData<Boolean>()
    private val mainMenuItems = mutableListOf<MainMenu>()
    private var mainSubMenuStatusLiveData = MutableLiveData<Boolean>()
    private val mainSubMenuItems = mutableListOf<MainSubMenu>()

    fun setMainMenuItems() {

        RemoteConfigUtils.fetchAndActive()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    fillMainMenu()
                    fillMainSubMenu()
                }
                else { mainMenuStatusLiveData.postValue(false) }
            }

    }

    private fun fillMainMenu() {

        val firebaseRConfig = RemoteConfigUtils.getFirebaseRemoteConfig()

        val items = mutableListOf<MainMenu>()
        var mustFill = true
        var count = 1
        do {
            val currentID = "main_menu_id_item_$count"
            val currentTitle = firebaseRConfig.getString("main_menu_title_item_$count")
            val currentIcon = firebaseRConfig.getString("main_menu_icon_item_$count")
            if (currentTitle.isNotEmpty() && currentIcon.isNotEmpty()) {
                items.add(MainMenu(currentID, currentTitle, currentIcon))
                count ++
            } else {
                mustFill = false
            }
        } while (mustFill)

        mainMenuItems.clear()
        mainMenuItems.addAll(items)
        mainMenuStatusLiveData.postValue(true)
    }

    private fun fillMainSubMenu() {

        val firebaseRConfig = RemoteConfigUtils.getFirebaseRemoteConfig()

        val items = mutableListOf<MainSubMenu>()
        var mustFill = true
        var count = 1
        do {
            val currentID = "main_menu_id_item_$count"
            val currentTitle = firebaseRConfig.getString("main_sub_menu_title_item_$count")
            val currentSubTitle = firebaseRConfig.getString("main_sub_menu_sub_title_item_$count")
            val currentDescription = firebaseRConfig.getString("main_sub_menu_description_item_$count")
            val currentIcon = firebaseRConfig.getString("main_sub_menu_icon_item_$count")
            if (currentTitle.isNotEmpty() && currentIcon.isNotEmpty()) {
                items.add(MainSubMenu(currentID, currentTitle, currentSubTitle, currentDescription, currentIcon))
                count ++
            } else {
                mustFill = false
            }
        } while (mustFill)

        mainSubMenuItems.clear()
        mainSubMenuItems.addAll(items)
        mainSubMenuStatusLiveData.postValue(true)

    }

    fun getMainMenuStatus() = mainMenuStatusLiveData
    fun getMainMenuItems() = mainMenuItems

    fun getMainSubMenuStatus() = mainSubMenuStatusLiveData
    fun getMainSubMenuItems() = mainSubMenuItems

}