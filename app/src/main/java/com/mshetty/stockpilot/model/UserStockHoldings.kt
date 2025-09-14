package com.mshetty.stockpilot.model

import com.squareup.moshi.JsonClass
import java.text.DecimalFormat

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

data class PortfolioSummary(
    val currentValue: Double,
    val totalInvestment: Double,
    val totalPnL: Double,
    val todayPnL: Double,
    val totalPnLPercentage: Double
) {
    fun getFormattedCurrentValue(): String = formatCurrency(currentValue)
    fun getFormattedTotalInvestment(): String = formatCurrency(totalInvestment)
    fun getFormattedTotalPnL(): String = formatCurrency(totalPnL)
    fun getFormattedTodayPnL(): String = formatCurrency(todayPnL)
    fun getFormattedTotalPnLPercentage(): String = "${String.format("%.2f", totalPnLPercentage)}%"
    
    private fun formatCurrency(amount: Double): String {
        val df = DecimalFormat("#,##0.00")
        return "₹ ${df.format(amount)}"
    }
}

object PortfolioCalculator {
    
    fun calculatePortfolioSummary(holdings: List<StockHolding>): PortfolioSummary {
        var currentValue = 0.0
        var totalInvestment = 0.0
        var todayPnL = 0.0
        holdings.forEach { holding ->
            currentValue += holding.ltp * holding.quantity
            totalInvestment += holding.avgPrice * holding.quantity
            todayPnL += (holding.close - holding.ltp) * holding.quantity
        }
        val totalPnL = currentValue - totalInvestment
        
        val totalPnLPercentage = if (totalInvestment > 0) {
            (totalPnL / totalInvestment) * 100
        } else 0.0
        
        return PortfolioSummary(
            currentValue = currentValue,
            totalInvestment = totalInvestment,
            totalPnL = totalPnL,
            todayPnL = todayPnL,
            totalPnLPercentage = totalPnLPercentage
        )
    }
}