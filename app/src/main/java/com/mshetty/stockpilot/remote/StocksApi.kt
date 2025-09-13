package com.mshetty.stockpilot.remote

import com.mshetty.stockpilot.model.StockHoldingsResponse
import retrofit2.http.GET

interface StocksApi {

    @GET(".")
    suspend fun getUserHoldings(): StockHoldingsResponse

}