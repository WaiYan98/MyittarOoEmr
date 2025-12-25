package com.waiyan.myittar_oo_emr.util

expect suspend fun backupDatabaseFile(): Result<Unit>
expect suspend fun restoreDatabaseFile(uriString: String): Result<Unit>
