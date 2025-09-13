package com.mshetty.stockpilot.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshetty.stockpilot.model.StockHoldingsResponse
import com.mshetty.stockpilot.repo.StockHoldingsRepository
import com.mshetty.stockpilot.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stockHoldingsRepository: StockHoldingsRepository
) : ViewModel() {

    private val _stockHoldingsData: MutableLiveData<UiState<StockHoldingsResponse>> = MutableLiveData()
    val stockHoldingsData: LiveData<UiState<StockHoldingsResponse>> get() = _stockHoldingsData

    fun getStockHoldings() {
        viewModelScope.launch(Dispatchers.IO) {
            _stockHoldingsData.postValue(UiState.Loading)
            val result = stockHoldingsRepository.getStockHoldings()
            _stockHoldingsData.postValue(result)
        }
    }
}