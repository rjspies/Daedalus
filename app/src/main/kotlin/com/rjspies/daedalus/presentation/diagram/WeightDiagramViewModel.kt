package com.rjspies.daedalus.presentation.diagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.AppError
import com.rjspies.daedalus.InsertWeightError
import com.rjspies.daedalus.domain.GetWeightsAscendingUseCase
import com.rjspies.daedalus.domain.InsertWeightUseCase
import com.rjspies.daedalus.presentation.common.WeightChartEntry
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeightDiagramViewModel(
    getWeightsAscending: GetWeightsAscendingUseCase,
    private val insertWeight: InsertWeightUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            getWeightsAscending().collectLatest { domainWeights ->
                _uiState.update { uiState ->
                    uiState.copy(
                        weights = domainWeights.mapIndexed { index, weight ->
                            WeightChartEntry(
                                x = index.toFloat(),
                                y = weight.value,
                                dateTime = weight.dateTime,
                            )
                        },
                    )
                }
            }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.ShowInsertWeightDialog -> viewModelScope.launch {
                _uiState.update { it.copy(shouldShowInsertWeightDialog = true) }
            }

            Event.CloseInsertWeightDialog -> viewModelScope.launch {
                _uiState.update { uiState ->
                    uiState.copy(
                        shouldShowInsertWeightDialog = false,
                        isInsertWeightDialogLoading = false,
                        insertWeightDialogCurrentWeight = null,
                        insertWeightDialogError = null,
                    )
                }
            }

            is Event.SetCurrentWeight -> viewModelScope.launch {
                _uiState.update { uiState ->
                    uiState.copy(
                        insertWeightDialogCurrentWeight = filterInput(event.weight),
                        insertWeightDialogError = null,
                    )
                }
            }

            is Event.InsertCurrentWeight -> viewModelScope.launch {
                _uiState.update { uiState ->
                    uiState.copy(
                        isInsertWeightDialogLoading = true,
                        isInsertWeightDialogInputEnabled = false,
                        isInsertWeightDialogDismissable = false,
                    )
                }

                val parsedWeight = _uiState.value.insertWeightDialogCurrentWeight.parseToFloat()
                var error: AppError? = null
                if (parsedWeight != null) {
                    insertWeight(
                        value = parsedWeight,
                        note = null,
                        dateTime = ZonedDateTime.now(),
                    )
                } else {
                    error = InsertWeightError.ParseFloatError
                }

                _uiState.update { uiState ->
                    uiState.copy(
                        shouldShowInsertWeightDialog = error != null,
                        isInsertWeightDialogLoading = false,
                        insertWeightDialogCurrentWeight = null,
                        insertWeightDialogError = error,
                        isInsertWeightDialogInputEnabled = true,
                        isInsertWeightDialogDismissable = true,
                    )
                }
            }
        }
    }

    private fun filterInput(weight: String): String = weight.filter { it.isDigit() || it == '.' || it == ',' }
    private fun String?.parseToFloat(): Float? = this?.run { replace(",", ".").toFloatOrNull() }

    data class UiState(
        val weights: List<WeightChartEntry> = emptyList(),
        val shouldShowInsertWeightDialog: Boolean = false,
        val isInsertWeightDialogLoading: Boolean = false,
        val insertWeightDialogCurrentWeight: String? = null,
        val insertWeightDialogError: AppError? = null,
        val isInsertWeightDialogInputEnabled: Boolean = true,
        val isInsertWeightDialogDismissable: Boolean = true,
    )

    sealed interface Event {
        data object ShowInsertWeightDialog : Event
        data object CloseInsertWeightDialog : Event
        data class SetCurrentWeight(val weight: String) : Event
        data object InsertCurrentWeight : Event
    }
}
