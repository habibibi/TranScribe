package com.ikp.transcribe.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.ikp.transcribe.data.table.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction` WHERE email LIKE :email")
    suspend fun getTransaction(email:String): List<Transaction>

    @Query("SELECT * FROM `transaction` WHERE email LIKE :email")
    fun getFlowTransaction(email:String): Flow<List<Transaction>>

    @Query("INSERT INTO `transaction` (email,judul,kategori,nominal,lokasi,tanggal) " +
            "VALUES (:email,:judul,:kategori,:nominal,:lokasi,:tanggal)")
    suspend fun insertData(email:String,judul:String,kategori:String,nominal:Double,lokasi:String,tanggal:String)

    @Query("DELETE FROM `transaction` WHERE id = :id")
    suspend fun deleteData (id:Int)

    @Query("UPDATE `transaction` SET judul = :judul, nominal = :nominal, lokasi = :lokasi " +
            "WHERE id = :id")
    suspend fun updateData(id:Int,judul:String,nominal:Double,lokasi:String)

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    suspend fun getTransacID(id:Int): Transaction
}