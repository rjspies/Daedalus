package com.rjspies.daedalus.domain

import io.kotest.matchers.longs.shouldBeLessThan
import io.kotest.matchers.shouldBe
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetThirtyDayAverageWeightUseCaseTest {
    @Test
    fun `invoke returns flow from service`() = runTest {
        val expected = 82.5f
        val fakeService = CapturingWeightService(MutableStateFlow(expected))
        val useCase = GetThirtyDayAverageWeightUseCase(fakeService)

        useCase().first() shouldBe expected
    }

    @Test
    fun `invoke passes a from datetime that is approximately 30 days ago`() = runTest {
        val fakeService = CapturingWeightService(MutableStateFlow(null))
        val useCase = GetThirtyDayAverageWeightUseCase(fakeService)
        val beforeInvoke = ZonedDateTime.now().minusDays(30)

        useCase().first()

        val capturedFrom = fakeService.capturedFrom!!
        val differenceInSeconds = ChronoUnit.SECONDS.between(beforeInvoke, capturedFrom).coerceAtLeast(0)
        differenceInSeconds shouldBeLessThan 1L
    }
}

private class CapturingWeightService(
    private val averageFlow: Flow<Float?>,
) : WeightService by FakeWeightService() {
    var capturedFrom: ZonedDateTime? = null

    override fun averageWeightSince(from: ZonedDateTime): Flow<Float?> {
        capturedFrom = from
        return averageFlow
    }
}

private class FakeWeightService : WeightService {
    override suspend fun insertWeight(
        value: Float,
        note: String?,
        dateTime: ZonedDateTime,
    ) = Unit
    override suspend fun deleteWeight(weight: Weight) = Unit
    override suspend fun exportWeights(path: String) = Unit
    override suspend fun importWeights(path: String) = Unit
    override fun weightsDescending(): Flow<List<Weight>> = MutableStateFlow(emptyList())
    override fun weightsAscending(): Flow<List<Weight>> = MutableStateFlow(emptyList())
    override fun averageWeightSince(from: ZonedDateTime): Flow<Float?> = MutableStateFlow(null)
}
