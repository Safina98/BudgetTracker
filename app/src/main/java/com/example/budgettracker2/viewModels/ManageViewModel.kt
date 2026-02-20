package com.example.budgettracker2.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker2.database.PocketTable
import com.example.budgettracker2.database.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageViewModel @Inject constructor( private val repository: BudgetRepository) :
    ViewModel() {

    val namaTabungan=  MutableStateFlow<String>("")
    val saldo=MutableStateFlow<Int>(0)
    val allPockets: StateFlow<List<PocketTable>> = repository.getAllPocket()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Grace period for configuration changes
            initialValue = emptyList()
        )

    fun insertTabungan(){
        viewModelScope.launch {
            val tabungan = PocketTable()
            tabungan.pocketName = namaTabungan.value
            tabungan.saldo = saldo.value
            repository.insertPocket(tabungan)
            Log.i("MainViewModel","InsertTabungan")
        }
    }
}