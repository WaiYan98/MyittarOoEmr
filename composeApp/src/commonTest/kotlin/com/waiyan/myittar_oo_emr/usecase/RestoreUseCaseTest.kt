package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class RestoreUseCaseTest {

    private lateinit var emrRepository: EmrRepository
    private lateinit var restoreUseCase: RestoreUseCase

    @BeforeTest
    fun setup() {
        emrRepository = mockk()
        restoreUseCase = RestoreUseCase(emrRepository)
    }

    @Test
    fun `invoke returns success when repository restore is successful`() = runTest {
        // Given
        val uriString = "some/uri"
        coEvery { emrRepository.restoreDatabase(uriString) } returns Result.success(Unit)

        // When
        val result = restoreUseCase(uriString)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when repository restore fails`() = runTest {
        // Given
        val uriString = "some/uri"
        val exception = Exception("Restore failed")
        coEvery { emrRepository.restoreDatabase(uriString) } returns Result.failure(exception)

        // When
        val result = restoreUseCase(uriString)

        // Then
        assertTrue(result.isFailure)
    }
}
