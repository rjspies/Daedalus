package com.rjspies.daedalus.presentation.history

import com.rjspies.daedalus.domain.CoroutineDispatcherProvider
import com.rjspies.daedalus.domain.DeleteWeightUseCase
import com.rjspies.daedalus.domain.ShowSnackbarUseCase
import com.rjspies.daedalus.domain.SnackbarRepository
import com.rjspies.daedalus.domain.SnackbarVisuals
import com.rjspies.daedalus.domain.Weight
import com.rjspies.daedalus.domain.WeightService
import com.rjspies.daedalus.presentation.common.StringProvider
import io.kotest.matchers.shouldBe
import java.time.ZonedDateTime
import kotlinx.coroutines.CoroutineDispatcher
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
class WeightHistoryItemViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeSnackbarRepository: FakeSnackbarRepository
    private lateinit var viewModel: WeightHistoryItemViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeSnackbarRepository = FakeSnackbarRepository()
        viewModel = WeightHistoryItemViewModel(
            deleteWeight = DeleteWeightUseCase(FakeWeightService()),
            dispatcherProvider = FakeDispatcherProvider(testDispatcher),
            showSnackbar = ShowSnackbarUseCase(fakeSnackbarRepository),
            stringProvider = StringProvider { "" },
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ShowDialog sets shouldShowDialog to true`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightHistoryItemViewModel.Event.ShowDialog)

        viewModel.uiState.value.shouldShowDialog shouldBe true
    }

    @Test
    fun `HideDialog sets shouldShowDialog to false`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightHistoryItemViewModel.Event.ShowDialog)
        viewModel.onEvent(WeightHistoryItemViewModel.Event.HideDialog)

        viewModel.uiState.value.shouldShowDialog shouldBe false
    }

    @Test
    fun `DeleteWeight resets dialog state on completion`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightHistoryItemViewModel.Event.ShowDialog)
        viewModel.onEvent(WeightHistoryItemViewModel.Event.DeleteWeight(FakeWeight))

        viewModel.uiState.value.shouldShowDialog shouldBe false
        viewModel.uiState.value.isDialogLoading shouldBe false
        viewModel.uiState.value.isDialogDismissable shouldBe true
    }

    @Test
    fun `DeleteWeight on success shows success snackbar`() = runTest(testDispatcher) {
        viewModel.onEvent(WeightHistoryItemViewModel.Event.DeleteWeight(FakeWeight))

        fakeSnackbarRepository.lastVisuals?.isError shouldBe false
    }

    @Test
    fun `DeleteWeight on failure shows error snackbar`() = runTest(testDispatcher) {
        val failingViewModel = WeightHistoryItemViewModel(
            deleteWeight = DeleteWeightUseCase(FailingWeightService()),
            dispatcherProvider = FakeDispatcherProvider(testDispatcher),
            showSnackbar = ShowSnackbarUseCase(fakeSnackbarRepository),
            stringProvider = StringProvider { "" },
        )

        failingViewModel.onEvent(WeightHistoryItemViewModel.Event.DeleteWeight(FakeWeight))

        fakeSnackbarRepository.lastVisuals?.isError shouldBe true
    }
}

private object FakeWeight : Weight {
    override val id = 1
    override val value = 70f
    override val note: String? = null
    override val dateTime: ZonedDateTime = ZonedDateTime.now()
}

private class FakeDispatcherProvider(private val dispatcher: CoroutineDispatcher) : CoroutineDispatcherProvider {
    override val main = dispatcher
    override val default = dispatcher
    override val io = dispatcher
    override val unconfined = dispatcher
}

private class FakeSnackbarRepository : SnackbarRepository {
    var lastVisuals: SnackbarVisuals? = null
    override val snackbarVisuals: Flow<SnackbarVisuals?> = MutableStateFlow(null)
    override suspend fun showSnackbar(visuals: SnackbarVisuals) {
        lastVisuals = visuals
    }
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
    override fun thirtyDayAverageWeight(): Flow<Float?> = MutableStateFlow(null)
}

private class FailingWeightService : WeightService {
    private val weights = MutableStateFlow<List<Weight>>(emptyList())
    override suspend fun insertWeight(
        value: Float,
        note: String?,
        dateTime: ZonedDateTime,
    ) = Unit
    override suspend fun deleteWeight(weight: Weight) = error("delete failed")
    override suspend fun exportWeights(path: String) = Unit
    override suspend fun importWeights(path: String) = Unit
    override fun weightsDescending(): Flow<List<Weight>> = weights
    override fun weightsAscending(): Flow<List<Weight>> = weights
    override fun thirtyDayAverageWeight(): Flow<Float?> = MutableStateFlow(null)
}
