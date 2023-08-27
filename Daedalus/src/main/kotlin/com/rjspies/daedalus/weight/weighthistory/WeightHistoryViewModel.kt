package com.rjspies.daedalus.weight.weighthistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.weight.service.WeightService
import com.rjspies.daedalus.weight.service.data.Weight
import org.koin.android.annotation.KoinViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@KoinViewModel
class WeightHistoryViewModel(private val service: WeightService) : ViewModel() {
    val weights = service.weights().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    suspend fun deleteWeight(weight: Weight) = service.deleteWeight(weight)
}
