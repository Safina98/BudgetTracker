package com.example.budgettracker2.ui.widgetstyles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgettracker2.database.model.TabunganHomeScreenModel
import com.example.budgettracker2.database.model.TabunganModel
import com.example.budgettracker2.database.table.PocketTable

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
                .background(brush = Brush.linearGradient(
                    0.0f to Color(0xFFc496a1),          // Start color
                    0.6f to Color(0x85c496a1),          // Center color at 60%
                    1.0f to Color(0xFFc496a1),          // End color (85 alpha)
                    start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
                    end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
                ))
                .padding(8.dp)) {
            Text(tabunganModel.pocketTable.pocketName)
            Text("Pemasukan tahun ini: "+formatRupiah(tabunganModel.thisYearIncome))
            Text("Pengeluaran tahun ini: "+formatRupiah(tabunganModel.thisYearOutcome))

        }
    }
}
@Preview
@Composable
fun PocketHomeScreenPreview(){
    PocketHomeScreenItemList(TabunganHomeScreenModel(PocketTable(),0,0))
}