package com.rjspies.daedalus.koin

import androidx.room.Room
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
    singleOf(::WeightServiceImpl) { bind<WeightService>() }
    factoryOf(::GetWeightsAscendingUseCase)
    viewModelOf(::WeightDiagramViewModel)
    factoryOf(::GetWeightsDescendingUseCase)
    factoryOf(::DeleteWeightUseCase)
    viewModelOf(::WeightHistoryViewModel)
    factoryOf(::InsertWeightUseCase)
    viewModelOf(::WeightHistoryItemViewModel)
}
