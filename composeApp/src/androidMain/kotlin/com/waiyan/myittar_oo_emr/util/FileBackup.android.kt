package com.waiyan.myittar_oo_emr.util

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.waiyan.myittar_oo_emr.AndroidAppContext
import com.waiyan.myittar_oo_emr.local.database.EmrDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri

actual suspend fun backupDatabaseFile(): Result<Unit> = withContext(Dispatchers.IO) {
    object : KoinComponent {
        val appContext: AndroidAppContext by inject()
        @RequiresApi(Build.VERSION_CODES.Q)
        fun backup(): Result<Unit> {
            val context = appContext.context
            val contentResolver = context.contentResolver
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val backupFileName = "emr_backup_${dateFormat.format(Date())}.db"

            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, backupFileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }
            }

            val uri = contentResolver.insert(collection, contentValues)
                ?: return Result.failure(Exception("Failed to create new MediaStore entry."))

            try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val dbFile = context.getDatabasePath("emr_database.db")
                    FileInputStream(dbFile).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                } ?: return Result.failure(Exception("Failed to open output stream."))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                    contentResolver.update(uri, contentValues, null, null)
                }
                android.util.Log.d("BackupDB", "Backup successful.")
                return Result.success(Unit)
            } catch (e: Exception) {
                // If something fails, delete the created entry
                contentResolver.delete(uri, null, null)
                android.util.Log.e("BackupDB", "Backup failed with exception.", e)
                e.printStackTrace()
                return Result.failure(e)
            }
        }
    }.backup()
}

actual suspend fun restoreDatabaseFile(uriString: String): Result<Unit> = withContext(Dispatchers.IO) {
    object : KoinComponent {
        val appContext: AndroidAppContext by inject()
        val database: EmrDatabase by inject()

        fun restore(): Result<Unit> {
            return try {
                android.util.Log.d("RestoreDB", "Starting restore process with URI: $uriString")
                val context = appContext.context
                val dbFile = context.getDatabasePath("emr_database.db")
                val contentResolver = context.contentResolver

                val uri = uriString.toUri()

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

