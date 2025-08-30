package com.rjspies.daedalus.koin

import com.rjspies.daedalus.data.WeightDatabase
import com.rjspies.daedalus.data.WeightServiceImpl
import com.rjspies.daedalus.domain.DeleteWeightUseCase
import com.rjspies.daedalus.domain.GetWeightsAscendingUseCase
import com.rjspies.daedalus.domain.GetWeightsDescendingUseCase
import com.rjspies.daedalus.domain.InsertWeightUseCase
import com.rjspies.daedalus.domain.WeightService
import com.rjspies.daedalus.presentation.diagram.WeightDiagramViewModel
import com.rjspies.daedalus.presentation.history.WeightHistoryItemViewModel
import com.rjspies.daedalus.presentation.history.WeightHistoryViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val APP_MODULE = module {
    singleOf(WeightDatabase::getDatabase)
    singleOf(WeightDatabase::weightDao)
    singleOf(::WeightServiceImpl) { bind<WeightService>() }
    factoryOf(::GetWeightsAscendingUseCase)
    factoryOf(::GetWeightsDescendingUseCase)
    factoryOf(::DeleteWeightUseCase)
    factoryOf(::InsertWeightUseCase)
    viewModelOf(::WeightHistoryViewModel)
    viewModelOf(::WeightDiagramViewModel)
    viewModelOf(::WeightHistoryItemViewModel)
}
