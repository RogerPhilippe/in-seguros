package br.com.inseguros.ui.useterm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UseTermViewModel : ViewModel() {

    private val useTermContent = MutableLiveData<String>()

    fun loadUseTermContent() {

    }

    fun getUseTermContent() = useTermContent

}