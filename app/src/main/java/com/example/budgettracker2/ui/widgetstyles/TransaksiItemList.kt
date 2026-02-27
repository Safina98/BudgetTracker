package com.example.budgettracker2.ui.widgetstyles

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.budgettracker2.DateTypeConverter
import com.example.budgettracker2.database.TransaksiModel
import com.example.budgettracker2.ui.theme.Typography
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionItemList(
    transaksi: TransaksiModel,
    onDeleteClicked: () -> Unit,
    onUpdateClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = beige)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.spacedBy((-8).dp), // Negative spacing pulls them together
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onUpdateClicked,
                    modifier = Modifier.size(40.dp) // Shrinks the container from 48dp to 40dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(25.dp) // Scales the icon appropriately
                    )
                }

                IconButton(
                    onClick = onDeleteClicked,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Side: Category, Date, and Note
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = transaksi.category_name_model_ ?: "",
                        style = MaterialTheme.typography.titleMedium, // Replace with your big_text_style
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = DateTypeConverter.fromDate(transaksi.date) ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = transaksi.ket ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Right Side: Nominal (Formatted as Rupiah)
                Text(
                    text = formatRupiah(transaksi.nominal),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

// Helper function for the "app:rupiahFormat" logic
fun formatRupiah(amount: Int?): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }
    return formatter.format(amount ?: 0L)
}