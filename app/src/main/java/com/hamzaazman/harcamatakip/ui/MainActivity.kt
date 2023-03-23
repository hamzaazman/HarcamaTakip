package com.hamzaazman.harcamatakip.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hamzaazman.harcamatakip.R
import com.hamzaazman.harcamatakip.common.getFormattedNumber
import com.hamzaazman.harcamatakip.databinding.ActivityMainBinding
import com.hamzaazman.harcamatakip.ui.viewmodel.TransactionType
import com.hamzaazman.harcamatakip.ui.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm: TransactionViewModel by viewModels()
    private val transactionAdapter: TransactionAdapter by lazy { TransactionAdapter() }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupRv()

        val transaction: com.hamzaazman.harcamatakip.data.model.Transaction =
            com.hamzaazman.harcamatakip.data.model.Transaction(
                id = 0,
                title = "Bahşiş",
                tags = "bahşiş",
                amount = 500.00,
                date = LocalDate.of(2005, 2, 1).toString(),
                type = TransactionType.Income.toString()
            )
        vm.addTransaction(transaction)
        vm.fetchAllTransactions()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.allTransactions.collect {
                    Log.d("list", "onCreate: $it")
                    transactionAdapter.submitList(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.calculateInCome().collect {
                    binding.tvIncomeValue.text = it.toString()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.calculateExpenses().collect {
                    binding.tvExpenseValue.text = it.toString()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.totalValue.collect {
                    binding.tvTotalValue.text = getFormattedNumber(it.toString())
                }
            }
        }
    }

    private fun setupRv() = with(binding) {
        rvTransaction.apply {
            adapter = transactionAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity, LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}