package com.rjspies.daedalus.ui.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rjspies.daedalus.ui.common.SAVED_STATE_HANDLE_KEY_UI_STATE

class SettingsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val uiState = savedStateHandle.getStateFlow(
        key = SAVED_STATE_HANDLE_KEY_UI_STATE,
        initialValue = SettingsUiState(settingItems().toList()),
    )

    private fun settingItems(): Set<SettingItem> = emptySet()
}
