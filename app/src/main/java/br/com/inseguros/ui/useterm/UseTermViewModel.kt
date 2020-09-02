package br.com.inseguros.ui.useterm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class UseTermViewModel(
    private val db: FirebaseFirestore
) : ViewModel() {

    private val useTermContent = MutableLiveData<String>()

    fun loadUseTermContent() {

    }

    fun getUseTermContent() = useTermContent

}