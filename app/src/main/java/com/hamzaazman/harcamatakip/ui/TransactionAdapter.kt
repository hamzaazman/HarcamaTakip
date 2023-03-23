package com.hamzaazman.harcamatakip.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hamzaazman.harcamatakip.common.getFormattedNumber
import com.hamzaazman.harcamatakip.data.model.Transaction
import com.hamzaazman.harcamatakip.databinding.TransactionRowItemBinding

class TransactionAdapter :
    ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffUtil) {
    object TransactionDiffUtil : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }

    }

    class TransactionViewHolder(private val binding: TransactionRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) = with(binding) {
            tvTitle.text = transaction.title
            tvTag.text = transaction.tags
            tvAmount.text = getFormattedNumber(transaction.amount.toString())
            tvDate.text = transaction.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            TransactionRowItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}