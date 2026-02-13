package com.example.fundnav.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fundnav.data.FundHolding
import com.example.fundnav.data.FundNavPoint
import com.example.fundnav.data.FundSearchItem
import com.example.fundnav.data.WatchFund
import com.example.fundnav.repository.FundRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FundUiState(
    val keyword: String = "",
    val searchResult: List<FundSearchItem> = emptyList(),
    val selectedFund: FundSearchItem? = null,
    val holdings: List<FundHolding> = emptyList(),
    val navHistory: List<FundNavPoint> = emptyList(),
    val watchList: List<WatchFund> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class FundViewModel(private val repository: FundRepository) : ViewModel() {
    private val _state = MutableStateFlow(FundUiState(watchList = repository.watchList()))
    val state: StateFlow<FundUiState> = _state.asStateFlow()

    fun updateKeyword(value: String) {
        _state.value = _state.value.copy(keyword = value)
    }

    fun search() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            val result = repository.searchFund(_state.value.keyword)
            _state.value = _state.value.copy(searchResult = result, loading = false)
        }
    }

    fun loadFundDetail(item: FundSearchItem) {
        viewModelScope.launch {
            _state.value = _state.value.copy(selectedFund = item, loading = true)
            val holdings = repository.holdings(item.code)
            val history = repository.navHistory(item.code)
            _state.value = _state.value.copy(
                holdings = holdings,
                navHistory = history,
                loading = false
            )
        }
    }

    class Factory(private val repository: FundRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FundViewModel(repository) as T
        }
    }
}
