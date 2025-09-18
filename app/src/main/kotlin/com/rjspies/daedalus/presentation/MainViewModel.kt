package com.rjspies.daedalus.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.domain.GetSnackbarQueueUseCase
import com.rjspies.daedalus.domain.SnackbarData
import com.rjspies.daedalus.domain.SnackbarIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.adevinta.spark.components.snackbars.SnackbarIntent as SparkSnackbarIntent

class MainViewModel(private val getSnackbarQueue: GetSnackbarQueueUseCase) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            getSnackbarQueue().collectLatest { snackbar ->
                _uiState.update { it.copy(snackbarData = snackbar) }
            }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.OnSnackbarDismissed -> _uiState.update { it.copy(snackbarData = null) }
        }
    }

    @Immutable
    data class UiState(val snackbarData: SnackbarData? = null)

    sealed interface Event {
        data object OnSnackbarDismissed : Event
    }
}

fun SnackbarIntent.toSparkSnackbarIntent(): SparkSnackbarIntent {
    return when (this) {
        SnackbarIntent.Success -> SparkSnackbarIntent.Success
        SnackbarIntent.Alert -> SparkSnackbarIntent.Alert
        SnackbarIntent.Error -> SparkSnackbarIntent.Error
        SnackbarIntent.Info -> SparkSnackbarIntent.Info
        SnackbarIntent.Neutral -> SparkSnackbarIntent.Neutral
        SnackbarIntent.Main -> SparkSnackbarIntent.Main
        SnackbarIntent.Basic -> SparkSnackbarIntent.Basic
        SnackbarIntent.Support -> SparkSnackbarIntent.Support
        SnackbarIntent.Accent -> SparkSnackbarIntent.Accent
        SnackbarIntent.SurfaceInverse -> SparkSnackbarIntent.SurfaceInverse
    }
}