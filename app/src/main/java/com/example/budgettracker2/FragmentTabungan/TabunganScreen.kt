package com.example.budgettracker2.FragmentTabungan

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgettracker2.ui.dialog.DeleteConfirmationDialog
import com.example.budgettracker2.ui.widgetstyles.TabunganItemList
import com.example.budgettracker2.viewModels.ManageViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabunganScreen(
    manageViewModel: ManageViewModel = hiltViewModel())
{
    val namaTabungan by manageViewModel.namaTabungan.collectAsState()
    val saldo by manageViewModel.saldo.collectAsState()
    val pockets by manageViewModel.allPockets.collectAsState()
    val showSheet by manageViewModel.showUpsertDialog.collectAsState()
    val showDeleteDialog by manageViewModel.showDeleteDialog.collectAsState()
    val deleteAllTransaction by manageViewModel.deleteAllTransaction.collectAsState()
    val errorMessage by manageViewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            manageViewModel.onCloseErrorMessage()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabungan") }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.TopStart
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                state = rememberLazyListState()
            ) {
                items(
                    items = pockets,
                    key = { it.pocketTable.pocket_id }   // 🔥 Important for smooth performance
                ){ pocket ->
                    TabunganItemList(
                        pocket,
                        {
                            manageViewModel.onEditTabunganClick(pocket)
                        },
                        {
                            manageViewModel.onDeleteClick(pocket.pocketTable.pocket_id)
                        }
                    )
                }
            }

            ExtendedFloatingActionButton(
            onClick = { manageViewModel.onShowUpsertDialog() },
                modifier = Modifier
                    .align(Alignment.BottomEnd),
            icon = { Icon(Icons.Filled.Edit, "Edit") },
            text = { Text(text = "Add Tabungan") },
        )
            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { manageViewModel.clearMutbale()}
                ) {
                    UpsertTabunganDialog (
                        namaTabungan = namaTabungan,
                        saldo = saldo,
                        onNamaChange = { manageViewModel.namaTabungan.value = it },
                        onSaldoChange = { input ->
                            val cleanString = input.replace(Regex("[^\\d]"), "")
                            manageViewModel.saldo.value = cleanString.toIntOrNull() ?: 0
                        },
                        onSave = {
                            manageViewModel.onSaveTabunganClick()
                        }
                    )
                }
            }
            if (showDeleteDialog!=null){
                DeleteConfirmationDialog(
                    "",
                    deleteAllTransaction,
                    true,
                    onCheckChange = { checked ->
                        manageViewModel.toggleDeleteAllTransaction(checked)
                    },
                    {
                        manageViewModel.deleteTabungan()
                    },
                    {
                        manageViewModel.onDeleteDialogDismiss()
                    }
                )

            }

        }
    }
}


