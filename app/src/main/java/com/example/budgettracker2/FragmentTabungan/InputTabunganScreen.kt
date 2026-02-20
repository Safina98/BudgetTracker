package com.example.budgettracker2.FragmentTabungan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.budgettracker2.RupiahVisualTransformation
import com.example.budgettracker2.rupiahFormatter
import com.example.budgettracker2.viewModels.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun InputTabunganScreen(mainViewModel: MainViewModel){

    val namaTabungan by mainViewModel.namaTabungan.collectAsState()
    val saldo by mainViewModel.saldo.collectAsState()
    val textState = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Screen") }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentAlignment = BiasAlignment(
                horizontalBias = 0f,
                verticalBias = -0.2f
            ) // Centers the Card horizontally and vertically
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
                // Note: Card uses 'colors = CardDefaults.cardColors(...)' for background normally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.Center, // Centers vertically
                    horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
                ) {
                    OutlinedTextField(
                        value = namaTabungan, // Display current state
                        onValueChange = { newText -> mainViewModel.namaTabungan.value=newText }, // Update state when typing
                        label = { Text("Nama Tabungan") },
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        placeholder = { Text("Nama Tabungan")}
                    )
                    OutlinedTextField(
                        value = saldo.toString(), // Display current state
                        onValueChange = { newText ->
                            val cleanString = newText.replace(Regex("[^\\d]"), "")
                            val intValue = cleanString.toIntOrNull()?:0
                            mainViewModel.saldo.value = intValue
                        }, // Update state when typing
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        label = { Text("Saldo") },
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        visualTransformation = RupiahVisualTransformation()
                    )
                    Button(
                        onClick = {
                           mainViewModel.insertTabungan()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Save Word")
                    }

                }
            }
        }
    }
}

