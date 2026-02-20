package com.example.budgettracker2.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgettracker2.TIPETRANSAKSI
import com.example.budgettracker2.database.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class MainViewModel(application: Application,
                    val datasource1:CategoryDao,
                    val datasource2:TransactionDao,
                    val pocketDao: PocketDao
                    ):AndroidViewModel (application){
/*************************************************************************************************************/
/****************************************Variables***********************************************************/
/************************************************************************************************************/

/************************************************Navigation**************************************************/
    private var _navigate_to_transaction = MutableLiveData<Int>()
    val navigate_to_transaction :LiveData<Int>get() = _navigate_to_transaction

    private var _navigate_to_input = MutableLiveData<Int>()
    val navigate_to_input :LiveData<Int>get() = _navigate_to_input

    private var _trans_id = MutableLiveData<Int>()
    val trans_id:LiveData<Int>get() = _trans_id

    private var _navigate_to_homeScreen = MutableLiveData<Boolean>()
    val navigate_to_toHomeScreen :LiveData<Boolean>get() = _navigate_to_homeScreen

/*************************************************Transaction***********************************************/
    //Category id
    val _receivedCatId = MutableLiveData<Int>()
    //Spinner entries
    val tipe_entries = listOf<String>("ALL","PEMASUKAN","PENGELUARAN")
    private val _selectedTipeSpinner = MutableLiveData<String>()
    val selectedTipeSpinner: LiveData<String> get() = _selectedTipeSpinner

    var _kategori_entries = MutableLiveData<List<String>>()
    val kategori_entries :LiveData<List<String>> get() = _kategori_entries

    private val _selectedKategoriSpinner = MutableLiveData<String>()
    val selectedKategoriSpinner: LiveData<String> get() = _selectedKategoriSpinner

    var _pocketEntries = MutableLiveData<List<String>>()
    val pocketEntries :LiveData<List<String>> get() = _pocketEntries

    private val _selectedPocketSpinner = MutableLiveData<String>()
    val selectedPocketSpinner: LiveData<String> get() = _selectedPocketSpinner

    private val _selectedStartDate = MutableLiveData<Date>()
    val selectedStartDate: LiveData<Date> get() = _selectedStartDate

    private val _selectedEndDate = MutableLiveData<Date>()
    val selectedEndDate: LiveData<Date> get() = _selectedEndDate

    private var _is_date_range = MutableLiveData<Boolean>()
    val is_date_range :LiveData<Boolean>get() = _is_date_range

    private var _filter_trans_sum = MutableLiveData<Int>()
    val filter_trans_sum :LiveData<Int>get() = _filter_trans_sum

    //val bulan = listOf<String>("ALL","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","Oktober","November","Desember")
    val bulan = listOf<String>("ALL","01","02","03","04","05","06","07","08","09","10","11","12","Date Range")
    private val _selectedBulanSpinner = MutableLiveData<String>()
    val selectedBulanSpinner: LiveData<String> get() = _selectedBulanSpinner

    private val _recyclerViewData = MutableLiveData<List<TransaksiModel>>()
    val recyclerViewData: LiveData<List<TransaksiModel>>
        get() = _recyclerViewData
    private val _unFilteredrecyclerViewData = MutableLiveData<List<TransaksiModel>>()

    val semuatabeltransaksi = datasource2.getAllTransactionTableCoba()
    private val _filteredDataSum = MutableLiveData<Int>()
    val filteredDataSum: LiveData<Int>
        get() = _filteredDataSum

    private val _categoryLoadedEvent = MutableLiveData<Boolean>()
    val categoryLoadedEvent: LiveData<Boolean> get() = _categoryLoadedEvent
/************************************************Input****************************************************/
    //Spinner Position
    //Spinner entries
    //field
    val jumlah = MutableLiveData<String>("0")

    val note = MutableLiveData<String>("")
    //Date Picker
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate
    private var _is_date_picker_clicked = MutableLiveData<Boolean>(false)
    val is_date_picker_clicked :LiveData<Boolean>get() = _is_date_picker_clicked
    /*************************************Update&Delete*******************************************************/
    private var _clicked_transtab = MutableLiveData<TransactionTable>()
    val clicked_transtab: LiveData<TransactionTable> get() = _clicked_transtab
    val _clicked_category = MutableLiveData<CategoryTable>()
    val clicked_category: LiveData<CategoryTable> get() = _clicked_category

    /***************************************Tabungan*****************************************/
    val namaTabungan=  MutableStateFlow<String>("")
    val saldo=MutableStateFlow<Int>(0)


    init {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        _selectedDate.value = currentDate
        _selectedBulanSpinner.value  = "THIS YEAR"
        _selectedKategoriSpinner.value= "ALL"
        _selectedPocketSpinner.value="Main"
        _clicked_category.observeForever {
            _categoryLoadedEvent.value = true
        }
    }

 /************************************************************************************************************/
 /*********************************************FUNTIONS*******************************************************/
 /***********************************************************************************************************/
 // DatePicker Input Fragment
 //DatePicker
     fun setSelectedDate(year: Int, month: Int, day: Int) {
     val selectedDate = "${year}-${(month+1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
         _selectedDate.value = selectedDate
     }
    fun onDatePickerClick(){ _is_date_picker_clicked.value = true }
    fun onDatePickerClicked(){ _is_date_picker_clicked.value = false }
/******************************************************CRUD***********************************************/

    private suspend fun getCategoryId(category: String) :Int{
       var a =  withContext(Dispatchers.IO) {
            datasource1.getCategoryIdByName(category)
        }
        return a
    }
    private suspend fun getPocketId(pocket: String) :Int{
       return withContext(Dispatchers.IO) {
            pocketDao.getIdByPocketName(pocket)
        }
    }
    fun getNominal(value: String?):Int{
       return if(value== TIPETRANSAKSI.keluar){
           jumlah.value?.toInt()!!*-1

       }else{
           jumlah.value!!.toInt()
       }
    }
    fun updateJumlah(value: String?) {
        // Update the LiveData without Rupiah formatting
        jumlah.value = value!!
    }
    fun getDate():Date{
        val dateString = selectedDate.value
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.parse(dateString)
    }
    // Method to update the selected value
    fun setSelectedPocketValue(value: String) {
        _selectedPocketSpinner.value = value
    }
    fun setSelectedKategoriValue(value: String) {
        _selectedKategoriSpinner.value = value
    }
    fun setSelectedTipeValue(value: String) {
        _selectedTipeSpinner.value = value
    }
    fun setSelectedBulanValue(value: String) {
        _selectedBulanSpinner.value = value
    }
    fun setDateRange(startDate:Date,endDate:Date){
        _selectedStartDate.value = startDate
        _selectedEndDate.value=endDate

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateRv4(){
        Log.i("UpdateRv","update rv called")
        val type = if (selectedTipeSpinner.value == "ALL") null else selectedTipeSpinner.value
        val startDate: String?
        val endDate: String?
        if (selectedBulanSpinner.value != "Date Range") {
            // Extract the month value from the selected date spinner
            val selectedMonth =selectedBulanSpinner.value?.toIntOrNull()
            if (selectedMonth != null) {
                startDate = constructStartDate(selectedMonth)
                endDate = constructEndDate(selectedMonth)
            }
            else {
                if (selectedBulanSpinner.value=="THIS YEAR")
                {
                startDate = constructStartDate(1)
                endDate = constructEndDate(12)}
                else{
                    // Invalid month value, handle the error case
                    startDate = null
                    endDate = null
                }
            }
        } else {
            // Date range option selected, use the selected start and end dates
            startDate = formatDate(_selectedStartDate.value)
            endDate = formatDate(_selectedEndDate.value)
        }
        
        performDataFiltering(type, selectedKategoriSpinner.value!!, startDate, endDate)
    }
    private fun performDataFiltering(type: String?, category: String, startDate: String?, endDate: String?) {
        viewModelScope.launch {
            val categoryId = withContext(Dispatchers.IO) {
                if (category == "ALL") null else datasource1.getCategoryIdByName(category)
            }
            val filteredData = withContext(Dispatchers.IO) {
                datasource2.getFilteredData3(type, categoryId, startDate, endDate)
            }
            val filteredSum =  withContext(Dispatchers.IO) {
                datasource2.getFilteredDataSum(type, categoryId, startDate, endDate)
            }
            _recyclerViewData.value = filteredData
            _unFilteredrecyclerViewData.value = filteredData
            _filter_trans_sum.value = filteredSum
            _filteredDataSum.value = _filter_trans_sum.value
        }
    }
    fun filterData(query: String?) {
        val list = mutableListOf<TransaksiModel>()
        if(!query.isNullOrEmpty()) {
            list.addAll(_unFilteredrecyclerViewData.value!!.filter {
                it.ket.lowercase(Locale.getDefault()).contains(query.toString().lowercase(Locale.getDefault()))})
        } else {
            list.addAll(_unFilteredrecyclerViewData.value!!)
            _filter_trans_sum.value = _filteredDataSum.value
        }
        val filteredData = recyclerViewData.value?.filter { it.ket.contains(query as CharSequence, ignoreCase = true) }
        _filter_trans_sum.value = calculateFilteredDataSum(filteredData)
        _recyclerViewData.value =list
    }
    private fun calculateFilteredDataSum(filteredData: List<TransaksiModel>?): Int {
        return filteredData?.sumBy { it.nominal } ?: 0
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun constructStartDate(month: Int): String? {
        val startDate = YearMonth.of(LocalDate.now().year, month).atDay(1)
        return startDate.format(DateTimeFormatter.ISO_DATE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun constructEndDate(month: Int): String? {
        val endDate = YearMonth.of(LocalDate.now().year, month).atEndOfMonth()
        return endDate.format(DateTimeFormatter.ISO_DATE)
    }
    private fun formatDate(date: Date?): String? {
        if (date != null) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(date)
        }
        return null
    }
    //transaction fragment
    fun getKategoriEntries(value:String){
       // Toast.makeText(getApplication(),value+" kategori",Toast.LENGTH_SHORT).show()
        viewModelScope.launch {
            val newData = withContext(Dispatchers.IO) {

                    if(value== TIPETRANSAKSI.TRANSFER){
                        val list = pocketDao.getAllPocketName()
                        list
                    }
                else {
                     val list =   datasource1.getKategoriNameD(value)
                        val modifiedList = listOf("ALL") + list
                        modifiedList
                }
                 // Create a new list with the added value
                 // Return the modified list
            }
            _kategori_entries.value = newData
        }
    }

    fun getPocketEntries(){
        viewModelScope.launch {
            val newData= withContext(Dispatchers.IO){
                pocketDao.getAllPocketName()
            }
            _pocketEntries.value=newData
        }
    }

    //Input Fragment

    fun saveOrUpdate(){
        if (_trans_id.value!=-1)
        {
            updateTransaction()
        }
        else{
            saveTransaction()
        }
    }
    fun updateTransaction(){
        viewModelScope.launch {
            val transaction = TransactionTable()
            transaction.date = getDate()
            transaction.note=note.value!!
            val kategori:String = selectedKategoriSpinner.value ?: ""
            val pocket:String=selectedPocketSpinner.value ?: "Tabungan Utama"
            transaction.category_id = getCategoryId(kategori)
            transaction.pocket_id=getPocketId(pocket)
            transaction.nominal=getNominal(selectedTipeSpinner.value)
            transaction.transaction_id = clicked_transtab.value!!.transaction_id
            update_trans(transaction)
        }

        onNavigateToTransaction(-1)
    }
    fun saveTransaction(){
        viewModelScope.launch {
            val transaction = TransactionTable()
            val kategori:String = selectedKategoriSpinner.value ?: ""
            val pocket:String=selectedPocketSpinner.value ?: "Tabungan Utama"
            val tipe:String=selectedTipeSpinner.value?:""
            if (tipe==TIPETRANSAKSI.TRANSFER){
                pocketTransfer()
            }else{
                transaction.category_id= getCategoryId(kategori)
                transaction.pocket_id = getPocketId(pocket)
                transaction.date = getDate()
                transaction.note = note.value!!
                transaction.nominal = getNominal(selectedTipeSpinner.value)
                insert(transaction)
            }
        }
        onNavigateToHomeScreen()
    }
    //transfer antar tabungan
    fun pocketTransfer() {
        viewModelScope.launch {
            val transaction = TransactionTable()
            val pocketAsal:String = selectedKategoriSpinner.value ?: ""
            val pocketTujuan:String=selectedPocketSpinner.value ?: ""
            val tipe:String=selectedTipeSpinner.value?:""
            transaction.category_id= getCategoryId("TRANSFER")
            transaction.pocket_id = getPocketId(pocketAsal)
            transaction.date = getDate()
            transaction.note = note.value!!
            transaction.nominal = getNominal(TIPETRANSAKSI.keluar)
            insert(transaction)
            transaction.pocket_id=getPocketId(pocketTujuan)
            transaction.nominal = getNominal(TIPETRANSAKSI.masuk)
            insert(transaction)
        }

    }
    private suspend fun getCategory(id:Int): CategoryTable? {
        return withContext(Dispatchers.IO) {
            datasource1.getCategory(id)
        }
    }
    private suspend fun getPocket(id:Int): PocketTable? {
        return withContext(Dispatchers.IO) {
            pocketDao.getPocketById(id)
        }
    }
    fun getClickedTransTab(id:Int){
        viewModelScope.launch {
            val a =withContext(Dispatchers.IO) {
              datasource2.getTransById(id)
            }
            _clicked_transtab.value = a
            getClickedCategory(clicked_transtab.value!!.category_id!!,clicked_transtab.value!!.pocket_id!!)
        }
    }
    fun getClickedCategory(id:Int,pocketId:Int){
        viewModelScope.launch {
            _clicked_category.value = getCategory(id)
            _selectedPocketSpinner.value = getPocket(pocketId)?.pocketName
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteTransaction(trans_id:Int){
        viewModelScope.launch {
            delete_trans(clicked_transtab.value!!)
            updateRv4()
        }
    }
    fun setValueForUpdate(){
        val t = clicked_transtab.value
        note.value = t?.note ?: ""
        jumlah.value = Math.abs(t!!.nominal!!).toString() ?: "0"
        _selectedDate.value = formatDateToString(t?.date)
    }

    fun formatDateToString(date: Date?): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }
    fun resetValue(){
        note.value = ""
        jumlah.value = "0"
        _selectedDate.value=SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        _selectedKategoriSpinner.value ="ALL"
        _selectedTipeSpinner.value = "PENGELUARAN"
        _clicked_transtab.value = TransactionTable()
        _clicked_category.value=CategoryTable()

    }

    fun setTransId(id:Int){
        _trans_id.value = id
    }
    fun setCatId(id:Int){
        _receivedCatId.value= id
    }
    private suspend fun insert(transaksi: TransactionTable){ withContext(Dispatchers.IO){datasource2.insert(transaksi)} }
    private suspend fun delete_trans(t:TransactionTable){ withContext(Dispatchers.IO){datasource2.delete2(t)} }
    private suspend fun update_trans(t:TransactionTable){ withContext(Dispatchers.IO){datasource2.update(t)} }

    //*******************************************Tabungan************************************//
    fun insertTabungan(){
        viewModelScope.launch {
            val tabungan = PocketTable()
            tabungan.pocketName = namaTabungan.value
            tabungan.saldo = saldo.value
            insertTabungantoDB(tabungan)
            Log.i("MainViewModel","InsertTabungan")
        }
    }
    private suspend fun insertTabungantoDB(pocketTable: PocketTable){
        withContext(Dispatchers.IO){
            pocketDao.insert(pocketTable)
        }
    }

    /********************************************Navigation***********************************************/
    //Navigating to transaction
    fun onClick(){ onNavigateToTransaction(-1) }
    fun onNavigateToTransaction(id:Int){ _navigate_to_transaction.value = id }
    @SuppressLint("NullSafeMutableLiveData")
    fun onNavigatedToTransaction(){
        _navigate_to_transaction.value = null
        }

    //Navigate to input
    fun onNavigateToInput(id: Int){ _navigate_to_input.value = id }
    @SuppressLint("NullSafeMutableLiveData")
    fun onNavigatedToInout(){ _navigate_to_input.value = null }

    //Navigate to HomeScreen
    fun onNavigateToHomeScreen(){ _navigate_to_homeScreen.value = true }
    @SuppressLint("NullSafeMutableLiveData")
    fun onNavigatedToHomeScreen(){_navigate_to_homeScreen.value=null}



    companion object {

        @JvmStatic
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()
                val dataSource = BudgetDB.getInstance(application).category_dao
                val dataSource2 = BudgetDB.getInstance(application).transaction_dao
                val dataSource3 = BudgetDB.getInstance(application).pocket_dao

                return MainViewModel(
                    application,dataSource,dataSource2,dataSource3
                ) as T
            }
        }
    }

}