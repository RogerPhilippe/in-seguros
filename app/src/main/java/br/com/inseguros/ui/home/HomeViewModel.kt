package br.com.inseguros.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.repository.QuoteVehicleRepository
import kotlinx.coroutines.runBlocking

class HomeViewModel(
    private val quoteVehicleRepository: QuoteVehicleRepository
) : ViewModel() {

    private val currentOPStatus = MutableLiveData<SaveStatusEnum>()

    fun updateQuoteStatus(quoteIDStr: String, quoteStatus: String) = runBlocking {

        try {
            val userAndQuoteID = quoteIDStr.split("|+|")
            val quoteID = userAndQuoteID[1].toLong()
            val quote = quoteVehicleRepository.findQuoteByID(quoteID)

            if (quote != null) {
                quote.apply {
                    this.quoteStatus = quoteStatus
                }
                quoteVehicleRepository.update(quote)
                currentOPStatus.postValue(SaveStatusEnum.SUCCESS)
            }
            else {
                Log.e("Err: uqs-01", "Erro ao buscar cotação id: $quoteID")
                currentOPStatus.postValue(SaveStatusEnum.ERROR)
            }

        } catch (ex: Exception) {
            Log.e("Err: uqs-02", ex.message?:"Erro desconhecido.")
            currentOPStatus.postValue(SaveStatusEnum.ERROR)
        }
    }

    fun getCurrentOPStatus() = currentOPStatus

}