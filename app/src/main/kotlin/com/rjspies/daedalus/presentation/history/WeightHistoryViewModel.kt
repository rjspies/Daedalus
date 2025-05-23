package com.rjspies.daedalus.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.domain.DeleteWeightUseCase
import com.rjspies.daedalus.domain.GetWeightsDescendingUseCase
import com.rjspies.daedalus.domain.Weight
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class WeightHistoryViewModel(
    getWeightsDescendingUseCase: GetWeightsDescendingUseCase,
    private val deleteWeightUseCase: DeleteWeightUseCase,
) : ViewModel() {
    val weights = getWeightsDescendingUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    suspend fun deleteWeight(weight: Weight) = deleteWeightUseCase(weight)
}
