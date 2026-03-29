package com.example.budgettracker2.viewModels

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker2.database.backup.AuthorizationResult
import com.example.budgettracker2.database.backup.DriveBackupManager
import com.example.budgettracker2.database.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// All possible states the backup UI can be in
sealed class BackupState {
    object Idle : BackupState()
    object Loading : BackupState()
    object Success : BackupState()
    data class Error(val message: String) : BackupState()
    // User needs to sign in first — UI must trigger Credential Manager
    data class NeedsSignIn(val action: BackupAction) : BackupState()
    // Drive authorization needs user interaction — UI must launch the pending intent
    data class NeedsAuthorization(
        val pendingIntent: android.app.PendingIntent?,
        val action: BackupAction
    ) : BackupState()
    // Ask user to confirm before overwriting local data
    data class ConfirmImport(val accessToken: String) : BackupState()
}

enum class BackupAction { EXPORT, IMPORT }

@HiltViewModel
class BackupViewModel @Inject constructor(
    application: Application,
    private val driveBackupManager: DriveBackupManager,
    private val repository: BudgetRepository
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<BackupState>(BackupState.Idle)
    val state = _state.asStateFlow()

    // ── Entry points from UI ──────────────────────────────────────────────────

    // Called when user taps Export in toolbar
    // Step 1: get Drive access token, then upload
    fun onExportClick() = requestAuthorization(BackupAction.EXPORT)

    // Called when user taps Import in toolbar
    // Step 1: get Drive access token, then show confirm dialog
    fun onImportClick() = requestAuthorization(BackupAction.IMPORT)

    // Called after Credential Manager sign-in succeeds
    // Step 2: now request Drive authorization
    fun onSignInSuccess(action: BackupAction) = requestAuthorization(action)

    // Called after user grants Drive permission via the authorization intent
    // Extracts the access token from the intent result
    fun onAuthorizationResult(intent: Intent, action: BackupAction) {
        viewModelScope.launch {
            val result = driveBackupManager.handleAuthorizationResult(intent)
            when (result) {
                is AuthorizationResult.Success -> {
                    // Got the token — proceed with the original action
                    when (action) {
                        BackupAction.EXPORT -> doExport(result.accessToken)
                        BackupAction.IMPORT -> _state.value = BackupState.ConfirmImport(result.accessToken)
                    }
                }
                is AuthorizationResult.Failure -> {
                    _state.value = BackupState.Error(result.message)
                }
                else -> _state.value = BackupState.Error("Authorization failed")
            }
        }
    }

    // Called when user confirms the import dialog
    fun onConfirmImport(accessToken: String) = doImport(accessToken)

    fun resetState() { _state.value = BackupState.Idle }

    // ── Private workers ───────────────────────────────────────────────────────

    // Requests Drive authorization — tries silently first, asks user if needed
    private fun requestAuthorization(action: BackupAction) {
        viewModelScope.launch {
            _state.value = BackupState.Loading
            when (val result = driveBackupManager.getAccessToken()) {
                is AuthorizationResult.Success -> {
                    // Already authorized — proceed directly
                    when (action) {
                        BackupAction.EXPORT -> doExport(result.accessToken)
                        BackupAction.IMPORT -> _state.value = BackupState.ConfirmImport(result.accessToken)
                    }
                }
                is AuthorizationResult.NeedsResolution -> {
                    // User needs to grant Drive permission — signal UI to launch intent
                    _state.value = BackupState.NeedsAuthorization(result.pendingIntent, action)
                }
                is AuthorizationResult.Failure -> {
                    // Authorization failed entirely — try sign-in first
                    Log.d("DriveBackup", "Auth failed, requesting sign-in: ${result.message}")
                    _state.value = BackupState.NeedsSignIn(action)
                }
            }
        }
    }

    // Reads all Room data → converts to JSON → uploads to Drive
    private fun doExport(accessToken: String) {
        viewModelScope.launch {
            _state.value = BackupState.Loading
            Log.d("DriveBackup", "Reading data from Room...")
            val json = repository.exportToJson()
            Log.d("DriveBackup", "JSON ready, length=${json.length}")

            driveBackupManager.uploadToDrive(accessToken, json)
                .onSuccess {
                    Log.d("DriveBackup", "Export complete")
                    _state.value = BackupState.Success
                }
                .onFailure {
                    Log.e("DriveBackup", "Export failed", it)
                    _state.value = BackupState.Error(it.message ?: "Export failed")
                }
        }
    }

    // Downloads JSON from Drive → parses it → overwrites Room data
    private fun doImport(accessToken: String) {
        viewModelScope.launch {
            _state.value = BackupState.Loading
            Log.d("DriveBackup", "Downloading from Drive...")

            driveBackupManager.downloadFromDrive(accessToken)
                .onSuccess { json ->
                    Log.d("DriveBackup", "Download complete, length=${json.length}")
                    repository.importFromJson(json)
                    Log.d("DriveBackup", "Database restored")
                    _state.value = BackupState.Success
                }
                .onFailure {
                    Log.e("DriveBackup", "Import failed", it)
                    _state.value = BackupState.Error(it.message ?: "Import failed")
                }
        }
    }
}