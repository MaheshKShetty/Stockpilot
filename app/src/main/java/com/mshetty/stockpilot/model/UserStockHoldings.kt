package com.mshetty.stockpilot.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StockHoldingsResponse(
    val data: StockHoldingsData
)

@JsonClass(generateAdapter = true)
data class StockHoldingsData(
    val userHolding: List<StockHolding>
)

@JsonClass(generateAdapter = true)
data class StockHolding(
    val avgPrice: Double,
    val close: Double,
    val ltp: Double,
    val quantity: Int,
    val symbol: String
)