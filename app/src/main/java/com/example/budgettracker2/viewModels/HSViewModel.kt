package com.example.budgettracker2.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.budgettracker2.database.BudgetDB
import com.example.budgettracker2.database.CategoryDao
import com.example.budgettracker2.database.CategoryTable
import com.example.budgettracker2.database.TransactionDao
import com.example.budgettracker2.database.TransactionTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HSViewModel (application: Application,
                   private val datasource1: CategoryDao,
                   private val datasource2: TransactionDao
): AndroidViewModel(application){
    /*************************************************************************************************************/
    /****************************************Variables***********************************************************/
    /************************************************************************************************************/

    /************************************************Navigation**************************************************/
    private var _navigate_to_transaction = MutableLiveData<Int>()
    val navigate_to_transaction : LiveData<Int>
        get() = _navigate_to_transaction

    private var _navigate_to_input = MutableLiveData<Int>()
    val navigate_to_input : LiveData<Int>
        get() = _navigate_to_input

    private val _selected_tipe_ac = MutableLiveData<String>("PENGELUARAN")
    val selected_tipe:LiveData<String> get() = _selected_tipe_ac

    val _selected_color_ac = MutableLiveData<String>("BLUE")
    val selected_color_ac:LiveData<String> get() = _selected_color_ac

    /****************************************************HomeScreen**********************************************/
    val kategori = datasource1.getAllKategori()



    //Total uang tahun ini
    val ty_money = datasource2.getSumTM()
    //Total pengeluaran tahun ini
    val tySpent = datasource2.getSumByCategoryType("PENGELUARAN")


    private var _is_ac_dialog_show = MutableLiveData<Boolean>()
    val is_ac_dialog_show : LiveData<Boolean>
        get() = _is_ac_dialog_show

    private var _clicked_category = MutableLiveData<CategoryTable>(CategoryTable())
    val clicked_category: LiveData<CategoryTable> get() = _clicked_category

    val _kategori_name_ac = MutableLiveData<String>("")

////////////////////////////////////////////Insert CSV HomeScreen///////////////////////////////////////////
    fun insertCsv(tokens:List<String>){
        var category =CategoryTable()
    category.category_name = tokens[4]
    category.category_type = tokens[5]
    category.category_color= "BLUE"
    insertCategoryCsv(category)
    insertCsvTrans(tokens)
    //Log.i("INSERTCSV","ViewModel insertCsv: "+category.toString())
    }
    fun getDate(dateString:String):Date{
        val inputFormat = SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH)
        return inputFormat.parse(dateString)
    }
    private suspend fun getCategoryId(category: String) :Int{
        var a =  withContext(Dispatchers.IO) {
            datasource1.getCategoryIdByName(category)
        }
        return a
    }
    fun insertCsvTrans(tokens: List<String>){
        viewModelScope.launch {
            var transactionTable = TransactionTable()
            transactionTable.note = tokens[2]
            Log.i("INSERTCSV","ViewModel insertCsv: "+tokens[2]+" : "+tokens[3])
            try {
                transactionTable.nominal = tokens[3].toDouble().toInt()
                transactionTable.category_id = getCategoryId(tokens[4])
                transactionTable.date = getDate(tokens[1])
                insert(transactionTable)
                Log.i("INSERTCSV","ViewModel insertCsv try: "+tokens)
            }catch (exception:Exception){
                Log.i("INSERTCSV",exception.toString()+ " " +tokens)

            }
        }
    }
    fun insertCategoryCsv(category: CategoryTable){
        viewModelScope.launch {
            _insertCategoryCsv(category.category_name,category.category_type,category.category_color)
           // Log.i("INSERTCSV","ViewModel insertCsv: "+category.toString())
        }
    }
    suspend fun _insertCategoryCsv(categoryName:String,categoryType:String,categoryColor:String){
        withContext(Dispatchers.IO){
            datasource1.insertIfNotExist(categoryName, categoryType,categoryColor)
        }
    }
    private suspend fun insert(transaksi: TransactionTable){ withContext(Dispatchers.IO){datasource2.insert(transaksi)} }

    /********************************************Add Category Dialog**************************************/

    fun saveNewCategotry(){
        viewModelScope.launch {
            val category = CategoryTable()
            category.category_name = _kategori_name_ac.value ?: ""
            category.category_type = selected_tipe.value ?: "PENGELUARAN"
            category.category_color =selected_color_ac.value ?: "BLUE"
            insertNewCategory(category)
            resetvalues()
        }
    }

    fun getClickedCategort(id:Int){
        viewModelScope.launch {
           val a =withContext(Dispatchers.IO) {
               datasource1.getCategory(id)
            }
            _clicked_category.value = a
        }
    }
    fun deleteCategory(){
        viewModelScope.launch {
            //_deleteCategory(id)
            _deleteCategory1(clicked_category.value!!)
        }
    }
    fun updateCategory(){
        viewModelScope.launch {
            val c = CategoryTable()
            c.category_name = _kategori_name_ac.value ?:""
            c.category_type = selected_tipe.value!!
            c.category_color = selected_color_ac.value!!
            c.category_id = clicked_category.value!!.category_id
            resetvalues()
            _update(c)
        }
    }
    private fun resetvalues(){
        _kategori_name_ac.value=""
        _selected_tipe_ac.value="PENGELUARAN"
        _selected_color_ac.value="BLUE"
    }

    fun setSelectedTipeValue(value:String){
        _selected_tipe_ac.value = value
    }
    fun setSelectedColorValue(value: String){
        _selected_color_ac.value = value
    }

    fun onAddCategoryClick(){
        _is_ac_dialog_show.value = true
    }
    fun onAddCategoryClicked(){
        _is_ac_dialog_show.value = false
    }
    private suspend fun insertNewCategory(category: CategoryTable){
        withContext(Dispatchers.IO){datasource1.insert(category)}
    }

    private suspend fun _deleteCategory1(c :CategoryTable){
        withContext(Dispatchers.IO){datasource1.delete1(c)}
    }
    private suspend fun _update(c:CategoryTable){
        withContext(Dispatchers.IO){datasource1.update(c)
    }}
    /********************************************Navigation***********************************************/
    //Navigating to transaction
    fun onClick(){ onNavigateToTransaction(-1) }
    fun onNavigateToTransaction(id:Int){ _navigate_to_transaction.value = id }
    @SuppressLint("NullSafeMutableLiveData")
    fun onNavigatedToTransaction(){ _navigate_to_transaction.value = null }

    //Navigate to input
    fun onNavigateToInput(id:Int){ _navigate_to_input.value = id }
    @SuppressLint("NullSafeMutableLiveData")
    fun onNavigatedToInout(){ _navigate_to_input.value = null }


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
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras

                val dataSource = BudgetDB.getInstance(application).category_dao
                val dataSource2 = BudgetDB.getInstance(application).transaction_dao

                return HSViewModel(
                    application,dataSource,dataSource2
                ) as T
            }
        }
    }

}