package com.waiyan.myittar_oo_emr.util

import android.os.Environment
import com.waiyan.myittar_oo_emr.di.AndroidAppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual suspend fun backupDatabaseFile(): Result<Unit> = withContext(Dispatchers.IO) {
    object : KoinComponent {
        val appContext: AndroidAppContext by inject()
        val database: com.waiyan.myittar_oo_emr.local.database.EmrDatabase by inject()

        fun backup(): Result<Unit> {
            return try {
                android.util.Log.d("BackupDB", "Starting backup process.")

                val context = appContext.context
                val dbFile = context.getDatabasePath("emr_database.db")

                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }

                val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val backupFileName = "emr_backup_${dateFormat.format(Date())}.db"
                val backupFile = File(downloadsDir, backupFileName)

                FileInputStream(dbFile).use { input ->
                    FileOutputStream(backupFile).use { output ->
                        input.copyTo(output)
                    }
                }
                android.util.Log.d("BackupDB", "Backup successful.")
                Result.success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("BackupDB", "Backup failed with exception.", e)
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }.backup()
}

actual suspend fun restoreDatabaseFile(uriString: String): Result<Unit> = withContext(Dispatchers.IO) {
    object : KoinComponent {
        val appContext: AndroidAppContext by inject()
        val database: com.waiyan.myittar_oo_emr.local.database.EmrDatabase by inject()

        fun restore(): Result<Unit> {
            return try {
                android.util.Log.d("RestoreDB", "Starting restore process with URI: $uriString")
                val context = appContext.context
                val dbFile = context.getDatabasePath("emr_database.db")
                val contentResolver = context.contentResolver

                val uri = android.net.Uri.parse(uriString)

                android.util.Log.d("RestoreDB", "Closing database before restore.")
                database.close()

                contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(dbFile).use { output ->
                        input.copyTo(output)
                    }
                } ?: return Result.failure(Exception("Could not open input stream for backup file."))

                android.util.Log.d("RestoreDB", "Restore successful.")
                Result.success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("RestoreDB", "Restore failed with exception.", e)
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }.restore()
}

