package br.com.inseguros.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import java.io.Serializable

@Entity(tableName = "tb_quote_vehicle")
data class QuoteVehicle(
    @get:Exclude @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var fullName: String = "",
    var cpf: String = "",
    var quoteDate: Long = 0,
    var genre: Char = ' ',
    var birthDate: Long = 0,
    var civilState: String = "",
    var vehicleType: String = "",
    var vehicleBrand: String = "",
    var vehicleModel: String = "",
    var vehicleYearManufacture: String = "",
    var vehicleModelYear: String = "",
    var vehicleLicenceNumber: String = "",
    var vehicleLicenceTime: Long = 0,
    var vehicleRegisterNum: String = "",
    var overnightCEP: String = "",
    var quoteStatus: String = "",
    var status: Boolean = false
): Serializable