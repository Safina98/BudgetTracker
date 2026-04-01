package com.example.budgettracker2.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgettracker2.ui.dialog.DateRangePickerDialog
import com.example.budgettracker2.ui.dialog.DeleteConfirmationDialog
import com.example.budgettracker2.ui.dialog.FilterDialog
import com.example.budgettracker2.ui.theme.AppTypography
import com.example.budgettracker2.ui.theme.getPocketBrush
import com.example.budgettracker2.ui.widgetstyles.PocketTopAppBar
import com.example.budgettracker2.ui.widgetstyles.TransactionItemList
import com.example.budgettracker2.viewModels.TransactionViewModel
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.budgettracker2.ui.backup.DriveBackupHandler
import com.example.budgettracker2.ui.widgetstyles.formatRupiah
import com.example.budgettracker2.viewModels.BackupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    id:Int?,
   onEditTransactionClick: (Int) -> Unit,
    onManageMenuClick: () -> Unit,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    backupViewModel: BackupViewModel=hiltViewModel()
) {
    DriveBackupHandler(backupViewModel = backupViewModel)
    val transactionList by transactionViewModel.filteredTransactions.collectAsState()
    val selectedTipe by transactionViewModel.selectedTipe.collectAsState()
    val selectedPocket by transactionViewModel.selectedPocket.collectAsState()
    val selectedCategory by transactionViewModel.selectedCategory.collectAsState()
    val selectedYear by transactionViewModel.selectedYear.collectAsState()
    val selectedMonth by transactionViewModel.selectedMonth.collectAsState()
    val selectedDate by transactionViewModel.startDate.collectAsState()
    val showFilter by transactionViewModel.showFilter.collectAsState()
    val pocktetListFilter by transactionViewModel.pocktetListFilter.collectAsState()
    val categoryListFilter by transactionViewModel.categoryListFilter.collectAsState()
    val showDatePicker by transactionViewModel.showDatePickerDialog.collectAsState()
    val dateString by transactionViewModel.dateString.collectAsState()
    val totalNominal by transactionViewModel.totalNominal.collectAsState()

    val errorMessage by transactionViewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val navigateToInput by transactionViewModel.navigateToInput.collectAsState()
    val showDeleteDialog by transactionViewModel.showDeleteDialog.collectAsState()

    var cardHeightPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current


    val searchQuery by transactionViewModel.searchQuery.collectAsState()
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
            onExportClick = { backupViewModel.onExportClick() },
            onImportClick = { backupViewModel.onImportClick() }
        )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent,

    ) { padding ->

        Box(
            modifier = Modifier.Companion.fillMaxSize().padding(padding)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            contentAlignment = Alignment.TopEnd
        ) {

           Column(modifier = Modifier.Companion
               .fillMaxSize()
               ) {
               Card(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(60.dp)
                   .onGloballyPositioned { coordinates ->
                   cardHeightPx = coordinates.size.height // 👈
               },
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
                           onValueChange = {
                               newvalue->transactionViewModel.onSearchQueryChange(newvalue)
                               Log.d("DEBUG", "typed: $newvalue")
                                           },
                           placeholder = { Text("Search...") },
                           modifier = Modifier
                               .weight(1f)
                               .padding(4.dp)
                               .clip(RoundedCornerShape(8.dp)),
                           singleLine = true,
                           leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                           trailingIcon = {
                               if (searchQuery.isNotEmpty()) {
                                   IconButton(onClick = { transactionViewModel.onSearchQueryChange("")}
                                   ) {
                                       Icon(Icons.Default.Close, contentDescription = "Clear")
                                   }
                               }
                           },
                           colors = TextFieldDefaults.colors(
                               unfocusedTextColor = Color.Black,
                               focusedContainerColor = Color.Transparent,
                               unfocusedContainerColor = Color.Transparent,
                               focusedIndicatorColor = Color.Transparent,
                               unfocusedIndicatorColor = Color.Transparent,)
                       )
                       Text(
                           text = "FILTER",
                           style = AppTypography.titleMedium,
                           color = Color.White,
                           modifier = Modifier.padding(vertical = 16.dp).clickable {
                              transactionViewModel.onFilterClick()
                           }
                       )
                       IconButton(
                           onClick = {transactionViewModel.onFilterClick()},
                           modifier = Modifier.size(50.dp)
                       ) {
                           Icon(
                               imageVector = Icons.Default.Tune,
                               contentDescription = "Delete",
                               tint = Color.White,
                               modifier = Modifier.size(30.dp)
                           )
                       }
                   }
                }

               LazyColumn(
                   modifier = Modifier
                       .fillMaxWidth()
                   .padding(4.dp)
                   .padding(bottom = 70.dp),
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
                               Log.d("LoadTransactionProbs","TransactionFragment id ${transaksi.id}")
                               onEditTransactionClick(transaksi.id)
                               //transactionViewModel.onNavigateToInput(transaksi.id)
                           }
                       )
                   }
               }
           }
            AnimatedVisibility(
                visible = showFilter,
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { -it } // 👈 slides down from top
                ),
                exit = fadeOut() + slideOutVertically(
                    targetOffsetY = { -it }
                ),
                modifier = Modifier.align(Alignment.TopCenter)
                    .offset(y = with(density) { cardHeightPx.toDp() })

            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(1.dp),

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = getPocketBrush("Olive")) // 👈 move here
                    ) {
                        FilterDialog(
                            pocketList = pocktetListFilter,
                            categoryList = categoryListFilter,
                            selectedYear = selectedYear,
                            selectedMonth = selectedMonth,
                            selectedTipe = selectedTipe,
                            selectedPocket = selectedPocket,
                            selectedCategory = selectedCategory,
                            onYearChange = { transactionViewModel.onYearChange(it) },
                            onMonthChange = { transactionViewModel.onMonthChange(it) },
                            onTipeChange = { transactionViewModel.onTipeChange(it) },
                            onPocketChange = { transactionViewModel.onPocketChange(it) },
                            onCategoryChange = { transactionViewModel.onCategoryChange(it) },
                            onDateClick = { transactionViewModel.onShowDatePicker() },
                            onResetClick = {transactionViewModel.resetFilter()},
                            onDismissClick = {transactionViewModel.onFilterDismiss()},
                            dateString = dateString
                        )
                    }
                }
            }
            if (showDatePicker) {
                DateRangePickerDialog (
                    onDateRangeSelected = { start, end ->
                        transactionViewModel.setStartDate(start)
                        transactionViewModel.setEndDate(end)
                        transactionViewModel.updateDateString()
                        transactionViewModel.onDismissDatePicker()
                    },
                    onDismiss = { transactionViewModel.onDismissDatePicker() }
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .align(Alignment.BottomStart),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(1.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(getPocketBrush("Olive")),
                    contentAlignment = Alignment.CenterStart  // 👈 centers the Text
                ) {
                    Text(
                        text = formatRupiah(totalNominal),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
            ExtendedFloatingActionButton(
                onClick = { onEditTransactionClick(-1) },
                modifier = Modifier.align(Alignment.Companion.BottomEnd),
                // .background(brush = getPocketBrush("Top Bar Color"), shape = RoundedCornerShape(16.dp)),
                icon = { Icon(Icons.Filled.Add, "Edit") },
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
                    Log.d("LoadTransactionProbs","TransactionFragment id $navigateToInput")
                    onEditTransactionClick(navigateToInput!!)
                    transactionViewModel.onNavigatedToInput()
                }
            }


        }
    }
}