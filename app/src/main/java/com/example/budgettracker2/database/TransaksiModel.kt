package com.example.budgettracker2.database

import java.util.*

data class TransaksiModel(
    var id:Int=0,
    var category_name_model_:String="",
    var pocketName:String?=null,
    var tipe:String?=null,
    var ket:String ="",
    var date: Date=Date(),
    var nominal:Int=0
)