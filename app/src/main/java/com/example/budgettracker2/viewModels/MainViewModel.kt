package com.example.budgettracker2.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.view.View
import android.widget.Toast
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
import java.util.*

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



    //val bulan = listOf<String>("ALL","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","Oktober","November","Desember")
    val bulan = listOf<String>("ALL","01","02","03","04","05","06")
    private val _selectedBulanSpinner = MutableLiveData<String>()
    val selectedBulanSpinner: LiveData<String> get() = _selectedBulanSpinner


    val semuatabeltransaksi = datasource2.getAllTransactionTableCoba()

    private val _recyclerViewData = MutableLiveData<List<TransaksiModel>>()
    val recyclerViewData: LiveData<List<TransaksiModel>>
        get() = _recyclerViewData


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

/*
    val nama_kategori get() = _tipe_position.value.let {tipe_p->
        datasource1.getKategoriName(tipe_list[tipe_p!!])
        }
 */
    //Date Picker
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate
    private var _is_date_picker_clicked = MutableLiveData<Boolean>()
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
    fun getNominal():Int{
       return if(_tipe_position.value!=1){
           jumlah.value?.toInt()!! *-1
       }else{
           jumlah.value!!.toInt()
       }
    }
    fun getTodaysDate():String{
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return  "${year}-${(month+1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
    }
    fun getDate():Date{
        val dateString = selectedDate.value
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.parse(dateString)
    }
    // Method to update the selected value

    fun setSelectedKategoriValue(value: String) {
        _selectedKategoriSpinner.value = value
    }
    fun setSelectedTipeValue(value: String) {
        _selectedTipeSpinner.value = value
    }
    fun setSelectedBulanValue(value: String) {
        _selectedBulanSpinner.value = value
    }

  fun updateRecyclerViewData(value:String) {
       // val selectedValue = _selected_kategori_entri.value
     // Toast.makeText(getApplication(),value+" rv",Toast.LENGTH_SHORT).show()
        viewModelScope.launch {
            val newData = withContext(Dispatchers.IO) {
                datasource2.getAllTransactionsWithCategoryNameKategori(value)
            }
                _recyclerViewData.value = newData
            }
        }
    fun updateRecyclerViewData2(value:String) {
        Toast.makeText(getApplication(),value+" rv",Toast.LENGTH_SHORT).show()
        viewModelScope.launch {
            val newData = withContext(Dispatchers.IO) {
                datasource2.getAllTransactionsWithCategoryNameDate(value)
            }
            _recyclerViewData.value = newData
        }
    }
    fun updateRecyclerViewData3() {
        //Toast.makeText(getApplication(),value+" rv",Toast.LENGTH_SHORT).show()
        viewModelScope.launch {
            val newData = withContext(Dispatchers.IO) {
            if ( selectedTipeSpinner.value=="ALL" && selectedBulanSpinner.value=="ALL"){
                datasource2.getAllTransactionsWithCategoryList()

            }else if(selectedTipeSpinner.value !="ALL"&& selectedBulanSpinner.value=="ALL" && selectedKategoriSpinner.value == "ALL"){
                datasource2.getAllTransactionWithCategoriNameTipe(selectedTipeSpinner.value!!)
            }
            else if(selectedTipeSpinner.value =="ALL"&& selectedBulanSpinner.value!="ALL" && selectedKategoriSpinner.value == "ALL"){
                datasource2.getAllTransactionsWithCategoryNameDate(selectedBulanSpinner.value!!)
            }
            else if(selectedTipeSpinner.value !="ALL" && selectedBulanSpinner.value=="ALL" && selectedKategoriSpinner.value != "ALL"){
                datasource2.getAllTransactionsWithCategoryNameKategori(selectedKategoriSpinner.value!!)

            }
            else if(selectedTipeSpinner.value != "ALL" && selectedBulanSpinner.value!="ALL" && selectedKategoriSpinner.value=="ALL"){
                datasource2.getAllTransactionsWithCategoryNameTipeDate(selectedTipeSpinner.value!!,selectedBulanSpinner.value!!)

            }
            //else if(selectedTipeSpinner.value !="ALL" && selectedKategoriSpinner.value != "ALL" && selectedBulanSpinner.value=="ALL"){
              //  datasource2.getAllTransactionsWithCategoryNameDate(selectedBulanSpinner.value!!)

            //}
            else{
                datasource2.getAllTransactionsWithCategoryNameKategoriDate(selectedKategoriSpinner.value!!+"", selectedBulanSpinner.value!!)

            }
            }
            _recyclerViewData.value = newData
        }
    }
    //transaction fragment
    fun getKategoriEntries(value:String){
        Toast.makeText(getApplication(),value+" kategori",Toast.LENGTH_SHORT).show()
        viewModelScope.launch {
            var newData = withContext(Dispatchers.IO) {
                val list = datasource1.getKategoriNameD(value)
                val modifiedList = listOf("ALL") + list // Create a new list with the added value
                modifiedList // Return the modified list
               // list
            }
            _kategori_entries.value = newData

        }

    }

    //Input Fragment

    fun saveTransaction(){
        viewModelScope.launch {
            val transaction = TransactionTable()
            val selected_kategori = getSelectedCategory()
            transaction.category_id= getCategory_id()
            transaction.date = getDate()
            transaction.note = note.value!!
            transaction.nominal = getNominal()
            Toast.makeText(getApplication(),transaction.date.toString(),Toast.LENGTH_SHORT).show()
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