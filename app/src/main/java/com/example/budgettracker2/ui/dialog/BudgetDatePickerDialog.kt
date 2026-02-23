package com.example.budgettracker2.ui.dialog

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    initialDate: java.util.Date, // Takes your Entity's Date object
    onDateSelected: (java.util.Date) -> Unit, // Returns a Date object
    onDismiss: () -> Unit
) {
    // 1. Convert your Date object to Millis for the DatePicker state
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.time
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // 2. Convert the Long (millis) back into a java.util.Date
                datePickerState.selectedDateMillis?.let { millis ->
                    onDateSelected(java.util.Date(millis))
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}