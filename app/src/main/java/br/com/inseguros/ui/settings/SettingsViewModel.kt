package br.com.inseguros.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.enums.QuoteTypeEnum
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.Message
import br.com.inseguros.data.model.MessageContent
import br.com.inseguros.data.model.QuotationProposal
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.data.repository.MessageContentRepository
import br.com.inseguros.data.repository.MessageRepository
import br.com.inseguros.data.repository.QuotationProposalRepository
import br.com.inseguros.data.repository.QuoteVehicleRepository
import br.com.inseguros.data.sessions.UserSession
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val db: FirebaseFirestore,
    private val quoteVehicleRepository: QuoteVehicleRepository,
    private val quotationProposalRepository: QuotationProposalRepository,
    private val messageRepository: MessageRepository,
    private val messageContentRepository: MessageContentRepository
) : ViewModel() {

    private val currentSyncStatusLiveData = MutableLiveData<SaveStatusEnum>()

    fun getQuotesFromFirebase() {

        db.collection("quotes").whereEqualTo("userID", UserSession.getUserID())
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty && it.documents.isNotEmpty()) {
                    val documents = it.documents
                    documents.forEach { quote ->

                        if ( (quote["quoteStatus"] as String) != QuoteTypeEnum.DELETED.value ) {

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

        db.collection("notifications").whereEqualTo("user_id", UserSession.getUserID())
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty && it.documents.isNotEmpty()) {
                    val documents = it.documents
                    documents.forEach { document ->

                        val messageContent = MessageContent(
                            quoteID = (document["content"] as HashMap<*, *>)["quote_id"] as String,
                            company = (document["content"] as HashMap<*, *>)["company"] as String,
                            title = (document["content"] as HashMap<*, *>)["title"] as String,
                            bodyText = (document["content"] as HashMap<*, *>)["bodyText"] as String,
                            proposalID = (document["content"] as HashMap<*, *>)["proposal_id"] as String
                        )

                        val messageContentID = saveMessageContent(messageContent)

                        if (messageContentID > 0) {

                            val message = Message(
                                id = document["message_id"] as String,
                                status = document["status"] as Long == 1L,
                                messageContentID = 0
                            )
                            saveMessage(message)
                        }
                    }
                    currentSyncStatusLiveData.postValue(SaveStatusEnum.SUCCESS)
                } else { currentSyncStatusLiveData.postValue(SaveStatusEnum.ERROR) }
            }
            .addOnFailureListener { currentSyncStatusLiveData.postValue(SaveStatusEnum.ERROR) }
    }

    fun getCurrentSyncStatus() = currentSyncStatusLiveData

    private fun saveVehicleQuoteRecovered(quoteVehicle: QuoteVehicle) = runBlocking {

        quoteVehicleRepository.insert(quoteVehicle)
    }

    private fun saveQuotationProposal(quotationProposal: QuotationProposal) = runBlocking {

        quotationProposalRepository.insert(quotationProposal)
    }

    private fun saveMessageContent(messageContent: MessageContent) = runBlocking {

        return@runBlocking messageContentRepository.insert(messageContent)

    }

    private fun saveMessage(message: Message) = runBlocking {

        messageRepository.insert(message)
    }

}