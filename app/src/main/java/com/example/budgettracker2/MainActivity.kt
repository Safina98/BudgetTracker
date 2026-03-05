package com.example.budgettracker2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import com.example.budgettracker2.ui.screen.MainScreen
import com.example.budgettracker2.ui.theme.BudgetTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

enum class tipe{PEMASUKAN, PENGELUARAN, TABUNGAN}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetTrackerTheme {
                MainScreen()
            }
        }
    }
}