package br.com.inseguros.data.dao

import androidx.room.*
import br.com.inseguros.data.model.QuotationProposal

@Dao
interface QuotationProposalDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quotationProposal: QuotationProposal)

    @Update
    suspend fun update(quotationProposal: QuotationProposal)

    @Query("SELECT * FROM tb_quotation_proposal")
    suspend fun findAll(): List<QuotationProposal>

    @Delete
    suspend fun delete(quotationProposal: QuotationProposal)

}