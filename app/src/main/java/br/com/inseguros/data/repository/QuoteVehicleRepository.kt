package br.com.inseguros.data.repository

import br.com.inseguros.data.dao.QuoteVehicleDAO
import br.com.inseguros.data.model.QuoteVehicle

class QuoteVehicleRepository(private val mDAO: QuoteVehicleDAO): ParentRepository {

    fun insert(quoteVehicle: QuoteVehicle) = mDAO.insert(quoteVehicle)
    fun update(quoteVehicle: QuoteVehicle) = mDAO.update(quoteVehicle)
    fun findAll() = mDAO.findAll()
    fun delete(quoteVehicle: QuoteVehicle) = mDAO.delete(quoteVehicle)

}