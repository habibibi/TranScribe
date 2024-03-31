package com.ikp.transcribe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ikp.transcribe.data.AppDatabase
import com.ikp.transcribe.data.model.Item
import com.ikp.transcribe.data.repository.BillRepository
import com.ikp.transcribe.data.table.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).TransactionDao()
    private val billRepository = BillRepository()
    private val _transactions: MutableStateFlow<List<Transaction>> = MutableStateFlow(emptyList())
    private var billItems : List<Item> = emptyList()
    private var email : String
    private var token : String
    val transactions: StateFlow<List<Transaction>> = _transactions

    init {
        val sharedPreferences = application.getSharedPreferences("auth", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", "email")!!
        token = sharedPreferences.getString("token", "email")!!
        fetchDataFromDao(email)
    }

    suspend fun fetchBillItems(imageFile : File){
        billItems = billRepository.getBill(token, imageFile)
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
}
