package com.example.budgettracker2.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker2.database.TransactionTable
import com.example.budgettracker2.database.TransaksiModel
import com.example.budgettracker2.database.model.NewKategoriModel
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

    val thisYearCategorySum: StateFlow<List<NewKategoriModel>> = repository.getThisYearCategorySum()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Grace period for configuration changes
            initialValue = emptyList()
        )

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

    private val _showDeleteDialog = MutableStateFlow<Int?>(null)
    val showDeleteDialog: StateFlow<Int?> = _showDeleteDialog

    private val _navigateToHomeScreen= MutableStateFlow<Boolean>(false)
    val navigateToHomeScreen:StateFlow<Boolean> = _navigateToHomeScreen

    private val _navigateToTransaction= MutableStateFlow<Int?>(null)
    val navigateToTransaction:StateFlow<Int?> = _navigateToTransaction

    private val _navigateToInput= MutableStateFlow<Int?>(null)
    val navigateToInput:StateFlow<Int?> = _navigateToInput

    val transactionList: StateFlow<List<TransaksiModel>> = repository.getAllTransaction().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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

    fun setTransactionId(id:Int){
        if (id!=-1){
            _transactionId.value=id
            loadTransaction(id)
        }else
        {
            _transactionId.value=null
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
    fun onEditTransactionCLick(transaction: TransaksiModel){
        onNavigateToInput(transaction.id)
    }
    fun onDeleteClick(id:Int){
        _showDeleteDialog.value = id
    }
    fun onDeleteDialogDismiss(){
        _showDeleteDialog.value = null
    }
    fun loadTransaction(id: Int) {
        viewModelScope.launch {
            repository.getTransactionById(id)
                .onSuccess { transaksi ->
                    if (transaksi != null) {
                        _transactionId.value=id
                        _namaKategori.value = transaksi.category_name_model_ ?: ""
                        _namaTabungan.value = transaksi.pocketName ?: ""
                        _tipe.value = transaksi.tipe ?: ""
                        _note.value = transaksi.ket ?: ""
                        _jumlah.value = transaksi.nominal
                        _date.value = transaksi.date
                    } else {
                        _errorMessage.value = "Transaction not found"
                    }
                }
                .onFailure {
                    _errorMessage.value = it.message
                }
        }
    }
    fun insertTransaction() {
        viewModelScope.launch {
            val transaction = TransactionTable().apply {
                date = _date.value
                tipe = _tipe.value
                note = _note.value
                nominal = _jumlah.value?:0
            }
            val result = repository.upsertTransaction(
                _transactionId.value,
                transaction,
                _namaKategori.value,
                _namaTabungan.value
            )
            result.onSuccess {
                if (_transactionId.value==null){
                    onNavigatedtoHomeScreen()
                }else{
                    onNavigateToTransaction(_transactionId.value!!)
                }
                resetMutable()

            }.onFailure { e ->
                _errorMessage.value = "Insert failed. "+e.localizedMessage
            }
        }
    }
    fun deleteTransaction(){
        viewModelScope.launch {
            repository.deleteTransaction(_showDeleteDialog.value!!)
                .onSuccess {
                    resetMutable()
                    onDeleteDialogDismiss()
                }
                .onFailure {e->
                    _errorMessage.value = "Delete failed. "+e.localizedMessage
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
    fun onNavigateToInput(id:Int){
        _navigateToInput.value=id
    }
    fun onNavigatedToInput(){
        _navigateToInput.value=null
    }
}