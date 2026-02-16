package com.example.budgettracker2.backup

import android.content.Context
import com.example.budgettracker2.database.BudgetDB
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.FileList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.util.zip.*

class DriveBackupManager(
    private val context: Context,
    private val account: GoogleSignInAccount
) {

    private fun getDriveService(): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account

        return Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )

            .setApplicationName("BudgetTracker")
            .build()
    }

    suspend fun backupToDrive() = withContext(Dispatchers.IO) {

        val drive = getDriveService()

        val zipFile = zipDatabase()

        // Delete old backup if exists
        val files: FileList = drive.files().list()
            .setQ("name='budget_backup.zip'")
            .execute()

        files.files?.forEach {
            drive.files().delete(it.id).execute()
        }

        val metadata = com.google.api.services.drive.model.File().apply {
            name = "budget_backup.zip"
        }

        val mediaContent = com.google.api.client.http.FileContent(
            "application/zip",
            zipFile
        )

        drive.files()
            .create(metadata, mediaContent)
            .setFields("id")
            .execute()
    }

    suspend fun restoreFromDrive() = withContext(Dispatchers.IO) {

        val drive = getDriveService()

        val files = drive.files().list()
            .setQ("name='budget_backup.zip'")
            .execute()

        if (files.files.isNullOrEmpty()) return@withContext

        val fileId = files.files[0].id

        val outputFile = File(context.cacheDir, "restore.zip")

        FileOutputStream(outputFile).use { output ->
            drive.files().get(fileId)
                .executeMediaAndDownloadTo(output)
        }

        restoreDatabase(outputFile)
    }

    private fun zipDatabase(): File {

        val dbPath = context.getDatabasePath("budget_db").path
        val zipFile = File(context.cacheDir, "budget_backup.zip")

        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->

            listOf("", "-wal", "-shm").forEach { suffix ->
                val file = File(dbPath + suffix)
                if (file.exists()) {
                    FileInputStream(file).use { fis ->
                        zos.putNextEntry(ZipEntry(file.name))
                        fis.copyTo(zos)
                        zos.closeEntry()
                    }
                }
            }
        }

        return zipFile
    }

    private fun restoreDatabase(zipFile: File) {

        BudgetDB.getInstance(context).close()
        BudgetDB.destroyInstance()

        val dbDir = context.getDatabasePath("budget_db").parentFile!!

        ZipInputStream(FileInputStream(zipFile)).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {

                val outFile = File(dbDir, entry.name)

                FileOutputStream(outFile).use { fos ->
                    zis.copyTo(fos)
                }

                entry = zis.nextEntry
            }
        }
    }
}
