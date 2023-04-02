package com.example.budgettracker2.database

import com.example.budgettracker2.tipe
import com.example.budgettracker2.warna

data class KategoriModel (
    var id_: Int,
    var category_name_: String,
    var category_type_: String,
    var category_color_:String,
    var sum: Int

        )