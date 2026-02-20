package com.example.budgettracker2.FragmentManage

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
import com.example.budgettracker2.ui.widgetstyles.PrimaryButtonStyle
import com.example.budgettracker2.R
@Composable
fun ManageScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButtonStyle(
            "Manage Pocket",
            onClick = {
                navController.navigate(R.id.action_manageFragment_to_fragmentTabungan)
            },
            modifier = Modifier.fillMaxWidth()
        )
        PrimaryButtonStyle(
            "Manage Kategori",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
        PrimaryButtonStyle(
            "Manage Data",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

