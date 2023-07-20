package com.example.budgettracker2.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.budgettracker2.database.BudgetDB
import com.example.budgettracker2.database.CategoryDao
import com.example.budgettracker2.database.CategoryTable
import com.example.budgettracker2.database.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HSViewModel (application: Application,
                   val datasource1: CategoryDao,
                   val datasource2: TransactionDao
): AndroidViewModel(application){
    /*************************************************************************************************************/
    /****************************************Variables***********************************************************/
    /************************************************************************************************************/

    /************************************************Navigation**************************************************/
    private var _navigate_to_transaction = MutableLiveData<Int>()
    val navigate_to_transaction : LiveData<Int>
        get() = _navigate_to_transaction

    private var _navigate_to_input = MutableLiveData<Boolean>()
    val navigate_to_input : LiveData<Boolean>
        get() = _navigate_to_input

    private val _selected_tipe_ac = MutableLiveData<String>("PENGELUARAN")
    val selected_tipe:LiveData<String> get() = _selected_tipe_ac

    val _selected_color_ac = MutableLiveData<String>("BLUE")
    val selected_color_ac:LiveData<String> get() = _selected_color_ac

    /****************************************************HomeScreen**********************************************/
    val kategori = datasource1.getAllKategori()

    val budget_rn = datasource2.getBuget()//income this month
    val tm_spend = datasource2.getSumByCategoryType("PENGELUARAN")
    val tm_income = datasource2.getSumByCategoryType("PEMASUKAN")
    var budget_tmm = datasource2.getBugetTM()//budget left this month

    private var _is_ac_dialog_show = MutableLiveData<Boolean>()
    val is_ac_dialog_show : LiveData<Boolean>
        get() = _is_ac_dialog_show

    private var _clicked_category = MutableLiveData<CategoryTable>(CategoryTable())
    val clicked_category: LiveData<CategoryTable> get() = _clicked_category

    val _kategori_name_ac = MutableLiveData<String>("")

    val c = datasource1.getAllKategoriCoba()


    /********************************************Add Category Dialog**************************************/

    fun saveNewCategotry(){
        viewModelScope.launch {
            val category = CategoryTable()
            category.category_name = _kategori_name_ac.value ?: ""
            category.category_type = selected_tipe.value ?: "PENGELUARAN"
            category.category_color =selected_color_ac.value ?: "BLUE"
            insertNewCategory(category)
            Log.i("UPDATEC","save"+category.toString())
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
    fun deleteCategory(id:Int){
        viewModelScope.launch {
            //_deleteCategory(id)
            _deleteCategory1(clicked_category.value!!)
        }
    }
    fun updateCategory(){
        viewModelScope.launch {
            var c = CategoryTable()
            c.category_name = _kategori_name_ac.value ?:""
            c.category_type = selected_tipe.value!!
            c.category_color = selected_color_ac.value!!
            c.category_id = clicked_category.value!!.category_id
            Log.i("UPDATEC","update"+c.toString())
            resetvalues()
            _update(c)
        }
    }
    fun resetvalues(){
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
    private suspend fun _deleteCategory(id:Int){
        withContext(Dispatchers.IO){datasource1.delete(id)}
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
    fun onNavigateToInput(){ _navigate_to_input.value = true }
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
                val savedStateHandle = extras.createSavedStateHandle()
                val dataSource = BudgetDB.getInstance(application).category_dao
                val dataSource2 = BudgetDB.getInstance(application).transaction_dao

                return HSViewModel(
                    application,dataSource,dataSource2
                ) as T
            }
        }
    }

}