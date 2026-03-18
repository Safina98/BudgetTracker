package com.example.budgettracker2.ui.widgetstyles

import android.R
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgettracker2.database.model.TabunganHomeScreenModel
import com.example.budgettracker2.database.table.PocketTable
import com.example.budgettracker2.pocketColors
import com.example.budgettracker2.ui.theme.AppTypography
import com.example.budgettracker2.ui.theme.getPocketBrush

import com.example.budgettracker2.ui.theme.values.fonts.dustyRoseEndDark

@Composable
fun PocketHomeScreenItemList(
    tabunganModel: TabunganHomeScreenModel

){
    Card(
        modifier = Modifier.padding(2.dp)
            .width(300.dp)
            .height(100.dp)
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(
                    brush = (getPocketBrush(tabunganModel.pocketTable.color))
                )
                .padding(12.dp)) {
            Text(
                text=tabunganModel.pocketTable.pocketName,
                style = AppTypography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pemasukan tahun ini: "+formatRupiah(tabunganModel.thisYearIncome),
                style = AppTypography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pengeluaran tahun ini: "+formatRupiah(tabunganModel.thisYearOutcome),
                style = AppTypography.bodyLarge
                )
        }
    }
}

@Preview
@Composable
fun PocketHomeScreenPreview(){
    PocketHomeScreenItemList(TabunganHomeScreenModel(PocketTable(),0,0))
}