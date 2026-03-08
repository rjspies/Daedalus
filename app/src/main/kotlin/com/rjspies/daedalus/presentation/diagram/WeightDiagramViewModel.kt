package com.rjspies.daedalus.presentation.diagram

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.AppError
import com.rjspies.daedalus.InsertWeightError
import com.rjspies.daedalus.R
import com.rjspies.daedalus.domain.ExportWeightsUseCase
import com.rjspies.daedalus.domain.GetWeightsAscendingUseCase
import com.rjspies.daedalus.domain.ImportWeightsUseCase
import com.rjspies.daedalus.domain.InsertWeightUseCase
import com.rjspies.daedalus.domain.ShowSnackbarUseCase
import com.rjspies.daedalus.domain.SnackbarVisuals
import com.rjspies.daedalus.presentation.common.StringProvider
import com.rjspies.daedalus.presentation.common.WeightChartEntry
import java.io.IOException
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val CSV_MIME_TYPE = "text/comma-separated-values"

class WeightDiagramViewModel(
    getWeightsAscending: GetWeightsAscendingUseCase,
    private val insertWeight: InsertWeightUseCase,
    private val exportWeights: ExportWeightsUseCase,
    private val importWeights: ImportWeightsUseCase,
    private val showSnackbar: ShowSnackbarUseCase,
    private val stringProvider: StringProvider,
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

    @Suppress("LongMethod")
    fun onEvent(event: Event) {
        when (event) {
            Event.ShowInsertWeightDialog -> _uiState.update { it.copy(shouldShowInsertWeightDialog = true) }
            Event.CloseInsertWeightDialog -> _uiState.update { uiState ->
                uiState.copy(
                    shouldShowInsertWeightDialog = false,
                    isInsertWeightDialogLoading = false,
                    insertWeightDialogCurrentWeight = null,
                    insertWeightDialogError = null,
                )
            }
            is Event.SetCurrentWeight -> _uiState.update { uiState ->
                uiState.copy(
                    insertWeightDialogCurrentWeight = filterInput(event.weight),
                    insertWeightDialogError = null,
                )
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
                    handleInsertSuccess()
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
            Event.ExportClicked -> _uiState.update { uiState ->
                uiState.copy(exportPrompt = ExportUiData("weights.csv", CSV_MIME_TYPE), isExporting = true)
            }
            is Event.PathChosen -> viewModelScope.launch {
                _uiState.update { it.copy(exportPrompt = null) }
                handleExportResult(exportWeights(event.contentUri))
                _uiState.update { it.copy(isExporting = false) }
            }
            Event.ImportClicked -> _uiState.update { it.copy(importPrompt = ImportUiData(CSV_MIME_TYPE), isImporting = true) }
            is Event.ImportPathChosen -> viewModelScope.launch {
                _uiState.update { it.copy(importPrompt = null) }
                handleImportResult(importWeights(event.contentUri))
                _uiState.update { it.copy(isImporting = false) }
            }
        }
    }

    private suspend fun handleInsertSuccess() {
        showSnackbar(SnackbarVisuals(stringProvider.getString(R.string.snackbar_insert_weight_success)))
    }

    private suspend fun handleExportResult(result: Result<Unit>) {
        result
            .onSuccess { showSnackbar(SnackbarVisuals(stringProvider.getString(R.string.snackbar_export_success))) }
            .onFailure { exception ->
                val res = when (exception) {
                    is IllegalArgumentException -> R.string.snackbar_export_error_uri_null
                    is IOException -> R.string.snackbar_export_error_io
                    else -> R.string.snackbar_export_error_unknown
                }
                showSnackbar(SnackbarVisuals(stringProvider.getString(res), isError = true))
            }
    }

    private suspend fun handleImportResult(result: Result<Unit>) {
        result
            .onSuccess { showSnackbar(SnackbarVisuals(stringProvider.getString(R.string.snackbar_import_success))) }
            .onFailure { exception ->
                val res = when (exception) {
                    is IllegalArgumentException -> R.string.snackbar_import_error_uri_null
                    is IOException -> R.string.snackbar_import_error_io
                    is NumberFormatException,
                    is DateTimeParseException,
                    is IndexOutOfBoundsException,
                    is NoSuchElementException,
                    -> R.string.snackbar_import_error_parse
                    else -> R.string.snackbar_import_error_unknown
                }
                showSnackbar(SnackbarVisuals(stringProvider.getString(res), isError = true))
            }
    }

    private fun filterInput(weight: String): String = weight.filter { it.isDigit() || it == '.' || it == ',' }
    private fun String?.parseToFloat(): Float? = this?.run { replace(",", ".").toFloatOrNull() }

    data class ExportUiData(val fileName: String, val mimeType: String)
    data class ImportUiData(val mimeType: String)

    @Immutable
    data class UiState(
        val weights: List<WeightChartEntry> = emptyList(),
        val shouldShowInsertWeightDialog: Boolean = false,
        val isInsertWeightDialogLoading: Boolean = false,
        val insertWeightDialogCurrentWeight: String? = null,
        val insertWeightDialogError: AppError? = null,
        val isInsertWeightDialogInputEnabled: Boolean = true,
        val isInsertWeightDialogDismissable: Boolean = true,
        val exportPrompt: ExportUiData? = null,
        val isExporting: Boolean = false,
        val importPrompt: ImportUiData? = null,
        val isImporting: Boolean = false,
    )

    sealed interface Event {
        data class SetCurrentWeight(val weight: String) : Event
        data class PathChosen(val contentUri: String?) : Event
        data class ImportPathChosen(val contentUri: String?) : Event
        data object ShowInsertWeightDialog : Event
        data object CloseInsertWeightDialog : Event
        data object InsertCurrentWeight : Event
        data object ExportClicked : Event
        data object ImportClicked : Event
    }
}
