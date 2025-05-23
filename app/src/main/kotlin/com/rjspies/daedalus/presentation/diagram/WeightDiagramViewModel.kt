package com.rjspies.daedalus.presentation.diagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.domain.GetWeightsAscendingUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class WeightDiagramViewModel(
    getWeightsAscendingUseCase: GetWeightsAscendingUseCase,
) : ViewModel() {
    val weights = getWeightsAscendingUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )
}
