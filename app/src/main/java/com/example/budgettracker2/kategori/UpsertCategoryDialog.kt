package com.example.budgettracker2.kategori

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.budgettracker2.tipeList
import com.example.budgettracker2.ui.widgetstyles.BudgetSpinner
import com.example.budgettracker2.warnaList

@Composable
fun UpsertKategoriDialog(
    namaKategori: String,
    tipeKategori:String,
    warnaKategori:String,
    onNamaChange: (String) -> Unit,
    onTipeChange: (String) -> Unit,
    onWarnaChange: (String) -> Unit,

    onSave: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = namaKategori,
            onValueChange = onNamaChange,
            label = { Text("Nama Kategori") },
            modifier = Modifier.fillMaxWidth()
        )
        BudgetSpinner(
            title = "Pilih Tipe",
            options = tipeList,
            selectedOption = tipeKategori,
            onOptionSelected = onTipeChange
        )
        BudgetSpinner(
            title = "Pilih Warna",
            options = warnaList,
            selectedOption = warnaKategori,
            onOptionSelected = onWarnaChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

    }
}