package com.mshetty.stockpilot.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshetty.stockpilot.R
import com.mshetty.stockpilot.adapter.StockHoldingsAdapter
import com.mshetty.stockpilot.databinding.ActivityMainBinding
import com.mshetty.stockpilot.model.PortfolioCalculator
import com.mshetty.stockpilot.model.PortfolioSummary
import com.mshetty.stockpilot.utils.UiState
import com.mshetty.stockpilot.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val stockHoldingsAdapter: StockHoldingsAdapter by lazy { StockHoldingsAdapter() }
    private var isSummaryExpanded = false
    private var portfolioSummary: PortfolioSummary? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.lifecycleOwner = this
        setupAdapter()
        setupObservers()
        setupClickListeners()
        viewModel.getStockHoldings()
    }
    private fun setupAdapter() {
        binding.rvHoldings.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stockHoldingsAdapter
        }
    }

    private fun setupObservers() {
        viewModel.stockHoldingsData.observe(this) { response ->
            when (response) {
                is UiState.Loading -> {
                    showLoadingState()
                }

                is UiState.Error -> {
                    showErrorState(response.message)
                }

                is UiState.Success -> {
                    showSuccessState(response.data.data.userHolding)
                }
            }
        }
    }

    private fun showLoadingState() {
        binding.apply {
            pbProgress.visibility = View.VISIBLE
            llErrorContainer.visibility = View.GONE
            rvHoldings.visibility = View.GONE
            llPortfolioSummary.visibility = View.GONE
        }
    }

    private fun showErrorState(errorMessage: String) {
        binding.apply {
            pbProgress.visibility = View.GONE
            llErrorContainer.visibility = View.VISIBLE
            rvHoldings.visibility = View.GONE
            llPortfolioSummary.visibility = View.GONE
            tvError.text = errorMessage
            btnRetry.text = getString(R.string.retry)
        }
    }

    private fun showSuccessState(holdings: List<com.mshetty.stockpilot.model.StockHolding>) {
        binding.apply {
            pbProgress.visibility = View.GONE
            llErrorContainer.visibility = View.GONE
            rvHoldings.visibility = View.VISIBLE
            llPortfolioSummary.visibility = View.VISIBLE
        }
        stockHoldingsAdapter.submitList(holdings)
        portfolioSummary = PortfolioCalculator.calculatePortfolioSummary(holdings)
        updateCollapsedView()
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            viewModel.getStockHoldings()
        }
        binding.llCollapsedView.setOnClickListener {
            togglePortfolioSummary()
        }
    }

    private fun togglePortfolioSummary() {
        isSummaryExpanded = !isSummaryExpanded
        val expandedContent = binding.llExpandedContent
        val expandArrow = binding.ivExpandArrow
        
        if (isSummaryExpanded) {
            expandedContent.visibility = View.VISIBLE
            expandArrow.rotation = 180f
            updateExpandedView()
        } else {
            expandedContent.visibility = View.GONE
            expandArrow.rotation = 0f
        }
    }

    private fun updateCollapsedView() {
        portfolioSummary?.let { summary ->
            val totalPnLText = summary.getFormattedTotalPnL()
            binding.tvCollapsedTotalPnl.text = totalPnLText
            
            val totalPnLColor = if (summary.totalPnL >= 0) R.color.profit_green else R.color.loss_red
            binding.tvCollapsedTotalPnl.setTextColor(ContextCompat.getColor(this,totalPnLColor))
        }
    }

    private fun updateExpandedView() {
        portfolioSummary?.let { summary ->
            binding.tvCurrentValue.text = summary.getFormattedCurrentValue()
            binding.tvTotalInvestment.text = summary.getFormattedTotalInvestment()
            
            val totalPnLText = "${summary.getFormattedTotalPnL()} (${summary.getFormattedTotalPnLPercentage()})"
            binding.tvTotalPnl.text = totalPnLText
            
            binding.tvTodaysPnl.text = summary.getFormattedTodayPnL()
            
            val totalPnLColor = if (summary.totalPnL >= 0) R.color.profit_green else R.color.loss_red
            val todayPnLColor = if (summary.todayPnL >= 0) R.color.profit_green else R.color.loss_red
            binding.tvTotalPnl.setTextColor(ContextCompat.getColor(this,totalPnLColor))
            binding.tvTodaysPnl.setTextColor(ContextCompat.getColor(this,todayPnLColor))
        }
    }

}