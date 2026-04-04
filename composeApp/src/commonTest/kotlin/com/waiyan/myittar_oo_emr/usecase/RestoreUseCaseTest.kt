package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
    fun `invoke calls restoreDatabase on repository`() = runTest {
        // Given
        val uri = "content://database_backup"
        coEvery { emrRepository.restoreDatabase(uri) } returns Result.success(Unit)

        // When
        val result = restoreUseCase(uri)

        // Then
        assertTrue(result.isSuccess)
        coVerify { emrRepository.restoreDatabase(uri) }
    }
}
