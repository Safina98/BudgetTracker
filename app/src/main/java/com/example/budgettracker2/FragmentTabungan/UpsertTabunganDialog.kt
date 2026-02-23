package com.example.budgettracker2.FragmentTabungan

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

@Composable
fun UpsertTabunganDialog(
    namaTabungan: String,
    saldo: Int?,
    onNamaChange: (String) -> Unit,
    onSaldoChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = namaTabungan,
            onValueChange = onNamaChange,
            label = { Text("Nama Tabungan") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value =  saldo?.toString() ?: "Rp",
            onValueChange = onSaldoChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = { Text("Saldo") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = RupiahVisualTransformation()
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