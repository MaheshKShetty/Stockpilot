package com.mshetty.stockpilot.repo

import com.mshetty.stockpilot.model.StockHoldingsResponse
import com.mshetty.stockpilot.network.NoInternetException
import com.mshetty.stockpilot.remote.StocksApi
import com.mshetty.stockpilot.utils.ErrorType
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
            when (exception) {
                is NoInternetException -> UiState.Error("", ErrorType.NO_INTERNET)
                is java.net.SocketTimeoutException -> UiState.Error("", ErrorType.TIMEOUT)
                is java.net.UnknownHostException -> UiState.Error("", ErrorType.NO_INTERNET)
                is retrofit2.HttpException -> UiState.Error("", ErrorType.SERVER_ERROR)
                else -> UiState.Error("", ErrorType.GENERIC)
            }
        }
    }
}