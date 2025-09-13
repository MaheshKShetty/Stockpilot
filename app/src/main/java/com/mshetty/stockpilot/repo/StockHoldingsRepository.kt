package com.mshetty.stockpilot.repo

import com.mshetty.stockpilot.model.StockHoldingsResponse
import com.mshetty.stockpilot.utils.UiState

interface StockHoldingsRepository {

    suspend fun getStockHoldings(): UiState<StockHoldingsResponse>

}