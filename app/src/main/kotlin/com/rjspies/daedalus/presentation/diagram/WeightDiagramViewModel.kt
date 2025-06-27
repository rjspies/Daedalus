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
    private val _uiSate = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiSate

    init {
        viewModelScope.launch {
            getWeightsAscending().collectLatest { domainWeights ->
                _uiSate.update {
                    it.copy(
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
                _uiSate.update { it.copy(insertWeightDialogShow = true) }
            }

            Event.CloseInsertWeightDialog -> viewModelScope.launch {
                _uiSate.update {
                    it.copy(
                        insertWeightDialogShow = false,
                        insertWeightDialogIsLoading = false,
                        insertWeightDialogCurrentWeight = null,
                        insertWeightDialogError = null,
                    )
                }
            }

            is Event.SetCurrentWeight -> viewModelScope.launch {
                _uiSate.update {
                    it.copy(
                        insertWeightDialogCurrentWeight = filterInput(event.weight),
                        insertWeightDialogError = null,
                    )
                }
            }

            is Event.InsertCurrentWeight -> viewModelScope.launch {
                _uiSate.update {
                    it.copy(
                        insertWeightDialogIsLoading = true,
                        insertWeightDialogIsInputEnabled = false,
                        insertWeightDialogIsDismissable = false,
                    )
                }

                val parsedWeight = _uiSate.value.insertWeightDialogCurrentWeight.parseToFloat()
                var error: AppError? = null
                if (parsedWeight != null) {
                    insertWeight(value = parsedWeight, null, ZonedDateTime.now())
                } else {
                    error = InsertWeightError.ParseFloatError
                }

                _uiSate.update {
                    it.copy(
                        insertWeightDialogShow = error != null,
                        insertWeightDialogIsLoading = false,
                        insertWeightDialogCurrentWeight = null,
                        insertWeightDialogError = error,
                        insertWeightDialogIsInputEnabled = true,
                        insertWeightDialogIsDismissable = true,
                    )
                }
            }
        }
    }

    private fun filterInput(weight: String): String = weight.filter { it.isDigit() || it == '.' || it == ',' }
    private fun String?.parseToFloat(): Float? = this?.replace(",", ".")?.toFloatOrNull()

    data class UiState(
        val weights: List<WeightChartEntry> = emptyList(),
        val insertWeightDialogShow: Boolean = false,
        val insertWeightDialogIsLoading: Boolean = false,
        val insertWeightDialogCurrentWeight: String? = null,
        val insertWeightDialogError: AppError? = null,
        val insertWeightDialogIsInputEnabled: Boolean = true,
        val insertWeightDialogIsDismissable: Boolean = true,
    )

    sealed class Event {
        data object ShowInsertWeightDialog : Event()
        data object CloseInsertWeightDialog : Event()
        data class SetCurrentWeight(val weight: String) : Event()
        data object InsertCurrentWeight : Event()
    }
}
