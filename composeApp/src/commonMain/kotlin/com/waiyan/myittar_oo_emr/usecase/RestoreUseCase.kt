package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.local_service.EmrRepository

class RestoreUseCase(
    private val emrRepository: EmrRepository
) {
    suspend operator fun invoke(uriString: String): Result<Unit> {
        return emrRepository.restoreDatabase(uriString)
    }
}
