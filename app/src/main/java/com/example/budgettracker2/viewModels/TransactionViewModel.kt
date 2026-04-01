package com.example.budgettracker2.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker2.DateFormatter
import com.example.budgettracker2.TIPETRANSAKSI
import com.example.budgettracker2.database.table.TransactionTable
import com.example.budgettracker2.database.TransaksiModel
import com.example.budgettracker2.database.model.FilterParams
import com.example.budgettracker2.database.model.NewKategoriModel
import com.example.budgettracker2.database.model.TabunganHomeScreenModel
import com.example.budgettracker2.database.repository.BudgetRepository
import com.google.gson.internal.bind.DefaultDateTypeAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
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
    val thisYearPocketSum: StateFlow<List<TabunganHomeScreenModel>> = repository.getThisYearPocketSum().stateIn(
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


    /****-FILTER-********-FILTER-********-FILTER-********-FILTER-********-FILTER-********-FILTER-****/
    private val _selectedTipe = MutableStateFlow<String>("Semua")
    val selectedTipe: StateFlow<String> = _selectedTipe
    private val _selectedPocket = MutableStateFlow<String>("Semua")
    val selectedPocket: StateFlow<String> = _selectedPocket
    private val _selectedCategory = MutableStateFlow<String>("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory
    private val _startDate = MutableStateFlow<Date?>(null)
    val startDate: StateFlow<Date?> = _startDate
    private val _endDate = MutableStateFlow<Date?>(null)
    val endDate: StateFlow<Date?> = _endDate
    private val _monthOnly= MutableStateFlow<Int?>(null)
    val monthOnly: StateFlow<Int?> = _monthOnly
    private val _dateString = MutableStateFlow<String?>(null)
    val dateString: StateFlow<String?> = _dateString
    private val _selectedYear = MutableStateFlow<String>("Semua")
    val selectedYear: StateFlow<String> = _selectedYear
    private val _selectedMonth = MutableStateFlow<String>("Semua")
    val selectedMonth: StateFlow<String> = _selectedMonth
    var _showFilter =MutableStateFlow(false)
    val showFilter:StateFlow<Boolean> = _showFilter
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery
    val pocktetListFilter: StateFlow<List<String>> = repository.getPocketNameListFilter().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val categoryListFilter: StateFlow<List<String>> = _selectedTipe
        .flatMapLatest { tipe ->
            repository.getCategoryNamebyTipe(tipe)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val filteredTransactions = combine(
        combine(_selectedTipe, _selectedPocket, _selectedCategory) { tipe, pocket, category ->
            Triple(tipe, pocket, category)
        },
        combine(_startDate, _endDate, _searchQuery) { startDate, endDate, searchQuery ->
            Triple(startDate, endDate, searchQuery)
        },
        _monthOnly
    ) { (tipe, pocket, category), (startDate, endDate, searchQuery), monthOnly ->
        val pocketId = if (pocket == "Semua") null else repository.getPocketIdByName(pocket)
        val categoryId = if (category == "Semua") null else repository.getCategoryIdByName(category)
        FilterParams(tipe, pocketId, categoryId, startDate, endDate, searchQuery, monthOnly)
    }.flatMapLatest { params ->
        repository.getFilteredTransactions(
            tipe = if (params.tipe == "Semua") null else params.tipe,
            pocketId = params.pocketId,
            categoryId = params.categoryId,
            startDate = params.startDate,
            endDate = params.endDate,
            searchQuery = params.searchQuery,
            monthOnly = params.monthOnly
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val totalNominal = filteredTransactions
        .map { transactions ->
            val pemasukan = transactions
                .filter { it.tipe == TIPETRANSAKSI.masuk }
                .sumOf { it.nominal }
            val pengeluaran = transactions
                .filter { it.tipe == TIPETRANSAKSI.keluar }
                .sumOf { it.nominal }
            pemasukan - pengeluaran
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onTipeChange(newTipe: String) {
        _selectedTipe.value = newTipe
    }
    fun onPocketChange(newPocket: String) {
        _selectedPocket.value = newPocket
    }
    fun onCategoryChange(newCategory: String) {
        _selectedCategory.value = newCategory
    }
    fun onYearChange(newYear: String) {
        _selectedYear.value = newYear
        updateDateRangeFromYearMonth()
    }
    fun onMonthChange(newMonth: String) {
        _selectedMonth.value = newMonth
        updateDateRangeFromYearMonth()
    }
    fun onDateRangeChange(newDate: Date?) {
        _startDate.value = newDate
    }
    fun onFilterClick() {
        _showFilter.value = !_showFilter.value
    }
    fun onFilterDismiss() {
        _showFilter.value = false
    }
    fun setStartDate(newDate: Date?) {
        _startDate.value = newDate
    }
    fun setEndDate(newDate: Date?) {
        _endDate.value = newDate
    }
    fun updateDateString(){
        _dateString.value = DateFormatter.format(startDate.value) + " - " + DateFormatter.format(endDate.value)

    }
    private fun updateDateRangeFromYearMonth() {
        val year = _selectedYear.value
        val month = _selectedMonth.value

        if (year == "Semua" && month == "Semua") {
            _startDate.value = null
            _endDate.value = null
            _monthOnly.value = null
            return
        }

        if (year == "Semua" && month != "Semua") {
            // No date range — use monthOnly flag instead
            _startDate.value = null
            _endDate.value = null
            _monthOnly.value = monthNameToIndex(month)
            return
        }

        // Year is set — proceed with date range as before
        _monthOnly.value = null
        val resolvedYear = year.toInt()
        val resolvedMonth = if (month == "Semua") null else monthNameToIndex(month)
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, resolvedYear)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (resolvedMonth != null) {
            calendar.set(Calendar.MONTH, resolvedMonth)
            _startDate.value = calendar.time
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            _endDate.value = calendar.time
        } else {
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            _startDate.value = calendar.time
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            calendar.set(Calendar.DAY_OF_MONTH, 31)
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            _endDate.value = calendar.time
        }
    }
    private fun monthNameToIndex(month: String): Int {
        return when (month) {
            "Januari" -> Calendar.JANUARY
            "Februari" -> Calendar.FEBRUARY
            "Maret" -> Calendar.MARCH
            "April" -> Calendar.APRIL
            "Mei" -> Calendar.MAY
            "Juni" -> Calendar.JUNE
            "Juli" -> Calendar.JULY
            "Agustus" -> Calendar.AUGUST
            "September" -> Calendar.SEPTEMBER
            "Oktober" -> Calendar.OCTOBER
            "November" -> Calendar.NOVEMBER
            "Desember" -> Calendar.DECEMBER
            else -> Calendar.JANUARY
        }
    }
    fun resetFilter(){
        _selectedTipe.value = "Semua"
        _selectedPocket.value = "Semua"
        _selectedCategory.value = "Semua"
        _selectedYear.value = "Semua"
        _selectedMonth.value = "Semua"
        _startDate.value = null
        _endDate.value=null
        _dateString.value=null
    }

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
            Log.d("LoadTransactionProbs","Viewmodel transactionId: ${_transactionId.value}")
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
                    Log.d("LoadTransactionProbs","View model Load transaction Succsess")
                    Log.d("LoadTransactionProbs","Viewmodel transactionId: ${_transactionId.value}")
                    Log.d("LoadTransactionProbs","Viewmodel nama kategori: ${_namaKategori.value}")
                    Log.d("LoadTransactionProbs","Viewmodel transaksi: ${transaksi}")
                }
                .onFailure {
                    _errorMessage.value = it.message
                    Log.d("LoadTransactionProbs","View model Load transaction Failed")
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
                    onNavigatedtoHomeScreen()
                //onNavigateToTransaction(_transactionId.value!!)
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
        _transactionId.value=null
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