package com.mshetty.stockpilot.di

import com.mshetty.stockpilot.repo.StockHoldingsRepository
import com.mshetty.stockpilot.repo.StockHoldingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStockHoldingsRepository(
        stockHoldingsRepositoryImpl: StockHoldingsRepositoryImpl
    ): StockHoldingsRepository
}
