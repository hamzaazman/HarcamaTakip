package com.hamzaazman.harcamatakip.ui

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hamzaazman.harcamatakip.R
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        val transaction: com.hamzaazman.harcamatakip.data.model.Transaction =
            com.hamzaazman.harcamatakip.data.model.Transaction(
                id = 1,
                title = "Yol-Masraf",
                tags = "yol",
                amount = 60.00,
                date= LocalDate.of(2000, 1, 1).toString(),
                type = TransactionType.Expenses.toString()
            )
        vm.addTransaction(transaction)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.totalValue.collect {
                    binding.tvTotalValue.text = it.toString()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.calculateInCome().collect {
                    binding.tvIncome.text = it.toString()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.calculateExpenses().collect {
                    binding.tvOutcome.text = it.toString()
                }
            }
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