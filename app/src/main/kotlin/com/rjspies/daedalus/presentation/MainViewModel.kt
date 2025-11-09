package com.rjspies.daedalus.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.domain.GetSnackbarUseCase
import com.rjspies.daedalus.domain.SnackbarVisuals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(getSnackbar: GetSnackbarUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            getSnackbar().collectLatest { visuals ->
                _uiState.update { it.copy(snackbarVisuals = visuals) }
            }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.OnSnackbarDismissed -> _uiState.update { it.copy(snackbarVisuals = null) }
        }
    }

    @Immutable
    data class UiState(
        val snackbarVisuals: SnackbarVisuals? = null,
    )

    sealed interface Event {
        data object OnSnackbarDismissed : Event
    }
}
