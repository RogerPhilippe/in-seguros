package br.com.inseguros.ui.quotes.genericscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.data.model.SaveStatusEnum
import br.com.inseguros.data.repository.ParentRepository
import br.com.inseguros.data.repository.QuoteVehicleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class QuoteGenericScreenViewModel(private val repository: ParentRepository) : ViewModel() {

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
        withContext(Dispatchers.IO) {
            val id = (repository as QuoteVehicleRepository).insert(currentQuoteVehicleLiveData.value!!)
            if (id > -1) {
                currentSaveStatus.postValue(SaveStatusEnum.SUCCESS)
            }
            else {
                currentSaveStatus.postValue(SaveStatusEnum.ERROR)
            }
        }
    }

}