package br.com.inseguros.data.dao

import androidx.room.*
import br.com.inseguros.data.model.QuoteVehicle

@Dao
interface QuoteVehicleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: QuoteVehicle): Long

    @Update
    fun update(item: QuoteVehicle)

    @Query("SELECT * FROM tb_quote_vehicle WHERE status IN(1)")
    fun findAll(): List<QuoteVehicle>

    @Delete
    fun delete(item: QuoteVehicle)

}