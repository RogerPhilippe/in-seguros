package br.com.inseguros.data.repository

import br.com.inseguros.data.dao.QuoteVehicleDAO
import br.com.inseguros.data.model.QuoteVehicle

class QuoteVehicleRepository(private val mDAO: QuoteVehicleDAO): ParentRepository {

    suspend fun insert(quoteVehicle: QuoteVehicle) = mDAO.insert(quoteVehicle)
    suspend fun update(quoteVehicle: QuoteVehicle) = mDAO.update(quoteVehicle)
    suspend fun findAll() = mDAO.findAll()
    suspend fun delete(quoteVehicle: QuoteVehicle) = mDAO.delete(quoteVehicle)

}