package com.rjspies.daedalus.presentation.history

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.domain.GetWeightsDescendingUseCase
import com.rjspies.daedalus.domain.Weight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeightHistoryViewModel(
    getWeightsDescending: GetWeightsDescendingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            getWeightsDescending().collectLatest { weights ->
                _uiState.update { it.copy(weights = weights) }
            }
        }
    }

    @Immutable
    data class UiState(
        val weights: List<Weight> = emptyList(),
    )
}
