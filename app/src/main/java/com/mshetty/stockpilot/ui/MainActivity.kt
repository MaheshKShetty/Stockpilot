package com.mshetty.stockpilot.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshetty.stockpilot.R
import com.mshetty.stockpilot.adapter.StockHoldingsAdapter
import com.mshetty.stockpilot.databinding.ActivityMainBinding
import com.mshetty.stockpilot.utils.UiState
import com.mshetty.stockpilot.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val stockHoldingsAdapter: StockHoldingsAdapter by lazy { StockHoldingsAdapter() }

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
                    showErrorState(response.errorType)
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
        }
    }

    private fun showErrorState(errorType: com.mshetty.stockpilot.utils.ErrorType) {
        binding.apply {
            pbProgress.visibility = View.GONE
            llErrorContainer.visibility = View.VISIBLE
            rvHoldings.visibility = View.GONE
            tvError.text = getString(errorType.messageResId)
            btnRetry.text = getString(R.string.retry)
        }
    }

    private fun showSuccessState(holdings: List<com.mshetty.stockpilot.model.StockHolding>) {
        binding.apply {
            pbProgress.visibility = View.GONE
            llErrorContainer.visibility = View.GONE
            rvHoldings.visibility = View.VISIBLE
        }
        stockHoldingsAdapter.submitList(holdings)
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            viewModel.getStockHoldings()
        }
    }

}