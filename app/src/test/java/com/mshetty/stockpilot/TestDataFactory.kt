package com.mshetty.stockpilot

import com.mshetty.stockpilot.model.StockHoldingsData
import com.mshetty.stockpilot.model.StockHoldingsResponse
import com.mshetty.stockpilot.model.StockHolding

object TestDataFactory {

    fun createSampleStockHoldings(): List<StockHolding> {
        return listOf(
            StockHolding(
                symbol = "RELIANCE",
                avgPrice = 2500.0,
                close = 2600.0,
                ltp = 2550.0,
                quantity = 10
            ),
            StockHolding(
                symbol = "TCS",
                avgPrice = 3500.0,
                close = 3600.0,
                ltp = 3400.0,
                quantity = 5
            ),
            StockHolding(
                symbol = "INFY",
                avgPrice = 1500.0,
                close = 1600.0,
                ltp = 1550.0,
                quantity = 20
            )
        )
    }

    fun createSampleStockHoldingsResponse(): StockHoldingsResponse {
        return StockHoldingsResponse(
            data = StockHoldingsData(
                userHolding = createSampleStockHoldings()
            )
        )
    }

    fun createEmptyStockHoldingsResponse(): StockHoldingsResponse {
        return StockHoldingsResponse(
            data = StockHoldingsData(
                userHolding = emptyList()
            )
        )
    }

    fun createSingleStockHolding(): StockHolding {
        return StockHolding(
            symbol = "SINGLE",
            avgPrice = 100.0,
            close = 110.0,
            ltp = 105.0,
            quantity = 100
        )
    }

    fun createLossStockHolding(): StockHolding {
        return StockHolding(
            symbol = "LOSS",
            avgPrice = 1000.0,
            close = 900.0,
            ltp = 800.0,
            quantity = 10
        )
    }

    fun createLargeStockHoldingsList(size: Int): List<StockHolding> {
        return (1..size).map { index ->
            StockHolding(
                symbol = "STOCK$index",
                avgPrice = 100.0 + index,
                close = 110.0 + index,
                ltp = 105.0 + index,
                quantity = index
            )
        }
    }

    fun createZeroQuantityStockHolding(): StockHolding {
        return StockHolding(
            symbol = "ZERO",
            avgPrice = 100.0,
            close = 110.0,
            ltp = 105.0,
            quantity = 0
        )
    }

    fun createDecimalPrecisionStockHolding(): StockHolding {
        return StockHolding(
            symbol = "DECIMAL",
            avgPrice = 123.456789,
            close = 123.789012,
            ltp = 123.567890,
            quantity = 1
        )
    }

    fun createLargeNumberStockHolding(): StockHolding {
        return StockHolding(
            symbol = "LARGE",
            avgPrice = 999999.99,
            close = 1000000.00,
            ltp = 999999.50,
            quantity = 1000000
        )
    }
}
