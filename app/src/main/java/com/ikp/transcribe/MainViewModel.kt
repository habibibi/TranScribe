package com.ikp.transcribe

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikp.transcribe.data.dao.TransactionDao
import com.ikp.transcribe.data.table.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val dao: TransactionDao,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _transactions: MutableStateFlow<List<Transaction>> = MutableStateFlow(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    init {
        val email = sharedPreferences.getString("email", null)
        if (email != null) {
            fetchDataFromDao(email)
        }
    }

    private fun fetchDataFromDao(email: String) {
        viewModelScope.launch {
            dao.getFlowTransaction(email).collect { data ->
                _transactions.value = data
            }
        }
    }
}
