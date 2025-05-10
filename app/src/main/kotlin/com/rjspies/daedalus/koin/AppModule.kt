package com.rjspies.daedalus.koin

import androidx.room.Room
import com.rjspies.daedalus.data.WeightDatabase
import com.rjspies.daedalus.data.WeightServiceImpl
import com.rjspies.daedalus.domain.DeleteWeightUseCase
import com.rjspies.daedalus.domain.GetAverageWeightUseCase
import com.rjspies.daedalus.domain.GetWeightsAscendingUseCase
import com.rjspies.daedalus.domain.GetWeightsDescendingUseCase
import com.rjspies.daedalus.domain.InsertWeightUseCase
import com.rjspies.daedalus.domain.WeightService
import com.rjspies.daedalus.presentation.MainViewModel
import com.rjspies.daedalus.presentation.averageweight.AverageWeightWidgetViewModel
import com.rjspies.daedalus.presentation.diagram.WeightDiagramViewModel
import com.rjspies.daedalus.presentation.history.WeightHistoryViewModel
import com.rjspies.daedalus.presentation.insertweight.InsertWeightViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room
            .databaseBuilder(
                context = get(),
                klass = WeightDatabase::class.java,
                name = "weight_database",
            ).build()
    }
    single { get<WeightDatabase>().weightDao() }
    single<WeightService> { WeightServiceImpl(get()) }
    viewModel { MainViewModel(get()) }
    factory { GetWeightsAscendingUseCase(get()) }
    viewModel { WeightDiagramViewModel(get()) }
    factory { GetWeightsDescendingUseCase(get()) }
    factory { DeleteWeightUseCase(get()) }
    viewModel { WeightHistoryViewModel(get(), get()) }
    factory { InsertWeightUseCase(get()) }
    viewModel { InsertWeightViewModel(get(), get()) }
    factory { GetAverageWeightUseCase(get()) }
    viewModel { AverageWeightWidgetViewModel(get()) }
}
