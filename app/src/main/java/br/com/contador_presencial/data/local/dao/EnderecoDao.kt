package br.com.contador_presencial.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.contador_presencial.data.local.entity.EnderecoEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * DAO completo para operações CRUD da tabela `endereco`.
 */
@Dao
interface EnderecoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(endereco: EnderecoEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(enderecos: List<EnderecoEntity>): List<Long>

    @Query("SELECT * FROM endereco WHERE id = :id LIMIT 1")
    suspend fun getById(id: UUID): EnderecoEntity?

    @Query("SELECT * FROM endereco WHERE id = :id LIMIT 1")
    fun observeById(id: UUID): Flow<EnderecoEntity?>

    @Query("SELECT * FROM endereco")
    suspend fun getAll(): List<EnderecoEntity>

    @Query("SELECT * FROM endereco")
    fun observeAll(): Flow<List<EnderecoEntity>>

    @Query("SELECT * FROM endereco WHERE autor_id = :autorId")
    suspend fun getByAutorId(autorId: UUID): List<EnderecoEntity>

    @Update
    suspend fun update(endereco: EnderecoEntity): Int

    @Update
    suspend fun updateAll(enderecos: List<EnderecoEntity>): Int

    @Delete
    suspend fun delete(endereco: EnderecoEntity): Int

    @Delete
    suspend fun deleteAll(enderecos: List<EnderecoEntity>): Int

    @Query("DELETE FROM endereco WHERE id = :id")
    suspend fun deleteById(id: UUID): Int
}
