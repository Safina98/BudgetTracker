package com.example.budgettracker2.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.budgettracker2.ui.dialog.DeleteConfirmationDialog
import com.example.budgettracker2.ui.widgetstyles.TransactionItemList
import com.example.budgettracker2.viewModels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    id:Int?,
    navController: NavController,
    transactionViewModel: TransactionViewModel = hiltViewModel()) {
    val transactionList by transactionViewModel.transactionList.collectAsState()
    val tipe by transactionViewModel.tipe.collectAsState()
    val errorMessage by transactionViewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val navigateToInput by transactionViewModel.navigateToInput.collectAsState()
    val showDeleteDialog by transactionViewModel.showDeleteDialog.collectAsState()




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
                title = { Text("Transaksi") }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.TopStart
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                state = rememberLazyListState()
            ) { items(
                items = transactionList,
                key = { it.id }
                ){ transaksi ->
                TransactionItemList(
                    transaksi,
                    {
                        transactionViewModel.onDeleteClick(transaksi.id)
                    },
                    {
                        transactionViewModel.onEditTransactionCLick(transaksi)
                    }
                )
                }
            }
            if (showDeleteDialog!=null){
                DeleteConfirmationDialog(
                    "",
                    false,
                    false,
                    onCheckChange = { checked ->

                    },
                    {
                        transactionViewModel.deleteTransaction()
                    },
                    {
                        transactionViewModel.onDeleteDialogDismiss()
                    }
                )

            }

            if (navigateToInput!=null){
                navController.navigate(TransactionFragmentDirections.actionTransactionFragmentToInputFragment3(navigateToInput!!))

                transactionViewModel.onNavigatedToInput()

            }

        }
    }
}