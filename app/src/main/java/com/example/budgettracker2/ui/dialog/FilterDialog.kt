package com.example.budgettracker2.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgettracker2.monthList
import com.example.budgettracker2.tipeListFilter
import com.example.budgettracker2.ui.theme.AppTypography
import com.example.budgettracker2.ui.widgetstyles.BudgetSpinner
import com.example.budgettracker2.ui.widgetstyles.PrimaryButtonStyle
import com.example.budgettracker2.yearList

@Composable
fun FilterDialog (
    pocketList: List<String>,
    categoryList: List<String>,
    selectedYear: String,
    selectedMonth: String,
    selectedTipe: String,
    selectedPocket: String,
    selectedCategory: String,
    onYearChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onTipeChange: (String) -> Unit,
    onPocketChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onResetClick: () -> Unit,
    onDismissClick: () -> Unit,
    dateString: String?=null
){
    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.Transparent) // 👈 move here
    ) {
        BudgetSpinner(
            title = "Pilih Tipe",
            options = tipeListFilter,
            selectedOption = selectedTipe,
            onOptionSelected = onTipeChange,
            modifier = Modifier
        )
        BudgetSpinner(
            title = "Pilih Tabungan",
            options = pocketList,
            selectedOption = selectedPocket,
            onOptionSelected = onPocketChange,
            modifier = Modifier
        )
        BudgetSpinner(
            title = "Pilih Kategori",
            options = categoryList,
            selectedOption = selectedCategory,
            onOptionSelected = onCategoryChange,
            modifier = Modifier
        )
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BudgetSpinner(
                title = "Pilih Tahun",
                options = yearList,
                selectedOption = selectedYear,
                onOptionSelected = onYearChange,
                modifier = Modifier.weight(1f),
            )
            BudgetSpinner(
                title = "Pilih Bulan",
                options = monthList,
                selectedOption = selectedMonth,
                onOptionSelected = onMonthChange,
                modifier = Modifier.weight(1f),
            )
        }

        Text(
            text=dateString?: "Pilih Tanggal",
            style = AppTypography.titleMedium,
            color = Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp,vertical = 2.dp)
                .clickable {
                    onDateClick()
                }
        )
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            PrimaryButtonStyle(
                text="Reset",
                onClick = {onResetClick()},
                modifier = Modifier
                    .weight(1f)
            )
            PrimaryButtonStyle(
                text="Dissmiss",
                onClick = onDismissClick,
                modifier = Modifier
                    .weight(1f)
            )

        }
    }
}
@Composable
@Preview
fun FilterDialogPreview(){
    FilterDialog(
        pocketList = listOf("Wallet", "Savings", "Checking"),
        categoryList = listOf("Food", "Transport", "Utilities", "Entertainment"),
        selectedYear = "Semua",
        selectedMonth = "Mei",
        selectedTipe = "Expense",
        selectedPocket = "Wallet",
        selectedCategory = "Food",
        onYearChange = {},
        onMonthChange = {},
        onTipeChange = {},
        onPocketChange = {},
        onCategoryChange = {},
        onDateClick = {},
        onResetClick = {},
        onDismissClick = {},
        dateString = null,
    )
}