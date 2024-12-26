package com.rjspies.daedalus.ui.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rjspies.daedalus.R
import com.rjspies.daedalus.ui.common.SAVED_STATE_HANDLE_KEY_UI_STATE
import com.rjspies.daedalus.ui.destinations.ImprintScreenDestination
import com.rjspies.daedalus.ui.destinations.PrivacyPolicyScreenDestination

class SettingsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val uiState = savedStateHandle.getStateFlow(
        key = SAVED_STATE_HANDLE_KEY_UI_STATE,
        initialValue = SettingsUiState(legals().toList()),
    )

    private fun legals(): Set<SettingItem> = setOf(
        SettingItem(
            itemIdResourceId = R.id.legal_item_privacy_policy,
            titleResourceId = R.string.settings_legal_item_privacy_policy,
            iconResourceId = R.drawable.shield_warning_fill,
            destination = { PrivacyPolicyScreenDestination },
        ),
        SettingItem(
            itemIdResourceId = R.id.legal_item_imprint,
            titleResourceId = R.string.settings_legal_item_imprint,
            iconResourceId = R.drawable.user_list_fill,
            destination = { ImprintScreenDestination },
        ),
    )
}
