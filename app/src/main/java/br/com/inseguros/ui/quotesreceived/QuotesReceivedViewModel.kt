package br.com.inseguros.ui.quotesreceived

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.model.QuotationProposal
import br.com.inseguros.data.repository.QuotationProposalRepository
import kotlinx.coroutines.runBlocking

class QuotesReceivedViewModel(
    private val quotationProposalRepository: QuotationProposalRepository
) : ViewModel() {

    private val currentQuotationListLiveData = MutableLiveData<List<QuotationProposal>>()

    fun getQuotations() = runBlocking {

        currentQuotationListLiveData.postValue(quotationProposalRepository.findAll())
    }

    fun getCurrentQuotationListLiveData() = this.currentQuotationListLiveData

}