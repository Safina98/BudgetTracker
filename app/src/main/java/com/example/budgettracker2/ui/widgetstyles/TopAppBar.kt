package com.example.budgettracker2.ui.widgetstyles

import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.Color
import com.example.budgettracker2.ui.theme.getPocketBrush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PocketTopAppBar(
    title: String,
    onManageClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onImportClick: () -> Unit = {}
) {
    var expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.background(brush = getPocketBrush("Top Bar Color")),
        actions = {
            IconButton(onClick = { expanded.value = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Manage") },
                    onClick = {
                        onManageClick()
                        expanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Export") },
                    onClick = {
                        onExportClick()
                        expanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Import") },
                    onClick = {
                        onImportClick()
                        expanded.value = false
                    }
                )
            }
        }
    )
}