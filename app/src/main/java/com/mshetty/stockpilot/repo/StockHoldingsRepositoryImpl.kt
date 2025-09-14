package com.mshetty.stockpilot.repo

import com.mshetty.stockpilot.model.StockHoldingsResponse
import com.mshetty.stockpilot.network.NoInternetException
import com.mshetty.stockpilot.remote.StocksApi
import com.mshetty.stockpilot.utils.UiState
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "StockHoldingsRepositoryImpl"

@Singleton
class StockHoldingsRepositoryImpl @Inject constructor(
    private val api: StocksApi
) : StockHoldingsRepository {

    override suspend fun getStockHoldings(): UiState<StockHoldingsResponse> {
        return try {
            val data = api.getUserHoldings()
            UiState.Success(data)
        } catch (exception: Exception) {
            UiState.Error(exception.message ?: "An unknown error occurred")
        }
    }
}