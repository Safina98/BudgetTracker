package com.example.budgettracker2.ui.widgetstyles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.budgettracker2.database.model.NewKategoriModel
import com.example.budgettracker2.pocketColors
import com.example.budgettracker2.ui.theme.AppTypography
import com.example.budgettracker2.ui.theme.getPocketBrush


@Composable
fun KategoriGridItemList(item: NewKategoriModel){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(2.dp)
            .background(Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getPocketBrush(item.categoryTable.category_color)),


        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp) ,// Add padding so text doesn't touch the edges
                        horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.categoryTable.category_name,
                    style = AppTypography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text=formatRupiah(item.categoryCashSum),
                    style = AppTypography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()

                )
            }
        }
    }
}