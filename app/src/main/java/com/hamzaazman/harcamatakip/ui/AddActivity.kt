package com.hamzaazman.harcamatakip.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hamzaazman.harcamatakip.R
import com.hamzaazman.harcamatakip.common.transactionTags
import com.hamzaazman.harcamatakip.common.transactionType
import com.hamzaazman.harcamatakip.common.transformIntoDatePicker
import com.hamzaazman.harcamatakip.data.model.Transaction
import com.hamzaazman.harcamatakip.databinding.ActivityAddBinding
import com.hamzaazman.harcamatakip.ui.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    private val vm: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() = with(binding) {
        val typeAdapter = ArrayAdapter(this@AddActivity, R.layout.type_row_item, transactionType)
        val tagAdapter = ArrayAdapter(this@AddActivity, R.layout.type_row_item, transactionTags)

        etTag.setAdapter(tagAdapter)
        etType.setAdapter(typeAdapter)

        etDate.transformIntoDatePicker(this@AddActivity, "dd/MM/yyyy", Date())

        saveTransactionButton.setOnClickListener {

            val title = etTitle.text?.toString()?.trim()
            val amount = etAmount.text.toString().toDouble()
            val type = etType.text?.toString()?.trim()
            val tag = etTag.text?.toString()?.trim()
            val date = etDate.text?.toString()?.trim()

            val transaction = Transaction(0, title, amount, type, tag, date)

            vm.addTransaction(transaction)

            finish()
        }

    }

    private fun checkNullTransaction() {

    }
}