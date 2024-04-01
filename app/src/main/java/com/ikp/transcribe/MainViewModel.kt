package com.ikp.transcribe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ikp.transcribe.data.AppDatabase
import com.ikp.transcribe.data.table.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).TransactionDao()
    private val _transactions: MutableStateFlow<List<Transaction>> = MutableStateFlow(emptyList())
    private var email : String
    private var token : String
    val transactions: StateFlow<List<Transaction>> = _transactions

    init {
        val sharedPreferences = application.getSharedPreferences("auth", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", "email")!!
        token = sharedPreferences.getString("token", "email")!!
        fetchDataFromDao(email)
    }

    private fun fetchDataFromDao(email: String) {
        viewModelScope.launch {
            dao.getFlowTransaction(email).collect { data ->
                _transactions.value = data
            }
        }
    }

    fun getTotal(): Int {
        var total = 0
        for (transaction in transactions.value) {
            total += transaction.nominal ?: 0
        }
        return total
    }

    fun getIncome(): Int {
        var income = 0
        for (transaction in transactions.value) {
            if (transaction.kategori == "Pemasukan") {
                income += transaction.nominal ?: 0
            }
        }
        return income
    }

    fun getExpense(): Int {
        var expense = 0
        for (transaction in transactions.value) {
            if (transaction.kategori == "Pengeluaran") {
                expense += transaction.nominal ?: 0
            }
        }
        return expense
    }
}
