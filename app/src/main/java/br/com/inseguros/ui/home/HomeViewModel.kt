package br.com.inseguros.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.QuotationProposal
import br.com.inseguros.data.repository.QuotationProposalRepository
import br.com.inseguros.data.repository.QuoteVehicleRepository
import br.com.inseguros.data.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class HomeViewModel(
    private val quoteVehicleRepository: QuoteVehicleRepository,
    private val quotationProposalRepository: QuotationProposalRepository,
    private val db: FirebaseFirestore,
    private val mContext: Context
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
                        userID = it["userID"] as String,
                        companyIcon = it["companyIcon"] as String,
                        companyName = it["companyName"] as String,
                        companySite = it["companySite"] as String,
                        companyLocation = it["companyLocation"] as String,
                        insuranceCoverage = it["insuranceCoverage"] as String,
                        contact = it["contact"] as String,
                        contactEmail = it["contactEmail"] as String,
                        contactPhone = it["contactPhone"] as String,
                        proposalDate = it["proposalDate"] as String,
                        proposalValue = it["proposalValue"] as String,
                        vehicleModelNameAndFacYear = it["vehicleModelNameAndFacYear"] as String
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
        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        prefs.edit().putString(Constants.NEW_QUOTATION_PROPOSAL_RECEIVED, "").apply()
        prefs.edit().putString(Constants.NEW_PROPOSAL_ID, "").apply()
        currentOPStatus.postValue(SaveStatusEnum.SUCCESS)

    }

    fun getCurrentOPStatus() = currentOPStatus

}