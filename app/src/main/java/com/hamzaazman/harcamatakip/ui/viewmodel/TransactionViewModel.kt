package com.hamzaazman.harcamatakip.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.harcamatakip.data.model.Transaction
import com.hamzaazman.harcamatakip.data.repo.TransactionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TransactionType {
    Income,
    Expenses
}

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepo
) : ViewModel() {

    private val _transactions = MutableSharedFlow<Transaction>()
    val transaction = _transactions.asSharedFlow()

    private val _allTransactions = MutableSharedFlow<List<Transaction>>()
    val allTransactions = _allTransactions.asSharedFlow()

     fun fetchAllTransactions() = viewModelScope.launch {
        val response = repository.fetchAllTransaction()
        response.collect {
            _allTransactions.emit(it)
        }
    }

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.addTransaction(transaction)
    }

    private val totalInCome: Flow<Double>
        get() {
            return repository.getAllSingleTransaction(TransactionType.Income.toString())
                .map {
                    var total = 0.0
                    it.forEach {
                        if (it.type == TransactionType.Income.toString()) {
                            total += it.amount!!
                        }
                    }
                    return@map total
                }
        }

    private val totalExpenses: Flow<Double>
        get() {
            return repository.getAllSingleTransaction(TransactionType.Expenses.toString())
                .map {
                    var totalExpenses = 0.0
                    it.forEach {
                        if (it.type == TransactionType.Expenses.toString()) {
                            totalExpenses += it.amount!!
                        }
                    }
                    return@map totalExpenses
                }
        }

    fun calculateInCome(): StateFlow<Double> = flow<Double> {
        totalInCome.collect {
            emit(it)
        }.runCatching {
            emit(0.0)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    fun calculateExpenses(): StateFlow<Double> =
        flow<Double> {
            totalExpenses.collect {
                emit(it)
            }.runCatching {
                emit(0.0)
            }

        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            0.0
        )

    val totalValue: StateFlow<Double>
        get() {
            return flow<Double> {
                combine(totalExpenses, totalInCome) { e, i ->
                    return@combine (i - e)
                }.collect{
                    emit(it)
                }.runCatching {
                    emit(0.0)
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
        }

}