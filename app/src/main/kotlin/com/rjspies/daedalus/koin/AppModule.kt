package com.rjspies.daedalus.koin

import com.rjspies.daedalus.data.SnackbarServiceImpl
import com.rjspies.daedalus.data.WeightDatabase
import com.rjspies.daedalus.data.WeightServiceImpl
import com.rjspies.daedalus.domain.DeleteWeightUseCase
import com.rjspies.daedalus.domain.ExportWeightsUseCase
import com.rjspies.daedalus.domain.GetSnackbarQueueUseCase
import com.rjspies.daedalus.domain.GetWeightsAscendingUseCase
import com.rjspies.daedalus.domain.GetWeightsDescendingUseCase
import com.rjspies.daedalus.domain.InsertWeightUseCase
import com.rjspies.daedalus.domain.RequestSnackbarUseCase
import com.rjspies.daedalus.domain.SnackbarService
import com.rjspies.daedalus.domain.WeightService
import com.rjspies.daedalus.presentation.MainViewModel
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
    singleOf(::SnackbarServiceImpl) { bind<SnackbarService>() }
    factoryOf(::GetWeightsAscendingUseCase)
    factoryOf(::GetWeightsDescendingUseCase)
    factoryOf(::DeleteWeightUseCase)
    factoryOf(::InsertWeightUseCase)
    factoryOf(::ExportWeightsUseCase)
    factoryOf(::RequestSnackbarUseCase)
    factoryOf(::GetSnackbarQueueUseCase)
    viewModelOf(::WeightHistoryViewModel)
    viewModelOf(::WeightDiagramViewModel)
    viewModelOf(::WeightHistoryItemViewModel)
    viewModelOf(::MainViewModel)
}
