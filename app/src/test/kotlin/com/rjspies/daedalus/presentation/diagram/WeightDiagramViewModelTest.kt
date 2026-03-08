package com.rjspies.daedalus.presentation.diagram

import com.rjspies.daedalus.domain.ExportWeightsUseCase
import com.rjspies.daedalus.domain.GetWeightsAscendingUseCase
import com.rjspies.daedalus.domain.ImportWeightsUseCase
import com.rjspies.daedalus.domain.InsertWeightUseCase
import com.rjspies.daedalus.domain.ShowSnackbarUseCase
import com.rjspies.daedalus.domain.SnackbarRepository
import com.rjspies.daedalus.domain.SnackbarVisuals
import com.rjspies.daedalus.domain.Weight
import com.rjspies.daedalus.domain.WeightService
import com.rjspies.daedalus.presentation.common.StringProvider
import io.kotest.matchers.shouldBe
import java.time.ZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeightDiagramViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeSnackbarRepo: FakeSnackbarRepository
    private lateinit var viewModel: WeightDiagramViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val fakeService = FakeWeightService()
        fakeSnackbarRepo = FakeSnackbarRepository()
        viewModel = WeightDiagramViewModel(
            getWeightsAscending = GetWeightsAscendingUseCase(fakeService),
            insertWeight = InsertWeightUseCase(fakeService),
            exportWeights = ExportWeightsUseCase(fakeService),
            importWeights = ImportWeightsUseCase(fakeService),
            showSnackbar = ShowSnackbarUseCase(fakeSnackbarRepo),
            stringProvider = StringProvider { "" },
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ImportClicked sets importPrompt with CSV mime type and isImporting`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightDiagramViewModel.Event.ImportClicked)

        viewModel.uiState.value.importPrompt shouldBe WeightDiagramViewModel.ImportUiData("text/comma-separated-values")
        viewModel.uiState.value.isImporting shouldBe true
    }

    @Test
    fun `ImportPathChosen resets importPrompt and isImporting`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightDiagramViewModel.Event.ImportClicked)
        viewModel.onEvent(WeightDiagramViewModel.Event.ImportPathChosen(null))

        viewModel.uiState.value.importPrompt shouldBe null
        viewModel.uiState.value.isImporting shouldBe false
    }

    @Test
    fun `ExportClicked sets exportPrompt with CSV mime type and isExporting`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightDiagramViewModel.Event.ExportClicked)

        viewModel.uiState.value.exportPrompt shouldBe WeightDiagramViewModel.ExportUiData("weights.csv", "text/comma-separated-values")
        viewModel.uiState.value.isExporting shouldBe true
    }

    @Test
    fun `PathChosen resets exportPrompt and isExporting`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightDiagramViewModel.Event.ExportClicked)
        viewModel.onEvent(WeightDiagramViewModel.Event.PathChosen(null))

        viewModel.uiState.value.exportPrompt shouldBe null
        viewModel.uiState.value.isExporting shouldBe false
    }

    @Test
    fun `InsertCurrentWeight with valid weight shows success snackbar`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightDiagramViewModel.Event.SetCurrentWeight("70"))
        viewModel.onEvent(WeightDiagramViewModel.Event.InsertCurrentWeight)

        fakeSnackbarRepo.lastVisuals?.isError shouldBe false
    }

    @Test
    fun `PathChosen with null URI shows error snackbar`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightDiagramViewModel.Event.PathChosen(null))

        fakeSnackbarRepo.lastVisuals?.isError shouldBe true
    }

    @Test
    fun `PathChosen with valid URI shows success snackbar`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightDiagramViewModel.Event.PathChosen("content://test/file.csv"))

        fakeSnackbarRepo.lastVisuals?.isError shouldBe false
    }

    @Test
    fun `ImportPathChosen with null URI shows error snackbar`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightDiagramViewModel.Event.ImportPathChosen(null))

        fakeSnackbarRepo.lastVisuals?.isError shouldBe true
    }

    @Test
    fun `ImportPathChosen with valid URI shows success snackbar`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightDiagramViewModel.Event.ImportPathChosen("content://test/file.csv"))

        fakeSnackbarRepo.lastVisuals?.isError shouldBe false
    }

    @Test
    fun `ImportPathChosen with empty file shows error snackbar`() = runTest(testDispatcher) {
        val emptyService = EmptyImportWeightService()
        val vm = WeightDiagramViewModel(
            getWeightsAscending = GetWeightsAscendingUseCase(emptyService),
            insertWeight = InsertWeightUseCase(emptyService),
            exportWeights = ExportWeightsUseCase(emptyService),
            importWeights = ImportWeightsUseCase(emptyService),
            showSnackbar = ShowSnackbarUseCase(fakeSnackbarRepo),
            stringProvider = StringProvider { "" },
        )
        vm.onEvent(WeightDiagramViewModel.Event.ImportPathChosen("content://test/empty.csv"))

        fakeSnackbarRepo.lastVisuals?.isError shouldBe true
    }
}

private class FakeSnackbarRepository : SnackbarRepository {
    var lastVisuals: SnackbarVisuals? = null
    override val snackbarVisuals: Flow<SnackbarVisuals?> = MutableStateFlow(null)
    override suspend fun showSnackbar(visuals: SnackbarVisuals) {
        lastVisuals = visuals
    }
}

private class EmptyImportWeightService : WeightService by FakeWeightService() {
    override suspend fun importWeights(path: String) = throw NoSuchElementException("no data")
}

private class FakeWeightService : WeightService {
    private val weights = MutableStateFlow<List<Weight>>(emptyList())

    override suspend fun insertWeight(
        value: Float,
        note: String?,
        dateTime: ZonedDateTime,
    ) = Unit
    override suspend fun deleteWeight(weight: Weight) = Unit
    override suspend fun exportWeights(path: String) = Unit
    override suspend fun importWeights(path: String) = Unit
    override fun weightsDescending(): Flow<List<Weight>> = weights
    override fun weightsAscending(): Flow<List<Weight>> = weights
}
