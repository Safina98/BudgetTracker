package com.example.budgettracker2.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker2.database.KategoriModel
import com.example.budgettracker2.database.PocketTable
import com.example.budgettracker2.database.model.TabunganModel
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

    val _showDialog = MutableStateFlow<Boolean>(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    val namaKategori=  MutableStateFlow<String>("")
    val tipeKategori=  MutableStateFlow<String>("")
    val warnaKategori=  MutableStateFlow<String>("")

    private val _errorMessage = MutableStateFlow<String?>(null)

    val allPockets: StateFlow<List<TabunganModel>> = repository.getAllPocket()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Grace period for configuration changes
            initialValue = emptyList()
        )
    val allCategory: StateFlow<List<KategoriModel>> = repository.getAllCategory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Grace period for configuration changes
            initialValue = emptyList()
        )

    fun onShowDialog(){
        _showDialog.value = true
    }

    fun clearMutbale(){
        _showDialog.value=false
        namaTabungan.value=""
        saldo.value=0
        namaKategori.value=""
        tipeKategori.value=""
        warnaKategori.value=""
    }

    fun insertTabungan(){
        viewModelScope.launch {
            repository.insertPocketWithInitialBalance(namaTabungan.value.uppercase().trim(), saldo.value)
                .onSuccess {
                    //
                    clearMutbale()
                }
                .onFailure { exception ->
                    // Logic for error (e.g., show "Database Error: ${exception.message}")
                    _errorMessage.value = exception.localizedMessage
                }
        }
    }

    fun insertKategori(){
        viewModelScope.launch {
            repository.insertKategori(namaKategori.value.uppercase().trim(), tipeKategori.value, warnaKategori.value)
                .onSuccess {
                    //
                    clearMutbale()
                }.onFailure { exception ->
                    // Logic for error (e.g., show "Database Error: ${exception.message}")
                    _errorMessage.value = exception.localizedMessage
                    Log.i("Kategori", "${_errorMessage.value}")
                }
        }
    }
}