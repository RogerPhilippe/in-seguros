package br.com.inseguros.ui.historic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.enums.QuoteTypeEnum
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.data.repository.QuoteVehicleRepository
import br.com.inseguros.data.sessions.UserSession
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class HistoricViewModel(
    private val quoteVehicleRepository: QuoteVehicleRepository,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val currentQuotesVehicleLiveData = MutableLiveData<List<QuoteVehicle>>()
    private val currentOPQuoteStatus = MutableLiveData<String>()

    // *********************************************************************************************
    // Public methods
    // *********************************************************************************************
    // Get methods
    // *********************************************************************************************

    fun getCurrentQuotesVehicleLiveData() = this.currentQuotesVehicleLiveData
    fun getCurrentOPQuoteStatus() = this.currentOPQuoteStatus

    // *********************************************************************************************
    // General purpose methods
    // *********************************************************************************************

    fun loadCurrentQuotesVehicle() = runBlocking {

        val quotesVehicle = quoteVehicleRepository.findAllByUserID(UserSession.getUserID())
        if (quotesVehicle.isNotEmpty()) {
            currentQuotesVehicleLiveData.postValue(quotesVehicle)
        }
    }

    fun cancelCurrentQuote(item: QuoteVehicle) = runBlocking {

        cancelQuoteStatusInFirebase(item, QuoteTypeEnum.CANCELED.value)
        quoteVehicleRepository.update(item)
        currentOPQuoteStatus.postValue(item.quoteStatus)
    }

    fun deleteQuotes(items: List<QuoteVehicle>) = runBlocking {

        items.forEach {
            cancelQuoteStatusInFirebase(it, QuoteTypeEnum.DELETED.value)
            quoteVehicleRepository.delete(it)
        }
    }

    private fun cancelQuoteStatusInFirebase(quoteVehicle: QuoteVehicle, quoteStatus: String) {

        val documentID = "${UserSession.getUserID()}|+|${quoteVehicle.id}"

        db.collection("quotes").document(documentID)
            .update("quoteStatus", quoteStatus)
    }

}