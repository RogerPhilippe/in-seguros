package br.com.inseguros.ui.quotes.genericscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.data.repository.QuoteVehicleRepository
import kotlinx.coroutines.runBlocking

class QuoteGenericScreenViewModel(private val quoteVehicleRepository: QuoteVehicleRepository) : ViewModel() {

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
            currentSaveStatus.postValue(SaveStatusEnum.SUCCESS)
        }
        else {
            currentSaveStatus.postValue(SaveStatusEnum.ERROR)
        }
    }

}