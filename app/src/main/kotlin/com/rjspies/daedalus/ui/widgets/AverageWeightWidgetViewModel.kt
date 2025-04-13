package com.rjspies.daedalus.ui.widgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.domain.GetAverageWeightUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AverageWeightWidgetViewModel(
    getAverageWeightUseCase: GetAverageWeightUseCase
) : ViewModel() {
    val averageWeight = getAverageWeightUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0f,
    )
}
