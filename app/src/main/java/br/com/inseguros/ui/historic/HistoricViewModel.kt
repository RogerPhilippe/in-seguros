package br.com.inseguros.ui.historic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.data.repository.ParentRepository
import br.com.inseguros.data.repository.QuoteVehicleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HistoricViewModel(private val repository: ParentRepository) : ViewModel() {

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
        withContext(Dispatchers.IO) {
            val quotesVehicle = (repository as QuoteVehicleRepository).findAll()
            if (quotesVehicle.isNotEmpty()) {
                currentQuotesVehicleLiveData.postValue(quotesVehicle)
            }
        }
    }

    fun cancelCurrentQuote(item: QuoteVehicle) = runBlocking {
        withContext(Dispatchers.IO) {
            (repository as QuoteVehicleRepository).update(item)
            currentOPQuoteStatus.postValue(item.quoteStatus)
        }
    }

    fun deleteQuotes(items: List<QuoteVehicle>) = runBlocking {
        withContext(Dispatchers.IO) {
            items.forEach {
                (repository as QuoteVehicleRepository).delete(it)
            }
        }
    }

}