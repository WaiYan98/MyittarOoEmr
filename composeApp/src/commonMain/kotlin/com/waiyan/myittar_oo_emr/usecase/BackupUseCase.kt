package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.local_service.EmrRepository

class BackupUseCase(
    private val emrRepository: EmrRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return emrRepository.backupDatabase()
    }
}
