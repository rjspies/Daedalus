package com.rjspies.daedalus.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rjspies.daedalus.presentation.common.SAVED_STATE_HANDLE_KEY_UI_STATE

class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val uiState = savedStateHandle.getStateFlow(
        key = SAVED_STATE_HANDLE_KEY_UI_STATE,
        initialValue = MainUiState(),
    )

    fun setShowDialog(showDialog: Boolean) {
        savedStateHandle[SAVED_STATE_HANDLE_KEY_UI_STATE] = uiState.value.copy(showDialog = showDialog)
    }
}
