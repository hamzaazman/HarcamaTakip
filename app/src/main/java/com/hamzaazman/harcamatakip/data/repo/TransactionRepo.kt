package com.hamzaazman.harcamatakip.data.repo

import com.hamzaazman.harcamatakip.data.local.TransactionDao
import com.hamzaazman.harcamatakip.data.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TransactionRepo @Inject constructor(
    private val dao: TransactionDao
) {
    fun fetchAllTransaction(): Flow<List<Transaction>> {
        return dao.getAllTransactions()
            .flowOn(Dispatchers.IO)
            .conflate()
    }

    suspend fun addTransaction(transaction: Transaction) = dao.insertTransaction(transaction)

    fun getAllSingleTransaction(type: String): Flow<List<Transaction>> {
        return dao.getAllSingleTransaction(type)
    }
}