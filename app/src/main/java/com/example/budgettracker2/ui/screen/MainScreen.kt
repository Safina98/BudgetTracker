package com.example.budgettracker2.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.budgettracker2.navigation.Screen

@Composable
fun MainScreen(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onManageMenuClick = {
                    navController.navigate(Screen.Manage.route)
                },
                onTransactionClick = {id->
                    navController.navigate(Screen.Transaction.createRoute(id))
                },
                onNavigateToInput = { id ->
                    navController.navigate(Screen.Input.createRoute(id))
                }
            )
        }
        composable(
            route = Screen.Input.route,
            arguments = listOf(navArgument("id") { type = NavType.Companion.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            InputScreen(
                id = id,
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(Screen.Kategori.route) {
            KategoriScreen()
        }
        composable(Screen.Manage.route) {
            ManageScreen(
                onNavigateToKategori = {
                    navController.navigate(Screen.Tabungan.route)
                },
                onNavigateToTabungan = {
                    navController.navigate(Screen.Kategori.route)
                }
            )
        }
        composable(Screen.Tabungan.route) {
            TabunganScreen()
        }

        composable(
            route = Screen.Transaction.route,
            arguments = listOf(navArgument("id") { type = NavType.Companion.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            TransactionScreen(
                id = id,
                onEditTransactionClick = { transId ->
                    navController.navigate(Screen.Input.createRoute(id))
                }
            )
        }


    }
}