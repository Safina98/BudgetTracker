package com.example.budgettracker2.database

import java.util.*

data class TransaksiModel(
    var id:Int=0,
    var cath_name_m:String="",
    var ket:String ="",
    var date: String="",
    var nominal:Int=0
)