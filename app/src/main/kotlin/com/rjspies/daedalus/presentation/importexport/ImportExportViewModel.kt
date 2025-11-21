package com.rjspies.daedalus.presentation.importexport

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ImportExportViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    fun onEvent(event: Event) {
        when (event) {
            Event.Export -> _uiState.update {
                it.copy(exportPrompt = ExportUiData("weights.csv", "text/csv"))
            }
            Event.Import -> {}
            is Event.PathChosen -> {
                _uiState.update { it.copy(exportPrompt = null) }
            }
        }
    }

    @Immutable
    data class ExportUiData(val fileName: String, val mimeType: String)

    @Immutable
    data class UiState(
        val exportPrompt: ExportUiData? = null,
    )

    sealed interface Event {
        data object Export : Event
        data object Import : Event
        data class PathChosen(val dataUri: Uri?) : Event
    }
}