package com.ikp.transcribe.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.ikp.transcribe.data.table.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction` WHERE email LIKE :email")
    fun getTransaction(email:String): List<Transaction>
    @Query("SELECT * FROM `transaction` WHERE email LIKE :email")
    fun getFlowTransaction(email:String): Flow<List<Transaction>>

    @Query("INSERT INTO `transaction` (email,judul,kategori,nominal,lokasi,tanggal) " +
            "VALUES (:email,:judul,:kategori,:nominal,:lokasi,:tanggal)")
    fun insertData(email:String,judul:String,kategori:String,nominal:Int,lokasi:String,tanggal:String)

    @Query("DELETE FROM `transaction` WHERE id = :id")
    fun deleteData (id:Int)

    @Query("UPDATE `transaction` SET judul = :judul, kategori = :kategori, nominal = :nominal, lokasi = :lokasi,tanggal = :tanggal " +
            "WHERE id = :id")
    fun updateData(id:Int,judul:String,kategori:String,nominal:Int,lokasi:String,tanggal: String)

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    fun getTransacID(id:Int): Transaction


}