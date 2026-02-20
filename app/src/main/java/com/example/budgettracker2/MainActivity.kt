package com.example.budgettracker2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint

enum class tipe{PEMASUKAN, PENGELUARAN, TABUNGAN}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}