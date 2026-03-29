package com.example.budgettracker2.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgettracker2.ui.backup.DriveBackupHandler
import com.example.budgettracker2.ui.theme.AppTypography
import com.example.budgettracker2.ui.widgetstyles.ArrowShape
import com.example.budgettracker2.ui.widgetstyles.KategoriGridItemList
import com.example.budgettracker2.ui.widgetstyles.PocketHomeScreenItemList
import com.example.budgettracker2.ui.widgetstyles.PocketTopAppBar
import com.example.budgettracker2.viewModels.BackupViewModel
import com.example.budgettracker2.viewModels.TransactionViewModel

/*
* 468373801236-rdqscl22r69jjfflbtr0m41ro2iqr8lh.apps.googleusercontent.com
* */



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onManageMenuClick: () -> Unit,
    onTransactionClick: (Int) -> Unit,
    onNavigateToInput: (Int) -> Unit,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    backupViewModel: BackupViewModel=hiltViewModel()
) {
    DriveBackupHandler(backupViewModel = backupViewModel)
    var expanded by remember { mutableStateOf(false) }
    val thisYearCategorySum by transactionViewModel.thisYearCategorySum.collectAsState()
    val thisYearPocketSum by transactionViewModel.thisYearPocketSum.collectAsState()
    Scaffold(
        topBar = {PocketTopAppBar(
            title = "LAPORAN TAHUN INI",
            onManageClick = { onManageMenuClick() },
            onExportClick = { backupViewModel.onExportClick() },
            onImportClick = { backupViewModel.onImportClick() }
        )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier.Companion
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.Companion
                .padding(16.dp)
                .fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.Companion
                        .fillMaxHeight(0.4f)
                        .fillMaxWidth(),
                    state = rememberLazyListState()
                ) {
                    items(
                        items = thisYearPocketSum,
                        key = { it.pocketTable.pocket_id }
                    ) { pocket ->
                        PocketHomeScreenItemList(pocket)
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            onTransactionClick(-1)
                        },
                        shape = ArrowShape()
                    ) {
                        Text(
                            "KATEGORI",
                            style = AppTypography.titleMedium,
                            color= Color.White
                        )
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // 2 columns
                    modifier = Modifier.Companion.fillMaxSize()
                ) {
                    items(thisYearCategorySum) { category ->
                        KategoriGridItemList(category)
                    }
                }
            }
            ExtendedFloatingActionButton(
                onClick = { onNavigateToInput(-1) },
                modifier = Modifier.Companion
                    .align(Alignment.Companion.BottomEnd),
                   // .background(brush = getPocketBrush("Top Bar Color"), shape = RoundedCornerShape(16.dp)),
                icon = { Icon(Icons.Filled.Edit, "Edit") },
                text = { Text(text = "Tambah Transaksi") },
                containerColor = Color(0xFF887d77),
                contentColor = Color.White
            )
        }
    }
}