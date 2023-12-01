package com.example.stockmarketapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {

    //Si insertamos un company con un id que ya exista , reemplazamos la data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListings()

    //En sqlite almenos esto significa || -> concatenamos algunos Strings "%" + string + "%"
    //Convertimos el name a lowercase , luego la consulta que le hacemos a lowercase tambien
    //Depues colocamos los simbolos "%" como un placeholder , para poder colocar cualquier cosa dentro de ambos "%"
    // Luego usamos el operador LIKE para hacer una comparacion entre el name en lowercase con la consulta en lowercase tambien .
    //Por ultimo usamos el operador OR para que tambien podamos comparar la consulta con un simbolo y no solo con el nombre del Stock
    @Query(""" 
            SELECT *
            FROM companylistingentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == symbol
            """)
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>
}