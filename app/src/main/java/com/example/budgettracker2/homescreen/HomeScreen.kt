package com.example.budgettracker2.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.budgettracker2.ui.widgetstyles.KategoriGridItemList
import com.example.budgettracker2.ui.widgetstyles.beige
import com.example.budgettracker2.ui.widgetstyles.midnightGreen
import com.example.budgettracker2.ui.widgetstyles.roseBrown
import com.example.budgettracker2.viewModels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel=hiltViewModel()
) {

    var expanded by remember { mutableStateOf(false) }
    val thisYearCategorySum by transactionViewModel.thisYearCategorySum.collectAsState()

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
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            Column(modifier=Modifier.padding(16.dp).fillMaxSize()) {
                Card(
                    modifier = Modifier.padding(8.dp)
                        .width(300.dp)
                        .height(100.dp)
                        .background(Color.Transparent)
                        .background(brush = midnightGreen)
                ) {
                        Text("Pendapatan Tahun Ini \nRp. 100.000")
                }
                Card(
                    modifier = Modifier.padding(8.dp)
                        .width(300.dp)
                        .height(100.dp)
                        .background(Color.Transparent)
                        .background(brush = roseBrown)
                ) {
                        Text("Tabungan Laptop \nRp. 100.000")
                }
                Card(
                    modifier = Modifier.padding(8.dp)
                        .width(300.dp)
                        .height(100.dp)
                        .background(Color.Transparent)
                        .background(brush = beige)
                ) {
                        Text("Pengeluaran Tahun ini \nRp. 100.000")
                }
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        navController.navigate(HomeScreenFragmentDirections.actionCategoryFragmentToTransactionFragment(-1))
                    }
                ) {
                    Text("Kategori")
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // 2 columns
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(thisYearCategorySum) { category ->
                        KategoriGridItemList(category)
                    }
                }

            }
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(HomeScreenFragmentDirections.actionCategoryFragmentToInputFragment(-1)) },
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                icon = { Icon(Icons.Filled.Edit, "Edit") },
                text = { Text(text = "Tambah Transaksi") },
            )
        }
    }
}