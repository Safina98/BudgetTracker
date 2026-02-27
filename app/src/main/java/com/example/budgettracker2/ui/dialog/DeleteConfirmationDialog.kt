package com.example.budgettracker2.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DeleteConfirmationDialog(
    itemName: String = "",
    isChecked: Boolean=false,
    showCheckbox:Boolean=true,
    onCheckChange: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Delete Item")
            },
            text = {
                Column {
                    Text(
                        text = if (itemName.isNotEmpty())
                            "Are you sure you want to delete \"$itemName\"?"
                        else
                            "Are you sure you want to delete this item?"
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    if (showCheckbox){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCheckChange(!isChecked) },
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange =onCheckChange
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Also delete related transactions")
                        }
                    }

                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
