package br.com.inseguros.data.repository

import br.com.inseguros.data.dao.QuotationProposalDAO
import br.com.inseguros.data.model.QuotationProposal

class QuotationProposalRepository(private val mDAO: QuotationProposalDAO) {

    suspend fun insert(quotationProposal: QuotationProposal) = mDAO.insert(quotationProposal)
    suspend fun update(quotationProposal: QuotationProposal) = mDAO.update(quotationProposal)
    suspend fun findAll() = mDAO.findAll()
    suspend fun delete(quotationProposal: QuotationProposal) = mDAO.delete(quotationProposal)

}