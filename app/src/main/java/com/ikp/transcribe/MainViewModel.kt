package com.ikp.transcribe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ikp.transcribe.data.AppDatabase
import com.ikp.transcribe.data.model.Item
import com.ikp.transcribe.data.repository.BillRepository
import com.ikp.transcribe.data.table.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.net.ConnectException
import java.net.UnknownHostException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).TransactionDao()
    private val billRepository = BillRepository()
    private val _transactions: MutableStateFlow<List<Transaction>> = MutableStateFlow(emptyList())
    private var billItems : List<Item> = emptyList()
    private var email : String
    private var token : String
    val transactions: StateFlow<List<Transaction>> = _transactions

    fun getEmail() : String{
        return email
    }
    init {
        val sharedPreferences = application.getSharedPreferences("auth", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", "email")!!
        token = sharedPreferences.getString("token", "email")!!
        fetchDataFromDao(email)
    }

    suspend fun fetchBillItems(imageFile : File) : Error? {
        return try {
            billItems = billRepository.getBill(token, imageFile)
            null
        } catch (e : UnknownHostException) {
            Error("Network error.")
        } catch (e : ConnectException){
            Error("Network error")
        } catch (e : Exception){
            if (e.cause?.message == "httpError"){
                Error("Http error.")
            } else {
                Log.e("fetchBill", e::class.toString())
                Error("Unknown error.")
            }
        }
    }

    fun addTransaction(
            email: String,
            judul: String,
            kategori: String,
            nominal: Double,
            lokasi: String,
            tanggal: String){
        viewModelScope.launch(Dispatchers.IO){
            dao.insertData(email,judul,kategori,nominal,lokasi,tanggal)
        }
    }

    fun getBillItems() : List<Item>{
        return billItems
    }

    private fun fetchDataFromDao(email: String) {
        viewModelScope.launch {
            dao.getFlowTransaction(email).collect { data ->
                _transactions.value = data
            }
        }
    }

    fun getTotal(): Double {
        var total = 0.0
        for (transaction in transactions.value) {
            total += transaction.nominal ?: 0.0
        }
        return total
    }

    fun getIncome(): Double {
        var income = 0.0
        for (transaction in transactions.value) {
            if (transaction.kategori == "Pemasukan") {
                income += transaction.nominal ?: 0.0
            }
        }
        return income
    }

    fun getExpense(): Double {
        var expense = 0.0
        for (transaction in transactions.value) {
            if (transaction.kategori == "Pengeluaran") {
                expense += transaction.nominal ?: 0.0
            }
        }
        return expense
    }
}
