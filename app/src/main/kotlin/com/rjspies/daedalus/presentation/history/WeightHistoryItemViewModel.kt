package com.rjspies.daedalus.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.domain.DeleteWeightUseCase
import com.rjspies.daedalus.domain.Weight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeightHistoryItemViewModel(
    private val deleteWeight: DeleteWeightUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    fun onEvent(event: Event) {
        when (event) {
            Event.ShowDialog -> viewModelScope.launch {
                _uiState.update { it.copy(showDialog = true) }
            }

            Event.HideDialog -> viewModelScope.launch {
                _uiState.update { it.copy(showDialog = false) }
            }

            is Event.DeleteWeight -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isDialogLoading = true,
                            isDialogDismissable = false,
                        )
                    }
                }

                val deleteJob = viewModelScope.launch(Dispatchers.IO) {
                    deleteWeight(event.weight)
                }

                deleteJob.invokeOnCompletion {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(
                                showDialog = false,
                                isDialogLoading = false,
                                isDialogDismissable = true,
                            )
                        }
                    }
                }
            }
        }
    }

    data class UiState(
        val showDialog: Boolean = false,
        val isDialogLoading: Boolean = false,
        val isDialogDismissable: Boolean = true,
    )

    sealed interface Event {
        data object ShowDialog : Event
        data object HideDialog : Event
        data class DeleteWeight(val weight: Weight) : Event
    }
}
