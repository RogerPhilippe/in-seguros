package br.com.inseguros.ui.historic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.sessions.UserSession
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.data.repository.QuoteVehicleRepository
import kotlinx.coroutines.runBlocking

class HistoricViewModel(private val quoteVehicleRepository: QuoteVehicleRepository) : ViewModel() {

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
        quoteVehicleRepository.update(item)
        currentOPQuoteStatus.postValue(item.quoteStatus)
    }

    fun deleteQuotes(items: List<QuoteVehicle>) = runBlocking {
        items.forEach {
            quoteVehicleRepository.delete(it)
        }
    }

}