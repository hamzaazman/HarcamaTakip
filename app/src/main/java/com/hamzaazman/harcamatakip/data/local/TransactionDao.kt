package com.hamzaazman.harcamatakip.data.local

import androidx.room.*
import com.hamzaazman.harcamatakip.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction`")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    // get all income or expense list by transaction type param
    @Query("SELECT * FROM `transaction` WHERE `type` == :transactionType")
    fun getAllSingleTransaction(transactionType: String): Flow<List<Transaction>>

    @Delete()
    fun nuke(transaction: Transaction)
}