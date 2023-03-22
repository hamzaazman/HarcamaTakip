package com.hamzaazman.harcamatakip.ui

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hamzaazman.harcamatakip.data.local.AppDatabase
import com.hamzaazman.harcamatakip.data.local.TransactionDao
import com.hamzaazman.harcamatakip.data.model.Transaction
import com.hamzaazman.harcamatakip.ui.viewmodel.TransactionType
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var transactionDao: TransactionDao
    private lateinit var db: AppDatabase

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        transactionDao = db.transactionDao()
    }


    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runBlocking{
        val transaction: Transaction = Transaction(
            1, "yol", 12.00,
            TransactionType.Income.toString(),
            LocalDate.of(2000, 1, 1).toString(),
            "yol"
        )
        transactionDao.insertTransaction(transaction)
        val byType = transactionDao.getAllSingleTransaction(TransactionType.Income.toString())
        MatcherAssert.assertThat(byType, IsEqual.equalTo(transaction.type))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}