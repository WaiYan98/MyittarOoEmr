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
        fun backup(): Result<Unit> {
            return try {
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
                Result.success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }.backup()
}
