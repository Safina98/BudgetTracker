package com.example.budgettracker2.kategori

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgettracker2.FragmentTabungan.UpsertTabunganDialog
import com.example.budgettracker2.viewModels.ManageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriScreen(
    manageViewModel: ManageViewModel = hiltViewModel())
{
    val namaKategori by manageViewModel.namaKategori.collectAsState()
    val tipeKategori by manageViewModel.tipeKategori.collectAsState()
    val warnaKategori by manageViewModel.warnaKategori.collectAsState()
    val kategoriList by manageViewModel.allCategory.collectAsState()

    kategoriList.forEach {
        Log.i("Kategori", "${it.category_name_}")
    }

    val showSheet by manageViewModel.showDialog.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kategori") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.TopStart
        ) {
            ExtendedFloatingActionButton(
                onClick = { manageViewModel.onShowDialog() },
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                icon = { Icon(Icons.Filled.Edit, "Edit") },
                text = { Text(text = "Tambah Kategori") },
            )

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { manageViewModel.clearMutbale()}
                ) {
                    UpsertKategoriDialog (
                        namaKategori=namaKategori,
                        tipeKategori=tipeKategori,
                        warnaKategori=warnaKategori,
                        onNamaChange = { manageViewModel.namaKategori.value = it },
                        onTipeChange = {
                            manageViewModel.tipeKategori.value = it
                        },
                        onWarnaChange = {
                            manageViewModel.warnaKategori.value = it
                        },
                        onSave = {
                            manageViewModel.insertKategori()
                        }
                    )
                }
            }
        }
    }

}