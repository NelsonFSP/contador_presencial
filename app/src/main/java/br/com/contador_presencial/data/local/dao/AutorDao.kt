package br.com.contador_presencial.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.contador_presencial.data.local.entity.AutorEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * DAO completo para operações CRUD da tabela `autor`.
 */
@Dao
interface AutorDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(autor: AutorEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(autores: List<AutorEntity>): List<Long>

    @Query("SELECT * FROM autor WHERE id = :id LIMIT 1")
    suspend fun getById(id: UUID): AutorEntity?

    @Query("SELECT * FROM autor WHERE id = :id LIMIT 1")
    fun observeById(id: UUID): Flow<AutorEntity?>

    @Query("SELECT * FROM autor")
    suspend fun getAll(): List<AutorEntity>

    @Query("SELECT * FROM autor")
    fun observeAll(): Flow<List<AutorEntity>>

    @Update
    suspend fun update(autor: AutorEntity): Int

    @Update
    suspend fun updateAll(autores: List<AutorEntity>): Int

    @Delete
    suspend fun delete(autor: AutorEntity): Int

    @Delete
    suspend fun deleteAll(autores: List<AutorEntity>): Int

    @Query("DELETE FROM autor WHERE id = :id")
    suspend fun deleteById(id: UUID): Int
}
