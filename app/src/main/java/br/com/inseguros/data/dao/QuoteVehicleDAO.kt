package br.com.inseguros.data.dao

import androidx.room.*
import br.com.inseguros.data.model.QuoteVehicle

@Dao
interface QuoteVehicleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: QuoteVehicle): Long

    @Update
    suspend fun update(item: QuoteVehicle)

    @Query("SELECT * FROM tb_quote_vehicle WHERE status IN (1)")
    suspend fun findAll(): List<QuoteVehicle>

    @Query("SELECT * FROM tb_quote_vehicle WHERE userID IN (:userID) AND status IN (1)")
    suspend fun findAllByUserID(userID: String): List<QuoteVehicle>

    @Query("SELECT * FROM tb_quote_vehicle WHERE id IN (:quoteID) AND status IN (1)")
    suspend fun findQuoteByID(quoteID: Long): QuoteVehicle?

    @Delete
    suspend fun delete(item: QuoteVehicle)

}