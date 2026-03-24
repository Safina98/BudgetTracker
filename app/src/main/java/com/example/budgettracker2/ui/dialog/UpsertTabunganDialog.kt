package com.example.budgettracker2.ui.dialog

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.budgettracker2.RupiahVisualTransformation
import com.example.budgettracker2.ui.widgetstyles.BudgetSpinner
import com.example.budgettracker2.warnaList

@Composable
fun UpsertTabunganDialog(
    namaTabungan: String,
    saldo: Int?,
    warnaTabungan:String,
    onNamaChange: (String) -> Unit,
    onSaldoChange: (String) -> Unit,
    onWarnaChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = namaTabungan,
            onValueChange = onNamaChange,
            label = { Text("Nama Tabungan") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = saldo?.toString() ?: "Rp",
            onValueChange = onSaldoChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Companion.Number
            ),
            label = { Text("Saldo") },
            modifier = Modifier.Companion.fillMaxWidth(),
            visualTransformation = RupiahVisualTransformation()
        )
        BudgetSpinner(
            title = "Pilih Warna",
            options = warnaList,
            selectedOption = warnaTabungan,
            onOptionSelected = onWarnaChange,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.Companion.height(16.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}