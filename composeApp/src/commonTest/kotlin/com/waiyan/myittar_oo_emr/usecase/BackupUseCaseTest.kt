package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
    fun `invoke calls backupDatabase on repository`() = runTest {
        // Given
        coEvery { emrRepository.backupDatabase() } returns Result.success(Unit)

        // When
        val result = backupUseCase()

        // Then
        assertTrue(result.isSuccess)
        coVerify { emrRepository.backupDatabase() }
    }
}
