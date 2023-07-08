package com.example.budgettracker2.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.budgettracker2.tipe
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgettracker2.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainViewModel(application: Application,
                    val datasource1:CategoryDao,
                    val datasource2:TransactionDao
                    ):AndroidViewModel (application){
/*************************************************************************************************************/
/****************************************Variables***********************************************************/
/************************************************************************************************************/

/************************************************Navigation**************************************************/
    private var _navigate_to_transaction = MutableLiveData<Int>()
    val navigate_to_transaction :LiveData<Int>get() = _navigate_to_transaction

    private var _navigate_to_input = MutableLiveData<Boolean>()
    val navigate_to_input :LiveData<Boolean>get() = _navigate_to_input

    private var _navigate_to_homeScreen = MutableLiveData<Boolean>()
    val navigate_to_toHomeScreen :LiveData<Boolean>get() = _navigate_to_homeScreen

/*************************************************Transaction***********************************************/
    //Spinner entries
    val tipe_entries = listOf<String>("ALL","PEMASUKAN","PENGELUARAN")
    private val _selectedTipeSpinner = MutableLiveData<String>()
    val selectedTipeSpinner: LiveData<String> get() = _selectedTipeSpinner

    var _kategori_entries = MutableLiveData<List<String>>()
    val kategori_entries :LiveData<List<String>> get() = _kategori_entries
    private val _selectedKategoriSpinner = MutableLiveData<String>()
    val selectedKategoriSpinner: LiveData<String> get() = _selectedKategoriSpinner

    private val _selectedStartDate = MutableLiveData<Date>()
    val selectedStartDate: LiveData<Date> get() = _selectedStartDate

    private val _selectedEndDate = MutableLiveData<Date>()
    val selectedEndDate: LiveData<Date> get() = _selectedEndDate

    private var _is_date_range = MutableLiveData<Boolean>()
    val is_date_range :LiveData<Boolean>get() = _is_date_range

    //val bulan = listOf<String>("ALL","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","Oktober","November","Desember")
    val bulan = listOf<String>("ALL","01","02","03","04","05","06","07","08","09","10","11","12","Date Range")
    private val _selectedBulanSpinner = MutableLiveData<String>()
    val selectedBulanSpinner: LiveData<String> get() = _selectedBulanSpinner

    private val _recyclerViewData = MutableLiveData<List<TransaksiModel>>()
    val recyclerViewData: LiveData<List<TransaksiModel>>
        get() = _recyclerViewData
    val semuatabeltransaksi = datasource2.getAllTransactionTableCoba()


    val transaction = datasource2.getAllTransactionsWithCategoryName()


    /****************************************************HomeScreen**********************************************/
    val kategori = datasource1.getAllKategori()

    val budget_rn = datasource2.getBuget()
    val tm_spend = datasource2.getSumByCategoryType("PENGELUARAN")
    val tm_income = datasource2.getSumByCategoryType("PEMASUKAN")
    var budget_tmm = datasource2.getBugetTM()

/************************************************Input****************************************************/
    //Spinner Position
    var _tipe_position = MutableLiveData<Int>(0)
    var _kategori_Position = MutableLiveData<Int>()
    //Spinner entries
    val tipe_list = listOf<String>(tipe.PENGELUARAN.toString(),tipe.PEMASUKAN.toString())
    val nama_kategori = datasource1.getAllKategoriName()
    //selected item spinner
    val selected_kategori = MutableLiveData<String>()
    val selected_tipe = MutableLiveData<String>()

    val jumlah = MutableLiveData<String>("0")
    val note = MutableLiveData<String>("")
    val kategoricobe = datasource1.getAllKategoriCoba()

/*
    val nama_kategori get() = _tipe_position.value.let {tipe_p->
        datasource1.getKategoriName(tipe_list[tipe_p!!])
        }
 */
    //Date Picker
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate
    private var _is_date_picker_clicked = MutableLiveData<Boolean>(false)
    val is_date_picker_clicked :LiveData<Boolean>get() = _is_date_picker_clicked

    init {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        _selectedDate.value = currentDate
        _selectedTipeSpinner.value = "ALL"
        _selectedKategoriSpinner.value = "ALL"
        _selectedBulanSpinner.value  = "ALL"
        _selectedKategoriSpinner.value= "ALL"
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

    //Getter
    fun getSelectedCategory():String{
        return kategori.value!![_kategori_Position.value!!].category_name_
    }
    fun getCategory_id():Int{
       return kategori.value!!.filter { it.category_name_ == getSelectedCategory()}.map {
            it.id_ }.first()
    }
    suspend fun getCategoryId(category: String) :Int{
       var a =  withContext(Dispatchers.IO) {
            datasource1.getCategoryIdByName(category)
        }
        return a
    }

    fun getNominal():Int{
       return if(selectedTipeSpinner.value=="PENGELUARAN"){
           jumlah.value?.toInt()!! *-1
       }else{
           jumlah.value!!.toInt()
       }
    }

    fun getDate():Date{
        val dateString = selectedDate.value
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.parse(dateString)
    }
    // Method to update the selected value

    fun setSelectedKategoriValue(value: String) {
        _selectedKategoriSpinner.value = value
        viewModelScope.launch { getCategoryId(selectedKategoriSpinner.value ?: value) }

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
        val type = if (selectedTipeSpinner.value == "ALL") null else selectedTipeSpinner.value
        val startDate: String?
        val endDate: String?
        if (selectedBulanSpinner.value != "Date Range") {
            // Extract the month value from the selected date spinner
            val selectedMonth =selectedBulanSpinner.value?.toIntOrNull()
            if (selectedMonth != null) {
                 startDate = constructStartDate(selectedMonth)
                endDate = constructEndDate(selectedMonth)
            } else {
                // Invalid month value, handle the error case
                startDate = null
                endDate = null
            }
        } else {
            // Date range option selected, use the selected start and end dates
            startDate = formatDate(selectedStartDate.value)
            endDate = formatDate(selectedEndDate.value)
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
            _recyclerViewData.value = filteredData
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun constructStartDate(month: Int): String? {
        val startDate = YearMonth.of(2023, month).atDay(1)
        return startDate.format(DateTimeFormatter.ISO_DATE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun constructEndDate(month: Int): String? {
        val endDate = YearMonth.of(2023, month).atEndOfMonth()
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
            var newData = withContext(Dispatchers.IO) {
                val list = datasource1.getKategoriNameD(value)
                val modifiedList = listOf("ALL") + list // Create a new list with the added value
                modifiedList // Return the modified list
            }
            _kategori_entries.value = newData
        }
    }

    //Input Fragment

    fun saveTransaction(){
        viewModelScope.launch {
            val transaction = TransactionTable()
            //val selected_kategori = getSelectedCategory()
            val kategori:String = selectedKategoriSpinner.value ?: ""
            transaction.category_id= getCategoryId(kategori)
            transaction.date = getDate()
            transaction.note = note.value!!
            transaction.nominal = getNominal()
            insert(transaction)
        }
    onNavigateToHomeScreen()
    }
    fun deleteTransaction(trans_id:Int){
        viewModelScope.launch {

            delete_trans(trans_id)
        }
    }
    private suspend fun insert(transaksi: TransactionTable){ withContext(Dispatchers.IO){datasource2.insert(transaksi)} }
    private suspend fun delete_trans(trans_id:Int){ withContext(Dispatchers.IO){datasource2.delete(trans_id)} }

    /********************************************Navigation***********************************************/
    //Navigating to transaction
    fun onClick(){ onNavigateToTransaction(-1) }
    fun onNavigateToTransaction(id:Int){ _navigate_to_transaction.value = id }
    @SuppressLint("NullSafeMutableLiveData")
    fun onNavigatedToTransaction(){ _navigate_to_transaction.value = null }

    //Navigate to input
    fun onNavigateToInput(){ _navigate_to_input.value = true }
    @SuppressLint("NullSafeMutableLiveData")
    fun onNavigatedToInout(){ _navigate_to_input.value = null }

    //Navigate to HomeScreen
    fun onNavigateToHomeScreen(){ _navigate_to_homeScreen.value = true }
    @SuppressLint("NullSafeMutableLiveData")
    fun onNavigatedToHomeScreen(){_navigate_to_homeScreen.value=null}
    fun onFabHSLongClick(v: View): Boolean { return true }


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

                return MainViewModel(
                    application,dataSource,dataSource2
                ) as T
            }
        }
    }

}