package br.com.inseguros.ui.quotes.genericscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.sessions.UserSession
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.data.repository.QuoteVehicleRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class QuoteGenericScreenViewModel(
    private val quoteVehicleRepository: QuoteVehicleRepository,
    private val db: FirebaseFirestore,
    private val realtimeDatabase: FirebaseDatabase
) : ViewModel() {

    private var currentQuoteVehicleLiveData = MutableLiveData<QuoteVehicle>()
    private var currentSaveStatus = MutableLiveData<SaveStatusEnum>()

    init {
        if (currentQuoteVehicleLiveData.value == null) {
            currentQuoteVehicleLiveData.value = QuoteVehicle()
        }
    }

    // *********************************************************************************************
    // Public methods
    // *********************************************************************************************
    // Gets
    // *********************************************************************************************

    fun getCurrentQuoteVehicleLiveData() = this.currentQuoteVehicleLiveData
    fun getCurrentSaveStatus() = this.currentSaveStatus

    // *********************************************************************************************
    // General purpose methods
    // *********************************************************************************************

    fun insertQuoteVehicle() = runBlocking {
        val id = quoteVehicleRepository.insert(currentQuoteVehicleLiveData.value!!)
        if (id > -1) {
            saveQuoteInFireStore(currentQuoteVehicleLiveData.value!!, id)
        }
        else {
            currentSaveStatus.postValue(SaveStatusEnum.ERROR)
        }
    }

    private fun saveQuoteInFireStore(quoteVehicle: QuoteVehicle, quoteID: Long) {

        db.collection("quotes")
            .document("${UserSession.getUserID()}|+|$quoteID")
            .set(quoteVehicle)
            .addOnSuccessListener {
                realtimeDatabase.reference
                    .child("new_quote_request")
                    .child(UserSession.getUserID())
                    .child("quote_id")
                    .setValue("${UserSession.getUserID()}|+|$quoteID")
                currentSaveStatus.postValue(SaveStatusEnum.SUCCESS)
            }
            .addOnFailureListener { currentSaveStatus.postValue(SaveStatusEnum.ERROR) }
    }

}