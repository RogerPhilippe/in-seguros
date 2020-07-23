package br.com.inseguros.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.QuotationProposal
import br.com.inseguros.data.repository.QuotationProposalRepository
import br.com.inseguros.data.repository.QuoteVehicleRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class HomeViewModel(
    private val quoteVehicleRepository: QuoteVehicleRepository,
    private val quotationProposalRepository: QuotationProposalRepository,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val currentOPStatus = MutableLiveData<SaveStatusEnum>()

    fun updateQuoteStatus(quoteIDStr: String, proposalID: String, quoteStatus: String) = runBlocking {

        Log.i("ProposalID", proposalID)

        try {
            val userAndQuoteID = quoteIDStr.split("|+|")
            val quoteID = userAndQuoteID[1].toLong()
            val quote = quoteVehicleRepository.findQuoteByID(quoteID)

            if (quote != null) {
                quote.apply {
                    this.quoteStatus = quoteStatus
                }
                quoteVehicleRepository.update(quote)
                findInFireStoreQuotationProposal(proposalID)
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

    private fun findInFireStoreQuotationProposal(proposalID: String) {

        db.collection("quotation_proposal").document(proposalID).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val quotationProposal = QuotationProposal(
                        proposalID,
                        it["companyName"] as String,
                        it["companySite"] as String,
                        it["companyLocation"] as String,
                        it["contact"] as String,
                        it["contactEmail"] as String,
                        it["contactPhone"] as String,
                        it["proposalDate"] as String,
                        it["proposalValue"] as String,
                        it["vehicleModelNameAndFacYear"] as String,
                        true
                    )
                    saveQuotationProposalInLocalDB(quotationProposal)
                }

            }
            .addOnFailureListener {
                Log.e("Err: ffSPDB-01", "")
                currentOPStatus.postValue(SaveStatusEnum.ERROR)
            }
    }

    private fun saveQuotationProposalInLocalDB(quotationProposal: QuotationProposal) = runBlocking {

        quotationProposalRepository.insert(quotationProposal)
        currentOPStatus.postValue(SaveStatusEnum.SUCCESS)

    }

    fun getCurrentOPStatus() = currentOPStatus

}