package com.example.budgettracker2.ui.backup

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgettracker2.viewModels.BackupAction
import com.example.budgettracker2.viewModels.BackupState
import com.example.budgettracker2.viewModels.BackupViewModel
import com.example.budgettracker2.database.backup.DriveBackupManager
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

// Drop this once in any screen — it handles all sign-in, authorization,
// dialogs, upload and download. Screen only calls onExportClick/onImportClick.
@Composable
fun DriveBackupHandler(
    backupViewModel: BackupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by backupViewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    // Stores which action triggered the flow so we can resume after auth
    var pendingAction by remember { mutableStateOf<BackupAction?>(null) }

    // Launcher for Drive authorization intent
    // Triggered when user needs to grant Drive scope permission
    val authorizationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            Log.d("DriveBackup", "Authorization granted")
            pendingAction?.let { action ->
                backupViewModel.onAuthorizationResult(result.data!!, action)
            }
        } else {
            Log.d("DriveBackup", "Authorization denied or cancelled")
            backupViewModel.resetState()
        }
        pendingAction = null
    }

    // React to each state from BackupViewModel
    when (val s = state) {

        is BackupState.NeedsSignIn -> {
            // Launch Credential Manager sign-in bottom sheet
            LaunchedEffect(s) {
                pendingAction = s.action
                val credentialManager = CredentialManager.create(context)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false) // show all accounts
                    .setServerClientId(DriveBackupManager.WEB_CLIENT_ID)
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                try {
                    val result = credentialManager.getCredential(context, request)
                    val credential = result.credential

                    // Check we got a Google credential
                    if (credential is CustomCredential &&
                        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    ) {
                        Log.d("DriveBackup", "Sign-in success, requesting Drive auth...")
                        // Sign-in done — now request Drive authorization
                        pendingAction?.let { backupViewModel.onSignInSuccess(it) }
                    } else {
                        backupViewModel.resetState()
                    }
                } catch (e: GetCredentialException) {
                    Log.e("DriveBackup", "Sign-in failed: ${e.message}")
                    backupViewModel.resetState()
                }
                pendingAction = null
            }
        }

        is BackupState.NeedsAuthorization -> {
            // User needs to grant Drive scope — launch the authorization intent
            LaunchedEffect(s) {
                pendingAction = s.action
                val pendingIntent = s.pendingIntent
                if (pendingIntent != null) {
                    authorizationLauncher.launch(
                        IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                    )
                } else {
                    backupViewModel.resetState()
                }
            }
        }

        is BackupState.ConfirmImport -> {
            // Show dialog asking user to confirm data overwrite
            AlertDialog(
                onDismissRequest = { backupViewModel.resetState() },
                title = { Text("Restore Backup?") },
                text = { Text("All current data will be replaced with the backup. This cannot be undone.") },
                confirmButton = {
                    TextButton(onClick = { backupViewModel.onConfirmImport(s.accessToken) }) {
                        Text("Restore")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { backupViewModel.resetState() }) {
                        Text("Cancel")
                    }
                }
            )
        }

        is BackupState.Loading -> {
            // Non-dismissible loading dialog during upload/download
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Please wait...") },
                text = { CircularProgressIndicator() },
                confirmButton = {}
            )
        }

        is BackupState.Success -> {
            AlertDialog(
                onDismissRequest = { backupViewModel.resetState() },
                title = { Text("Done") },
                text = { Text("Operation completed successfully.") },
                confirmButton = {
                    TextButton(onClick = { backupViewModel.resetState() }) {
                        Text("OK")
                    }
                }
            )
        }

        is BackupState.Error -> {
            AlertDialog(
                onDismissRequest = { backupViewModel.resetState() },
                title = { Text("Error") },
                text = { Text(s.message) },
                confirmButton = {
                    TextButton(onClick = { backupViewModel.resetState() }) {
                        Text("OK")
                    }
                }
            )
        }

        BackupState.Idle -> Unit // nothing to show
    }
}