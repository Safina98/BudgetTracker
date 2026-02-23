package com.example.budgettracker2.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker2.database.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionViewModel @Inject constructor( private val repository: BudgetRepository):
    ViewModel() {

    private val _date= MutableStateFlow<Date>(Date())
    val date:StateFlow<Date> = _date
    private val _tipe= MutableStateFlow<String>("")
    val tipe:StateFlow<String> = _tipe
    private val _namaKategori= MutableStateFlow<String>("")
    val namaKategori:StateFlow<String> = _namaKategori

    private val _namaTabungan= MutableStateFlow<String>("")
    val namaTabungan:StateFlow<String> = _namaTabungan

    private val _note= MutableStateFlow<String>("")
    val note:StateFlow<String> = _note

    private val _jumlah= MutableStateFlow<Int>(0)
    val jumlah:StateFlow<Int> = _jumlah

    private val _showDatePickerDialog= MutableStateFlow<Boolean>(false)
    val showDatePickerDialog:StateFlow<Boolean> = _showDatePickerDialog


    private val _navigateToHomeScreen= MutableStateFlow<Boolean>(false)
    val navigateToHomeScreen:StateFlow<Boolean> = _navigateToHomeScreen

    private val _navigateToTransaction= MutableStateFlow<Int?>(null)
    val navigateToTransaction:StateFlow<Int?> = _navigateToTransaction


    val categoryNames: StateFlow<List<String>> = tipe
        .flatMapLatest { currentTipe ->
            repository.getCategoryNamebyTipe(currentTipe)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val pocketList: StateFlow<List<String>> = repository.getAllPocketName().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    fun onTipeSpinnerChange(newTipe:String){
        _tipe.value=newTipe
    }
    fun onKategoriSpinnerChange(newKategori:String){
        _namaKategori.value=newKategori
    }
    fun onTabunganSpinnerChange(newTabungan:String){
        _namaTabungan.value=newTabungan
    }
    fun onNoteChange(newNote:String){
        _note.value=newNote
    }
    fun onJumlahChange(newJumlah:String){
        _jumlah.value=newJumlah.toInt()
    }
    fun onDateChange(newDate:Date){
        _date.value=newDate


    }
    fun onSaveClick(){

    }

    fun onShowDatePicker(){
        _showDatePickerDialog.value=true
    }
    fun onDismissDatePicker(){
        _showDatePickerDialog.value=false
    }

    fun onNavigateToHomeScreen(){
        _navigateToHomeScreen.value=true
    }
    fun onNavigatedtoHomeScreen(){
        _navigateToHomeScreen.value=false
    }
    fun onNavigateToTransaction(id:Int){
        _navigateToTransaction.value=id
    }
    fun onNavigatedToTransaction() {
        _navigateToTransaction.value = null
    }






}