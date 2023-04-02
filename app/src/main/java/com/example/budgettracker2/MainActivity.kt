package com.example.budgettracker2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
enum class tipe{PEMASUKAN, PENGELUARAN, TABUNGAN}
enum class warna{PINK,YELLOW_PINK,BLUE,GREEN,PURPLE,YELLOW}
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}