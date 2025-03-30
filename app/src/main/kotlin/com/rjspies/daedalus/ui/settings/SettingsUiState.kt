package com.rjspies.daedalus.ui.settings

import com.rjspies.daedalus.ui.common.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsUiState(
    val settingItems: List<SettingItem> = emptyList(),
) : UiState
