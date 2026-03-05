package com.example.budgettracker2.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgettracker2.R
import com.example.budgettracker2.ui.widgetstyles.PrimaryButtonStyle

@Composable
fun ManageScreen(
    onNavigateToTabungan: () -> Unit,
    onNavigateToKategori: () -> Unit,
){
    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        PrimaryButtonStyle(
            "Manage Pocket",
            onClick = onNavigateToKategori,
            modifier = Modifier.Companion.fillMaxWidth()
        )
        PrimaryButtonStyle(
            "Manage Kategori",
            onClick = onNavigateToTabungan,
            modifier = Modifier.Companion.fillMaxWidth()
        )
        PrimaryButtonStyle(
            "Manage Data",
            onClick = {},
            modifier = Modifier.Companion.fillMaxWidth()
        )
    }
}