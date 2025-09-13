
package com.mshetty.stockpilot.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mshetty.stockpilot.R
import com.mshetty.stockpilot.databinding.ItemStockHoldingBinding
import com.mshetty.stockpilot.model.StockHolding
import java.text.DecimalFormat

class StockHoldingsAdapter :
    ListAdapter<StockHolding, StockHoldingsAdapter.StockHoldingViewHolder>(StockHoldingDiffCallback()) {

    inner class StockHoldingViewHolder(private val binding: ItemStockHoldingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockHolding) {
            binding.stockHolding = item
            
            binding.tvNetQty.text = binding.root.context.getString(R.string.net_qty, item.quantity)
            
            val ltpFormatted = binding.root.context.getString(R.string.ltp, formatNumber(item.ltp))
            binding.tvLtp.text = ltpFormatted

            val pnl = (item.ltp - item.avgPrice) * item.quantity
            val pnlFormatted = binding.root.context.getString(R.string.pnl, formatNumber(pnl))
            binding.tvPnL.text = pnlFormatted
            
            if (pnl >= 0) {
                binding.tvPnL.setTextColor(ContextCompat.getColor(binding.root.context,R.color.profit_green))
            } else {
                binding.tvPnL.setTextColor(ContextCompat.getColor(binding.root.context,R.color.loss_red))
            }
        }
        
        private fun formatNumber(number: Double): String {
            val df = DecimalFormat("#,##0.00")
            return df.format(number)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockHoldingViewHolder {
        val binding = ItemStockHoldingBinding.inflate(LayoutInflater.from(parent.context))
        return StockHoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockHoldingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class StockHoldingDiffCallback : DiffUtil.ItemCallback<StockHolding>() {
    override fun areItemsTheSame(oldItem: StockHolding, newItem: StockHolding): Boolean {
        return oldItem.symbol == newItem.symbol
    }

    override fun areContentsTheSame(oldItem: StockHolding, newItem: StockHolding): Boolean {
        return oldItem == newItem
    }
}
