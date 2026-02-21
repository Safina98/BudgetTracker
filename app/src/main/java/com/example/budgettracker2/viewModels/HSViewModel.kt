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
import com.example.budgettracker2.database.PocketDao
import com.example.budgettracker2.database.PocketTable
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
                   private val datasource2: TransactionDao,
                    private val pocketDao: PocketDao
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
    //Tabungan Laptop
    val lp_money = datasource2.getSumLp()
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
        viewModelScope.launch {
            val category =CategoryTable()
            category.category_id=tokens[0].toInt()
            category.category_name = tokens[1]
            category.category_type = tokens[2]
            category.category_color= tokens[3]
            try {
                _insertCategoryCsv(category)
            }catch (e:Exception){
                Log.i("INSERTCSV",e.toString()+ " " +tokens)
            }

        }

   // insertCsvTrans(tokens)
    //Log.i("INSERTCSV","ViewModel insertCsv: "+category.toString())
    }
    fun getDate(dateString: String): Date {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return inputFormat.parse(dateString)
    }
    private suspend fun getCategoryId(category: String) :Int{
        var a =  withContext(Dispatchers.IO) {
            datasource1.getCategoryIdByName(category)
        }
        return a
    }
    private suspend fun getCategoryByName(category: String) :CategoryTable{
        return withContext(Dispatchers.IO) {
            datasource1.getCategoryByName(category)
        }

    }
    private suspend fun getPocketId(category: String) :Int{
        return  withContext(Dispatchers.IO) {
            pocketDao.getIdByPocketName(category)
        }
    }
    fun insertPocket(tokens:List<String>){
        viewModelScope.launch {
            val pocket=PocketTable()
            pocket.pocket_id=tokens[0].toInt()
            pocket.pocketName=tokens[1]
            pocket.saldo=tokens[2].toInt()
            insertPocketToDao(pocket)
        }
    }
    fun insertCsvTrans(tokens: List<String>){
        viewModelScope.launch {
            var transactionTable = TransactionTable()
            transactionTable.note = tokens[3]
            //Log.i("INSERTCSV","ViewModel insertCsv: "+tokens[3]+" : "+tokens[3])
            try {
                transactionTable.pocket_id=tokens[2].toInt()
                transactionTable.nominal = tokens[5].toDouble().toInt()
                transactionTable.category_id = tokens[1].toInt()
                transactionTable.date = getDate(tokens[4])
                insert(transactionTable)
               // Log.i("INSERTCSV","ViewModel insertCsv try: "+tokens)
            }catch (exception:Exception){
                Log.i("INSERTCSV",exception.toString()+ " " +tokens[4])

            }
        }
    }
    fun insertCategoryCsv(category: CategoryTable){
        viewModelScope.launch {
           // _insertCategoryCsv(category.category_name,category.category_type,category.category_color)
           // Log.i("INSERTCSV","ViewModel insertCsv: "+category.toString())
        }
    }
    suspend fun _insertCategoryCsv(category: CategoryTable){
        withContext(Dispatchers.IO){
          //  datasource1.insertIfNotExist(categoryName, categoryType,categoryColor)
            datasource1.insert(category)
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
    fun populatePocket(){
        viewModelScope.launch {
            val allTransaction= getAllTransaction()
           // val category= getCategoryByName("Tabungan laptop")
            //val pocketLaptopId=getPocketId("Tabungan Laptop")
            val pocketTabunganUtamaId=getPocketId("Tabungan Utama")
            Log.i("PocketProbs","tabungan utama id $pocketTabunganUtamaId")
            //pindah dana
            allTransaction.forEach {


            }
        }

    }

    private suspend fun getIdByName():List<TransactionTable>{
        return withContext(Dispatchers.IO){
            datasource2.getAllTransactionTable()
        }
    }
    private suspend fun getAllTransaction():List<TransactionTable>{
        return withContext(Dispatchers.IO){
            datasource2.getAllTransactionTable()
        }
    }

    fun insertPocket(name:String){
        viewModelScope.launch {
            val pocketTable=PocketTable()
            pocketTable.pocketName=name
            insertPocketToDao(pocketTable)
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
    private suspend fun insertPocketToDao(pocketTable: PocketTable){
        withContext(Dispatchers.IO){
            pocketDao.insertPocket(pocketTable)
        }
    }
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
                val dataSource3 = BudgetDB.getInstance(application).pocket_dao

                return HSViewModel(
                    application,dataSource,dataSource2,dataSource3
                ) as T
            }
        }
    }

}