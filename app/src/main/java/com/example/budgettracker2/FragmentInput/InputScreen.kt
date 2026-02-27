package com.example.budgettracker2.FragmentInput

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.budgettracker2.DateTypeConverter
import com.example.budgettracker2.R
import com.example.budgettracker2.RupiahVisualTransformation
import com.example.budgettracker2.tipeList
import com.example.budgettracker2.ui.dialog.MyDatePickerDialog
import com.example.budgettracker2.ui.widgetstyles.BudgetSpinner
import com.example.budgettracker2.viewModels.ManageViewModel
import com.example.budgettracker2.viewModels.TransactionViewModel
import com.example.budgettracker2.warnaList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    navController: NavController,
    id: Int,
    transactionViewModel: TransactionViewModel = hiltViewModel(),

    ){
    LaunchedEffect(id) {
        transactionViewModel.setTransactionId(id)
    }
    val namaKategori by transactionViewModel.namaKategori.collectAsState()
    val tipe by transactionViewModel.tipe.collectAsState()
    val note by transactionViewModel.note.collectAsState()
    val jumlah by transactionViewModel.jumlah.collectAsState()
    val namaTabungan by transactionViewModel.namaTabungan.collectAsState()
    val namaKategoriList by transactionViewModel.categoryNames.collectAsState()
    val namaTabunganList by transactionViewModel.pocketList.collectAsState()
    val date by transactionViewModel.date.collectAsState()
    val navigateToHomeScreen by transactionViewModel.navigateToHomeScreen.collectAsState()
    val navigateToTransaction by transactionViewModel.navigateToTransaction.collectAsState()
    val showDatePicker by transactionViewModel.showDatePickerDialog.collectAsState()
    val errorMessage by transactionViewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    Log.i("UpdateProblem","inputScreen trans Id(parameter): ${id} kategoriName : ${namaKategori}, note ${note}")
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            transactionViewModel.onErrorDissmiss()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabungan") }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize() // Fill the whole screen
                .padding(16.dp), // Respect Scaffold bars
            contentAlignment = Alignment.Center // Center everything inside
        ) {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                ,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
                // Note: Card uses 'colors = CardDefaults.cardColors(...)' for background normally
            ){
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text= DateTypeConverter.fromDate(date)?:"",
                        Modifier.clickable{
                            transactionViewModel.onShowDatePicker()
                        }
                    )
                    BudgetSpinner(
                        title = "Jenis Transaksi",
                        options = tipeList,
                        selectedOption = tipe,
                        onOptionSelected ={newvalue->transactionViewModel.onTipeSpinnerChange(newvalue)}

                    )
                    BudgetSpinner(
                        title = "Pilih Kategori",
                        options = namaKategoriList,
                        selectedOption = namaKategori,
                        onOptionSelected = {newvalue->transactionViewModel.onKategoriSpinnerChange(newvalue)}
                    )
                    BudgetSpinner(
                        title = "Pilih Tabungan",
                        options = namaTabunganList,
                        selectedOption = namaTabungan,
                        onOptionSelected = {newvalue->transactionViewModel.onTabunganSpinnerChange(newvalue)}
                    )
                    OutlinedTextField(
                        value = note,
                        onValueChange = {newvalue->transactionViewModel.onNoteChange(newvalue)},
                        label = { Text("note") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = jumlah?.toString() ?: "Rp",
                        onValueChange = {newValue->
                            val cleanString = newValue.replace(Regex("[^\\d]"), "")
                            transactionViewModel.onJumlahChange(cleanString)},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        label = { Text("Saldo") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = RupiahVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            transactionViewModel.insertTransaction()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save")
                    }
                    if (showDatePicker) {
                        MyDatePickerDialog(
                            initialDate = date,
                            onDateSelected = { newDate ->
                                // Send the Date object back to the ViewModel
                                transactionViewModel.onDateChange(newDate)
                            },
                            onDismiss = { transactionViewModel.onDismissDatePicker() }
                        )
                    }

                    if (navigateToHomeScreen){
                        navController.popBackStack()
                        transactionViewModel.onNavigatedtoHomeScreen()
                    }
                    if (navigateToTransaction!=null){
                        navController.navigate(InputFragmentDirections.actionInputFragmentToTransactionFragment(navigateToTransaction!!))
                        transactionViewModel.onNavigatedToTransaction()
                    }
                }
            }
    }
}
}