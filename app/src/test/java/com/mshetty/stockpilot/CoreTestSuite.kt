package com.mshetty.stockpilot

import com.mshetty.stockpilot.model.PortfolioCalculator
import com.mshetty.stockpilot.model.PortfolioSummary
import com.mshetty.stockpilot.model.StockHolding
import com.mshetty.stockpilot.utils.UiState
import org.junit.Assert.*
import org.junit.Test

class CoreTestSuite {

    @Test
    fun `UiState Success should hold and return data correctly`() {
        val testData = "Test Data"
        val success = UiState.Success(testData)
        assertEquals(testData, success.data)
        assertTrue(success is UiState.Success)
    }

    @Test
    fun `UiState Error should hold and return message correctly`() {
        val errorMessage = "Test Error Message"
        val error = UiState.Error(errorMessage)
        assertEquals(errorMessage, error.message)
        assertTrue(error is UiState.Error)
    }

    @Test
    fun `UiState Loading should be singleton instance`() {
        val loading1 = UiState.Loading
        val loading2 = UiState.Loading
        assertSame(loading1, loading2)
        assertTrue(loading1 is UiState.Loading)
    }

    @Test
    fun `StockHolding should create with all required fields`() {
        val holding = StockHolding(
            symbol = "TEST",
            avgPrice = 100.0,
            close = 110.0,
            ltp = 105.0,
            quantity = 10
        )
        assertEquals("TEST", holding.symbol)
        assertEquals(100.0, holding.avgPrice, 0.01)
        assertEquals(110.0, holding.close, 0.01)
        assertEquals(105.0, holding.ltp, 0.01)
        assertEquals(10, holding.quantity)
    }

    @Test
    fun `PortfolioSummary should create with all required fields`() {
        val portfolioSummary = PortfolioSummary(
            currentValue = 1000.0,
            totalInvestment = 800.0,
            totalPnL = 200.0,
            todayPnL = 50.0,
            totalPnLPercentage = 25.0
        )
        assertEquals(1000.0, portfolioSummary.currentValue, 0.01)
        assertEquals(800.0, portfolioSummary.totalInvestment, 0.01)
        assertEquals(200.0, portfolioSummary.totalPnL, 0.01)
        assertEquals(50.0, portfolioSummary.todayPnL, 0.01)
        assertEquals(25.0, portfolioSummary.totalPnLPercentage, 0.01)
    }

    @Test
    fun `PortfolioCalculator should handle empty portfolio`() {
        val emptyHoldings = emptyList<StockHolding>()
        val result = PortfolioCalculator.calculatePortfolioSummary(emptyHoldings)
        assertEquals(0.0, result.currentValue, 0.01)
        assertEquals(0.0, result.totalInvestment, 0.01)
        assertEquals(0.0, result.todayPnL, 0.01)
        assertEquals(0.0, result.totalPnL, 0.01)
        assertEquals(0.0, result.totalPnLPercentage, 0.01)
    }

    @Test
    fun `PortfolioCalculator should calculate single stock correctly`() {
        val singleHolding = listOf(
            StockHolding(symbol = "SINGLE", avgPrice = 100.0, close = 110.0, ltp = 105.0, quantity = 10)
        )
        val result = PortfolioCalculator.calculatePortfolioSummary(singleHolding)
        assertEquals(1050.0, result.currentValue, 0.01)
        assertEquals(1000.0, result.totalInvestment, 0.01)
        assertEquals(50.0, result.todayPnL, 0.01)
        assertEquals(50.0, result.totalPnL, 0.01)
        assertEquals(5.0, result.totalPnLPercentage, 0.01)
    }

    @Test
    fun `PortfolioCalculator should handle profit scenario`() {
        val profitHolding = listOf(
            StockHolding(symbol = "PROFIT", avgPrice = 100.0, close = 120.0, ltp = 110.0, quantity = 10)
        )
        val result = PortfolioCalculator.calculatePortfolioSummary(profitHolding)
        assertEquals(1100.0, result.currentValue, 0.01)
        assertEquals(1000.0, result.totalInvestment, 0.01)
        assertEquals(100.0, result.todayPnL, 0.01)
        assertEquals(100.0, result.totalPnL, 0.01)
        assertEquals(10.0, result.totalPnLPercentage, 0.01)
    }

    @Test
    fun `PortfolioCalculator should handle loss scenario`() {
        val lossHolding = listOf(
            StockHolding(symbol = "LOSS", avgPrice = 100.0, close = 90.0, ltp = 95.0, quantity = 10)
        )
        val result = PortfolioCalculator.calculatePortfolioSummary(lossHolding)
        assertEquals(950.0, result.currentValue, 0.01)
        assertEquals(1000.0, result.totalInvestment, 0.01)
        assertEquals(-50.0, result.todayPnL, 0.01)
        assertEquals(-50.0, result.totalPnL, 0.01)
        assertEquals(-5.0, result.totalPnLPercentage, 0.01)
    }

    @Test
    fun `PortfolioCalculator should handle zero investment division by zero`() {
        val zeroHoldings = listOf(
            StockHolding(symbol = "ZERO", avgPrice = 0.0, close = 100.0, ltp = 100.0, quantity = 0)
        )
        val result = PortfolioCalculator.calculatePortfolioSummary(zeroHoldings)
        assertEquals(0.0, result.currentValue, 0.01)
        assertEquals(0.0, result.totalInvestment, 0.01)
        assertEquals(0.0, result.todayPnL, 0.01)
        assertEquals(0.0, result.totalPnL, 0.01)
        assertEquals(0.0, result.totalPnLPercentage, 0.01)
    }

    @Test
    fun `PortfolioSummary formatting should work for positive values`() {
        val portfolioSummary = PortfolioSummary(
            currentValue = 1234567.89,
            totalInvestment = 1000000.0,
            totalPnL = 234567.89,
            todayPnL = 5000.0,
            totalPnLPercentage = 23.4567
        )
        assertEquals("₹ 1,234,567.89", portfolioSummary.getFormattedCurrentValue())
        assertEquals("₹ 1,000,000.00", portfolioSummary.getFormattedTotalInvestment())
        assertEquals("₹ 234,567.89", portfolioSummary.getFormattedTotalPnL())
        assertEquals("₹ 5,000.00", portfolioSummary.getFormattedTodayPnL())
        assertEquals("23.46%", portfolioSummary.getFormattedTotalPnLPercentage())
    }

    @Test
    fun `PortfolioSummary formatting should work for negative values`() {
        val portfolioSummary = PortfolioSummary(
            currentValue = 500000.0,
            totalInvestment = 600000.0,
            totalPnL = -100000.0,
            todayPnL = -5000.0,
            totalPnLPercentage = -16.6667
        )
        assertEquals("₹ 500,000.00", portfolioSummary.getFormattedCurrentValue())
        assertEquals("₹ 600,000.00", portfolioSummary.getFormattedTotalInvestment())
        assertEquals("₹ -100,000.00", portfolioSummary.getFormattedTotalPnL())
        assertEquals("₹ -5,000.00", portfolioSummary.getFormattedTodayPnL())
        assertEquals("-16.67%", portfolioSummary.getFormattedTotalPnLPercentage())
    }

    @Test
    fun `PortfolioCalculator should handle decimal precision correctly`() {
        val decimalHoldings = listOf(
            StockHolding(symbol = "DECIMAL", avgPrice = 123.456789, close = 123.789012, ltp = 123.567890, quantity = 1)
        )
        val result = PortfolioCalculator.calculatePortfolioSummary(decimalHoldings)
        assertEquals(123.567890, result.currentValue, 0.000001)
        assertEquals(123.456789, result.totalInvestment, 0.000001)
        assertEquals(0.221122, result.todayPnL, 0.000001)
        assertEquals(0.111101, result.totalPnL, 0.000001)
    }

    @Test
    fun `PortfolioCalculator should handle large numbers`() {
        val largeHoldings = listOf(
            StockHolding(symbol = "LARGE", avgPrice = 1000000.0, close = 1001000.0, ltp = 1000500.0, quantity = 1000)
        )
        val result = PortfolioCalculator.calculatePortfolioSummary(largeHoldings)
        assertEquals(1000500000.0, result.currentValue, 0.01)
        assertEquals(1000000000.0, result.totalInvestment, 0.01)
        assertEquals(500000.0, result.todayPnL, 0.01)
        assertEquals(500000.0, result.totalPnL, 0.01)
        assertEquals(0.05, result.totalPnLPercentage, 0.01)
    }
}
