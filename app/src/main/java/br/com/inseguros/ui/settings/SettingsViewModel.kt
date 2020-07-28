package br.com.inseguros.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.QuotationProposal
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.data.repository.QuotationProposalRepository
import br.com.inseguros.data.repository.QuoteVehicleRepository
import br.com.inseguros.data.sessions.UserSession
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val db: FirebaseFirestore,
    private val quoteVehicleRepository: QuoteVehicleRepository,
    private val quotationProposalRepository: QuotationProposalRepository
) : ViewModel() {

    private val currentSyncStatusLiveData = MutableLiveData<SaveStatusEnum>()

    fun getQuotesFromFirebase() {

        db.collection("quotes").whereEqualTo("userID", UserSession.getUserID())
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty && it.documents.isNotEmpty()) {
                    val documents = it.documents
                    documents.forEach { quote ->
                        saveVehicleQuoteRecovered(
                            QuoteVehicle(
                                userID = quote["userID"] as String,
                                fullName = quote["fullName"] as String,
                                cpf = quote["cpf"] as String,
                                quoteDate = quote["quoteDate"] as Long,
                                genre = quote["genre"] as String,
                                birthDate = quote["birthDate"] as Long,
                                civilState = quote["fullName"] as String,
                                vehicleType = quote["vehicleType"] as String,
                                vehicleBrand = quote["vehicleBrand"] as String,
                                vehicleModel = quote["vehicleModel"] as String,
                                vehicleYearManufacture = quote["vehicleYearManufacture"] as String,
                                vehicleModelYear = quote["vehicleModelYear"] as String,
                                vehicleLicenceNumber = quote["vehicleLicenceNumber"] as String,
                                vehicleLicenceTime = quote["vehicleLicenceTime"] as Long,
                                vehicleRegisterNum = quote["vehicleRegisterNum"] as String,
                                overnightCEP = quote["overnightCEP"] as String,
                                quoteStatus = quote["quoteStatus"] as String,
                                status = quote["status"] as Boolean
                            )
                        )
                    }
                    getQuotationsReceivedFromFirebase()
                } else { currentSyncStatusLiveData.postValue(SaveStatusEnum.ERROR) }

            }
            .addOnFailureListener { currentSyncStatusLiveData.postValue(SaveStatusEnum.ERROR) }
    }

    private fun getQuotationsReceivedFromFirebase() {

        db.collection("quotation_proposal").whereEqualTo("userID", UserSession.getUserID())
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty && it.documents.isNotEmpty()) {
                    val documents = it.documents
                    documents.forEach { document ->
                        saveQuotationProposal(
                            QuotationProposal(
                                id = document.id,
                                companyIcon = document["companyIcon"] as String,
                                companyLocation = document["companyLocation"] as String,
                                companyName = document["companyName"] as String,
                                companySite = document["companySite"] as String,
                                contact = document["contact"] as String,
                                contactEmail = document["contactEmail"] as String,
                                contactPhone = document["contactPhone"] as String,
                                insuranceCoverage = document["insuranceCoverage"] as String,
                                proposalDate = document["proposalDate"] as String,
                                proposalValue = document["proposalValue"] as String,
                                userID = document["userID"] as String,
                                vehicleModelNameAndFacYear = document["vehicleModelNameAndFacYear"] as String
                            )
                        )
                    }
                    getMessages()
                } else { currentSyncStatusLiveData.postValue(SaveStatusEnum.ERROR) }
            }
            .addOnFailureListener { currentSyncStatusLiveData.postValue(SaveStatusEnum.ERROR) }
    }

    private fun getMessages() {

        currentSyncStatusLiveData.postValue(SaveStatusEnum.SUCCESS)
    }

    fun getCurrentSyncStatus() = currentSyncStatusLiveData

    private fun saveVehicleQuoteRecovered(quoteVehicle: QuoteVehicle) = runBlocking {

        quoteVehicleRepository.insert(quoteVehicle)
    }

    private fun saveQuotationProposal(quotationProposal: QuotationProposal) = runBlocking {

        quotationProposalRepository.insert(quotationProposal)
    }

}