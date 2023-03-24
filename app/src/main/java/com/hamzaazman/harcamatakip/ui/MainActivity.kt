package com.hamzaazman.harcamatakip.ui

import android.content.Intent
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamzaazman.harcamatakip.R
import com.hamzaazman.harcamatakip.common.getFormattedNumber
import com.hamzaazman.harcamatakip.databinding.ActivityMainBinding
import com.hamzaazman.harcamatakip.ui.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        setupSwapDelete()
        vm.fetchAllTransactions()
        observeAllTransaction()
        observeIncome()
        observeExpense()
        observeTotal()


    }

    private fun setupSwapDelete() {
        val transactionTouchHelper = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                vm.removeTransaction(transactionAdapter.currentList[viewHolder.adapterPosition])
            }
        }
        ItemTouchHelper(transactionTouchHelper).attachToRecyclerView(binding.rvTransaction)
    }

    private fun observeTotal() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.totalValue.collect {
                    Log.d("lifecycle", "observeTotal: $it ")
                    binding.tvTotalValue.text = getFormattedNumber(it.toString())
                }
            }
        }
    }

    private fun observeExpense() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.calculateExpenses().collect {
                    binding.tvExpenseValue.text = it.toString()
                }
            }
        }
    }

    private fun observeIncome() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.calculateInCome().collect {
                    binding.tvIncomeValue.text = it.toString()
                }
            }
        }
    }

    private fun observeAllTransaction() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.allTransactions.collect {
                    transactionAdapter.submitList(it)
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

    override fun onResume() {
        super.onResume()
        vm.fetchAllTransactions()
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
            R.id.addTransaction -> {
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}