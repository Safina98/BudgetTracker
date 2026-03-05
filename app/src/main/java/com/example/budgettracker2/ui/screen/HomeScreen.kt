package com.example.budgettracker2.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.budgettracker2.homescreen.HomeScreenFragmentDirections
import com.example.budgettracker2.ui.widgetstyles.KategoriGridItemList
import com.example.budgettracker2.ui.widgetstyles.PocketHomeScreenItemList
import com.example.budgettracker2.viewModels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onManageMenuClick: () -> Unit,
    onTransactionClick: (Int) -> Unit,
    onNavigateToInput: (Int) -> Unit,
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {

    var expanded by remember { mutableStateOf(false) }
    val thisYearCategorySum by transactionViewModel.thisYearCategorySum.collectAsState()
    val thisYearPocketSum by transactionViewModel.thisYearPocketSum.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Budget Tracker") },
                actions = {

                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Manage") },
                            onClick = {
                                onManageMenuClick()
                                expanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Export") },
                            onClick = {
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Import") },
                            onClick = {
                                expanded = false
                            }
                        )
                    }
                }
            )
        }
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
                        key = { it.pocketTable.pocket_id }   // 🔥 Important for smooth performance
                    ) { pocket ->
                        PocketHomeScreenItemList(pocket)
                    }
                }

                Button(
                    modifier = Modifier.Companion.padding(8.dp),
                    onClick = {
                        onTransactionClick(-1)
                    }

                ) {
                    Text("Kategori")
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
                icon = { Icon(Icons.Filled.Edit, "Edit") },
                text = { Text(text = "Tambah Transaksi") },
            )
        }
    }
}