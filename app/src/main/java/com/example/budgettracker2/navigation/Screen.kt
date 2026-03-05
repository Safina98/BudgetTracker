package com.example.budgettracker2.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Input : Screen("input/{id}") {
        fun createRoute(id: Int) = "input/$id"
    }
    object Kategori : Screen("kategori")
    object Manage : Screen("manage")
    object Tabungan : Screen("tabungan")
    object Transaction : Screen("transaction/{id}"){
        fun createRoute(id: Int) = "transaction/$id"
    }

}