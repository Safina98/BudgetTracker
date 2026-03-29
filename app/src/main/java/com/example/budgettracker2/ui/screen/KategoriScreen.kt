package com.example.budgettracker2.ui.screen

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgettracker2.ui.backup.DriveBackupHandler
import com.example.budgettracker2.ui.dialog.UpsertKategoriDialog
import com.example.budgettracker2.ui.dialog.DeleteConfirmationDialog
import com.example.budgettracker2.ui.theme.getPocketBrush
import com.example.budgettracker2.ui.widgetstyles.KategoriLinearItemList
import com.example.budgettracker2.ui.widgetstyles.PocketTopAppBar
import com.example.budgettracker2.viewModels.BackupViewModel
import com.example.budgettracker2.viewModels.ManageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriScreen(
    onManageMenuClick:()->Unit,
    manageViewModel: ManageViewModel = hiltViewModel(),
    backupViewModel: BackupViewModel=hiltViewModel()
)
{
    DriveBackupHandler(backupViewModel = backupViewModel)
    val namaKategori by manageViewModel.namaKategori.collectAsState()
    val tipeKategori by manageViewModel.tipeKategori.collectAsState()
    val warnaKategori by manageViewModel.warnaKategori.collectAsState()
    val kategoriList by manageViewModel.allCategory.collectAsState()

    val deleteAllTransaction by manageViewModel.deleteAllTransaction.collectAsState()
    val showSheet by manageViewModel.showUpsertDialog.collectAsState()
    val showDeleteDialog by manageViewModel.showDeleteDialog.collectAsState()
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
            PocketTopAppBar(
                title = "Input Transaksi",
                onManageClick = { onManageMenuClick() },
                onExportClick = { backupViewModel.onExportClick() },
                onImportClick = { backupViewModel.onImportClick() }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier.Companion.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Companion.TopStart
        ) {
            LazyColumn(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(8.dp),
                state = rememberLazyListState()
            ) {
                items(
                    items = kategoriList,
                    key = { it.categoryTable.category_id }   /// 🔥 Important for smooth performance
                ) { kategori ->
                    KategoriLinearItemList(
                        kategori,
                        {
                            manageViewModel.onEditKategoriClick(kategori)
                        },
                        {
                            Log.i("KategoriDelete", "Kategori Screen: delete clicked")
                            manageViewModel.onDeleteClick(kategori.categoryTable.category_id)
                        }
                    )
                }
            }
            ExtendedFloatingActionButton(
                onClick = { manageViewModel.onShowUpsertDialog() },
                modifier = Modifier.Companion
                    .align(Alignment.Companion.BottomEnd),
                icon = { Icon(Icons.Filled.Edit, "Edit") },
                text = { Text(text = "Tambah Kategori") },
            )

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { manageViewModel.clearMutbale() }
                ) {
                    UpsertKategoriDialog(
                        namaKategori = namaKategori,
                        tipeKategori = tipeKategori,
                        warnaKategori = warnaKategori,
                        onNamaChange = { manageViewModel.onKategoriNamaChange(it) },
                        onTipeChange = {
                            manageViewModel.onKategoriTipeChange(it)
                        },
                        onWarnaChange = {
                            manageViewModel.onWarnaCategoryChange(it)
                        },
                        onSave = {
                            manageViewModel.onKategoriSaveClick()
                        }
                    )
                }
            }
            if (showDeleteDialog != null) {
                DeleteConfirmationDialog(
                    "",
                    deleteAllTransaction,
                    true,
                    onCheckChange = { checked ->
                        manageViewModel.toggleDeleteAllTransaction(checked)
                    },
                    {
                        manageViewModel.deleteKategori()
                    },
                    {
                        manageViewModel.onDeleteDialogDismiss()
                    }
                )

            }
        }
    }

}