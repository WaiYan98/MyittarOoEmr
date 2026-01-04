package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class BackupUseCaseTest {

    private lateinit var emrRepository: EmrRepository
    private lateinit var backupUseCase: BackupUseCase

    @BeforeTest
    fun setup() {
        emrRepository = mockk()
        backupUseCase = BackupUseCase(emrRepository)
    }

    @Test
    fun `invoke returns success when repository backup is successful`() = runTest {
        // Given
        coEvery { emrRepository.backupDatabase() } returns Result.success(Unit)

        // When
        val result = backupUseCase()

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when repository backup fails`() = runTest {
        // Given
        val exception = Exception("Backup failed")
        coEvery { emrRepository.backupDatabase() } returns Result.failure(exception)

        // When
        val result = backupUseCase()

        // Then
        assertTrue(result.isFailure)
    }
}
