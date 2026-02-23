package com.example.budgettracker2.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker2.database.TransactionTable
import com.example.budgettracker2.database.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.onSuccess

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

    private val _jumlah= MutableStateFlow<Int?>(null)
    val jumlah:StateFlow<Int?> = _jumlah

    private val _transactionId= MutableStateFlow<Int?>(null)
    val transId:StateFlow<Int?> = _transactionId


    private val _showDatePickerDialog= MutableStateFlow<Boolean>(false)
    val showDatePickerDialog:StateFlow<Boolean> = _showDatePickerDialog

    val _errorMessage= MutableStateFlow<String?>(null)
    val errorMessage:StateFlow<String?> = _errorMessage


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

    fun setTransactionId(id:Int?){
        if (id!=-1){
            _transactionId.value=id
        }
    }
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
        _jumlah.value=newJumlah.toIntOrNull()
    }
    fun onDateChange(newDate:Date){
        _date.value=newDate
    }

    fun onShowDatePicker(){
        _showDatePickerDialog.value=true
    }
    fun onDismissDatePicker(){
        _showDatePickerDialog.value=false
    }
    fun onErrorDissmiss(){
        _errorMessage.value=null
    }

    fun insertTransaction() {
        viewModelScope.launch {
            val transaction = TransactionTable().apply {
                date = _date.value
                tipe = _tipe.value
                note = _note.value
                nominal = _jumlah.value?:0
            }
            // Capture the Result returned from the repository
            val result = repository.upsertTransaction(
                _transactionId.value,
                transaction,
                _namaKategori.value,
                _namaTabungan.value
            )
            result.onSuccess {
                resetMutable()
                onNavigatedtoHomeScreen()
            }.onFailure { e ->
                _errorMessage.value = "Insert failed. "+e.localizedMessage
            }
        }
    }
    fun resetMutable(){
        _date.value=Date()
        _tipe.value=""
        _note.value=""
        _jumlah.value=0
        _namaKategori.value=""
        _namaTabungan.value=""
        _transactionId.value=-1
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
        _navigateToTransaction.value = -1
    }
}