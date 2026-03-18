package com.example.budgettracker2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.budgettracker2.DateTypeConverter
import com.example.budgettracker2.transactions.TransactionFragmentDirections
import com.example.budgettracker2.ui.dialog.DeleteConfirmationDialog
import com.example.budgettracker2.ui.theme.AppTypography
import com.example.budgettracker2.ui.theme.getPocketBrush
import com.example.budgettracker2.ui.widgetstyles.PocketTopAppBar
import com.example.budgettracker2.ui.widgetstyles.TransactionItemList
import com.example.budgettracker2.viewModels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    id:Int?,
   onEditTransactionClick: (Int) -> Unit,
    onManageMenuClick: () -> Unit,
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    val transactionList by transactionViewModel.transactionList.collectAsState()
    val tipe by transactionViewModel.tipe.collectAsState()
    val errorMessage by transactionViewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val navigateToInput by transactionViewModel.navigateToInput.collectAsState()
    val showDeleteDialog by transactionViewModel.showDeleteDialog.collectAsState()


    var searchQuery by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }

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
        topBar = {PocketTopAppBar(
            title = "TRANSAKSI",
            onManageClick = { onManageMenuClick() },
            onExportClick = { /* handle export */ },
            onImportClick = { /* handle import */ }
        )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent,

    ) { padding ->

        Box(
            modifier = Modifier.Companion.fillMaxSize().padding(padding),
            contentAlignment = Alignment.TopEnd
        ) {
           Column(modifier = Modifier.Companion
               .fillMaxSize()
               ) {
               Card(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(60.dp),
                   shape = RoundedCornerShape(1.dp),
                   elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
               ){
                   Row(modifier = Modifier.Companion
                       .fillMaxSize()
                       .background(getPocketBrush("Olive")),
                   )
                   {
                       TextField(
                           value = searchQuery,
                           onValueChange = { searchQuery = it },
                           placeholder = { Text("Search...") },
                           modifier = Modifier
                               .weight(1f)
                               .padding(16.dp)
                               .clip(RoundedCornerShape(8.dp)),
                           singleLine = true,
                           leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                           trailingIcon = {
                               if (searchQuery.isNotEmpty()) {
                                   IconButton(onClick = { searchQuery = "" }) {
                                       Icon(Icons.Default.Close, contentDescription = "Clear")
                                   }
                               }
                           },
                           colors = TextFieldDefaults.colors(
                               focusedContainerColor = Color.Transparent,
                               unfocusedContainerColor = Color.Transparent,
                               focusedIndicatorColor = Color.Transparent,
                               unfocusedIndicatorColor = Color.Transparent
                           )
                       )
                       Text(
                           text = "FILTER",
                           style = AppTypography.titleMedium,
                           color = Color.White,
                           modifier = Modifier.padding(vertical = 16.dp)
                       )
                       IconButton(
                           onClick = {},
                           modifier = Modifier.size(50.dp)
                       ) {
                           Icon(
                               imageVector = Icons.Default.Tune,
                               contentDescription = "Delete",
                               tint = Color.White,
                               modifier = Modifier.size(35.dp)
                           )
                       }
                   }
                }
               LazyColumn(
                   modifier = Modifier.Companion
                       .fillMaxWidth()
                   .padding(4.dp),
                   state = rememberLazyListState()
               ) {
                   items(
                       items = transactionList,
                       key = { it.id }
                   ) { transaksi ->
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
           }

            ExtendedFloatingActionButton(
                onClick = { onEditTransactionClick(-1) },
                modifier = Modifier.align(Alignment.Companion.BottomEnd),
                // .background(brush = getPocketBrush("Top Bar Color"), shape = RoundedCornerShape(16.dp)),
                icon = { Icon(Icons.Filled.Edit, "Edit") },
                text = { Text(text = "Tambah Transaksi") },
                containerColor = Color(0xFF887d77),
                contentColor = Color.White
            )
            if (showDeleteDialog != null) {
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
            LaunchedEffect(navigateToInput) {
                if (navigateToInput!=null) {
                    onEditTransactionClick(navigateToInput!!)
                    transactionViewModel.onNavigatedToInput()
                }
            }


        }
    }
}