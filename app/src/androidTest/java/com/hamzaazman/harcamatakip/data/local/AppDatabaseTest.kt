package com.hamzaazman.harcamatakip.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.hamzaazman.harcamatakip.data.model.Transaction
import com.hamzaazman.harcamatakip.ui.MainActivity
import com.hamzaazman.harcamatakip.ui.viewmodel.TransactionType
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate


@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var transactionDao: TransactionDao
    private lateinit var db: AppDatabase

    @get:Rule
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
    fun writeUserAndReadInList() = runBlocking {
        val transaction: Transaction = Transaction(
            2, "yol", 12.00,
            TransactionType.Income.toString(),
            LocalDate.of(2000, 1, 1).toString(),
            "yol"
        )
        transactionDao.insertTransaction(transaction)

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}