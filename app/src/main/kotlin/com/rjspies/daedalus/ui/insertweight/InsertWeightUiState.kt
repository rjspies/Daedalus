package com.rjspies.daedalus.ui.insertweight

import com.rjspies.daedalus.InsertWeightError
import com.rjspies.daedalus.ui.common.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class InsertWeightUiState(
    val isLoading: Boolean = false,
    val error: InsertWeightError? = null,
    val dismissDialog: Boolean = false,
) : UiState
