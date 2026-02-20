package com.example.budgettracker2.FragmentTabungan

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgettracker2.RupiahVisualTransformation
import com.example.budgettracker2.viewModels.MainViewModel
import com.example.budgettracker2.viewModels.ManageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabunganScreen(
    manageViewModel: ManageViewModel = hiltViewModel())
{

    val namaTabungan by manageViewModel.namaTabungan.collectAsState()
    val saldo by manageViewModel.saldo.collectAsState()
    val pockets by manageViewModel.allPockets.collectAsState()
    val showSheet = remember { mutableStateOf(false) }
    pockets.forEach {
        Log.i("MainViewModel","${it.pocketName}")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Screen") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.TopStart
        ) {

            LazyColumn {
                items(pockets) { pocket ->
                    Text(text = pocket.pocketName) // Example field
                }
            }
            ExtendedFloatingActionButton(
            onClick = { showSheet.value= true },
                modifier = Modifier
                    .align(Alignment.BottomEnd),
            icon = { Icon(Icons.Filled.Edit, "Edit") },
            text = { Text(text = "Add Tabungan") },
        )
            if (showSheet.value) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet.value = false }
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
                            manageViewModel.insertTabungan()
                            showSheet.value = false
                        }
                    )
                }
            }
        }
    }
}


