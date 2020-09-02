package br.com.inseguros.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "tb_quotation_proposal")
data class QuotationProposal(
    @PrimaryKey val id: String = "",
    val userID: String = "",
    val companyIcon: String = "",
    val companyName: String = "",
    val companySite: String = "",
    val companyLocation: String = "",
    val insuranceCoverage: String = "",
    val contact: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",
    val proposalDate: String = "",
    val proposalValue: String = "",
    val vehicleModelNameAndFacYear: String = ""
)