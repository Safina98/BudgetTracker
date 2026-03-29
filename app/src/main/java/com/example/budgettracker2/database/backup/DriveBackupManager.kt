package com.example.budgettracker2.database.backup

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class DriveBackupManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        // Replace this with your Web Client ID from Google Cloud Console
        // Cloud Console → APIs & Services → Credentials → Web Client ID
        const val WEB_CLIENT_ID = "468373801236-rdqscl22r69jjfflbtr0m41ro2iqr8lh.apps.googleusercontent.com"
        private const val BACKUP_FILE_NAME = "budget_backup.json"
        // Drive scope for appDataFolder — hidden folder only this app can access
        private const val DRIVE_APPDATA_SCOPE = "https://www.googleapis.com/auth/drive.appdata"
        private const val TAG = "DriveBackup"
    }

    // HTTP client for calling Drive REST API directly
    private val httpClient = OkHttpClient()

    // ── Step 1: Sign in with Google using Credential Manager ─────────────────

    // Signs the user in and returns their Google ID token
    // Must be called from a Composable context (needs Activity)
    // Returns null if sign-in fails or is cancelled
    suspend fun signIn(context: Context): String? {
        val credentialManager = CredentialManager.create(context)

        // Build the sign-in request
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // show all Google accounts on device
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(false) // always show account picker
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            // Extract the Google ID token from the credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                Log.d(TAG, "Sign-in success: ${googleCredential.displayName}")
                googleCredential.idToken
            } else {
                Log.e(TAG, "Unexpected credential type: ${credential.type}")
                null
            }
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Sign-in failed: ${e.message}", e)
            null
        }
    }

    // ── Step 2: Authorize Drive access and get an access token ───────────────

    // Uses AuthorizationClient to request Drive scope access
    // Returns an OAuth access token that can be used in HTTP calls to Drive API
    // pendingIntent will be non-null if user needs to grant permission (needs UI)
    suspend fun getAccessToken(): AuthorizationResult = withContext(Dispatchers.IO) {
        val authRequest = AuthorizationRequest.builder()
            .setRequestedScopes(listOf(Scope(DRIVE_APPDATA_SCOPE)))
            .build()

        return@withContext try {
            val authResult = Identity.getAuthorizationClient(context)
                .authorize(authRequest)
                .await()

            if (authResult.hasResolution()) {
                // User needs to grant Drive permission — return the pending intent
                Log.d(TAG, "Authorization needs user interaction")
                AuthorizationResult.NeedsResolution(authResult.pendingIntent)
            } else {
                // Already authorized — access token is ready
                val token = authResult.accessToken
                Log.d(TAG, "Access token obtained")
                AuthorizationResult.Success(token!!)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Authorization failed: ${e.message}", e)
            AuthorizationResult.Failure(e.message ?: "Authorization failed")
        }
    }

    // Handle authorization result after user grants permission via the pending intent
    suspend fun handleAuthorizationResult(intent: android.content.Intent): AuthorizationResult {
        return try {
            val authResult = Identity.getAuthorizationClient(context)
                .getAuthorizationResultFromIntent(intent)
            val token = authResult.accessToken
            if (token != null) {
                AuthorizationResult.Success(token)
            } else {
                AuthorizationResult.Failure("No access token returned")
            }
        } catch (e: Exception) {
            AuthorizationResult.Failure(e.message ?: "Failed to get token from intent")
        }
    }

    // ── Step 3: Upload JSON to Drive appDataFolder ───────────────────────────

    // Uploads JSON string to Drive appDataFolder
    // Deletes existing backup first if found, then uploads new one
    suspend fun uploadToDrive(accessToken: String, json: String): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "Starting upload, json size=${json.length}")

                // Delete existing backup if any
                val existingId = findFileId(accessToken)
                if (existingId != null) {
                    Log.d(TAG, "Deleting existing backup: $existingId")
                    deleteFile(accessToken, existingId)
                }

                // Step A: Create file metadata (multipart upload)
                // First request creates the file entry with metadata
                val metadataJson = JSONObject().apply {
                    put("name", BACKUP_FILE_NAME)
                    put("parents", JSONArray().apply { put("appDataFolder") })
                }

                // Use multipart upload to send both metadata and content in one request
                val boundary = "backup_boundary"
                val body = buildString {
                    append("--$boundary\r\n")
                    append("Content-Type: application/json; charset=UTF-8\r\n\r\n")
                    append(metadataJson.toString())
                    append("\r\n--$boundary\r\n")
                    append("Content-Type: application/json\r\n\r\n")
                    append(json)
                    append("\r\n--$boundary--")
                }

                val request = Request.Builder()
                    .url("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart&fields=id,name")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .post(body.toRequestBody("multipart/related; boundary=$boundary".toMediaType()))
                    .build()

                val response = httpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    error("Upload failed: ${response.code} ${response.body?.string()}")
                }

                val responseJson = JSONObject(response.body!!.string())
                Log.d(TAG, "Upload success, file id: ${responseJson.getString("id")}")
            }
        }
    }

    // ── Step 4: Download JSON from Drive appDataFolder ───────────────────────

    // Downloads the backup file content from Drive
    // Returns the raw JSON string to be parsed by the repository
    suspend fun downloadFromDrive(accessToken: String): Result<String> {
        return runCatching {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "Starting download...")

                // First find the file ID
                val fileId = findFileId(accessToken)
                    ?: error("No backup file found on Google Drive")

                Log.d(TAG, "Found backup file: $fileId, downloading...")

                // Download file content with alt=media
                val request = Request.Builder()
                    .url("https://www.googleapis.com/drive/v3/files/$fileId?alt=media")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .get()
                    .build()

                val response = httpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    error("Download failed: ${response.code} ${response.body?.string()}")
                }

                val content = response.body!!.string()
                Log.d(TAG, "Download success, size=${content.length}")
                content
            }
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    // Lists files in appDataFolder and returns the ID of the backup file if it exists
    private fun findFileId(accessToken: String): String? {
        val request = Request.Builder()
            .url("https://www.googleapis.com/drive/v3/files?spaces=appDataFolder&q=name%3D%27$BACKUP_FILE_NAME%27&fields=files(id,name)")
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) return null

        val json = JSONObject(response.body!!.string())
        val files = json.getJSONArray("files")
        return if (files.length() > 0) files.getJSONObject(0).getString("id") else null
    }

    // Deletes a file from Drive by its file ID
    private fun deleteFile(accessToken: String, fileId: String) {
        val request = Request.Builder()
            .url("https://www.googleapis.com/drive/v3/files/$fileId")
            .addHeader("Authorization", "Bearer $accessToken")
            .delete()
            .build()
        httpClient.newCall(request).execute()
    }

    // Signs the user out — clears stored credential state
    suspend fun signOut(context: Context) {
        CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
    }
}

// Sealed class representing the result of requesting Drive authorization
sealed class AuthorizationResult {
    data class Success(val accessToken: String) : AuthorizationResult()
    data class NeedsResolution(val pendingIntent: android.app.PendingIntent?) : AuthorizationResult()
    data class Failure(val message: String) : AuthorizationResult()
}