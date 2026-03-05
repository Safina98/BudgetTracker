package com.example.budgettracker2.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker2.database.model.NewKategoriModel
import com.example.budgettracker2.database.model.TabunganModel
import com.example.budgettracker2.database.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageViewModel @Inject constructor( private val repository: BudgetRepository) :
    ViewModel() {

    private val _namaTabungan=  MutableStateFlow<String>("")
    val namaTabungan: StateFlow<String> = _namaTabungan
    private val _warnaTabungan=  MutableStateFlow<String>("")
    val warnaTabungan: StateFlow<String> = _warnaTabungan
    private val _index= MutableStateFlow<Int?>(null)
    val index: StateFlow<Int?> = _index
    private val _saldo=MutableStateFlow<Int?>(null)
    val saldo: StateFlow<Int?> = _saldo
    private val _pocketId= MutableStateFlow<Int?>(null)
    val pocketId: StateFlow<Int?> = _pocketId


    private val _showUpsertDialog = MutableStateFlow<Boolean>(false)
    val showUpsertDialog: StateFlow<Boolean> = _showUpsertDialog
    private val _showDeleteDialog = MutableStateFlow<Int?>(null)
    val showDeleteDialog: StateFlow<Int?> = _showDeleteDialog

    private val _deleteAllTransaction = MutableStateFlow<Boolean>(false)
    val deleteAllTransaction: StateFlow<Boolean> = _deleteAllTransaction

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage:StateFlow<String?> = _errorMessage


    private val _namaKategori=  MutableStateFlow<String>("")
    val namaKategori: StateFlow<String> = _namaKategori
    private val _tipeKategori=  MutableStateFlow<String>("")
    val tipeKategori: StateFlow<String> = _tipeKategori
    private val _warnaKategori=  MutableStateFlow<String>("")
    val warnaKategori: StateFlow<String> = _warnaKategori
    private val _kategoriId = MutableStateFlow<Int?>(null)
    val kategoriId: StateFlow<Int?> = _kategoriId






    val allPockets: StateFlow<List<TabunganModel>> = repository.getAllPocket()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Grace period for configuration changes
            initialValue = emptyList()
        )
    val allCategory: StateFlow<List<NewKategoriModel>> = repository.getAllCategory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Grace period for configuration changes
            initialValue = emptyList()
        )

    fun onShowUpsertDialog(){
        _showUpsertDialog.value = true
    }
    fun onDeleteClick(id:Int){
        _showDeleteDialog.value = id
    }
    fun onDeleteDialogDismiss(){
        _showDeleteDialog.value = null
    }
    fun toggleDeleteAllTransaction(boolean: Boolean){
        _deleteAllTransaction.value = boolean
    }
    fun onWarnaCategoryChange(warna:String){
        _warnaKategori.value = warna
    }
    fun onKategoriTipeChange(tipe:String){
        _tipeKategori.value = tipe
    }
    fun onKategoriNamaChange(nama:String){
        _namaKategori.value = nama
    }
    fun onTabunganNamaChange(nama:String){
        _namaTabungan.value = nama
    }
    fun onWarnaTabunganChange(warna:String){
        _warnaTabungan.value = warna
    }
    fun onSaldoChange(saldo:Int){
        _saldo.value = saldo
    }
    fun onEditKategoriClick(nkm: NewKategoriModel){
        _namaKategori.value=nkm.categoryTable.category_name
        _tipeKategori.value=nkm.categoryTable.category_type
        _warnaKategori.value=nkm.categoryTable.category_color
        _kategoriId.value=nkm.categoryTable.category_id
        onShowUpsertDialog()

    }

    fun onCloseErrorMessage(){
        _errorMessage.value=null
    }

    fun clearMutbale(){
        _showUpsertDialog.value=false
        _namaTabungan.value=""
        _warnaTabungan.value=""
        _saldo.value=0
        _namaKategori.value=""
        _tipeKategori.value=""
        _warnaKategori.value=""
        _kategoriId.value=null
        _pocketId.value=null
        _index.value=null
        _deleteAllTransaction.value=false
    }
    fun onEditTabunganClick(pocket: TabunganModel){
        _namaTabungan.value=pocket.pocketTable.pocketName
        _saldo.value=pocket.pocketTable.saldo
        _pocketId.value=pocket.pocketTable.pocket_id
        _warnaTabungan.value=pocket.pocketTable.color
        _index.value=pocket.pocketTable.index
        onShowUpsertDialog()
    }

    fun onSaveTabunganClick(){
        if (pocketId.value==null)insertTabungan() else updateTabungan()
    }


    fun insertTabungan(){
        viewModelScope.launch {
            repository.insertPocketWithInitialBalance(namaTabungan.value.uppercase().trim(), warnaTabungan.value,index.value?:0,false,saldo.value?:0)
                .onSuccess {
                    clearMutbale()
                }
                .onFailure { exception ->
                    _errorMessage.value = "Insert failed. "+exception.localizedMessage
                }
        }
    }
    fun updateTabungan(){
        viewModelScope.launch {
            repository.updatePocketWithInitialBalance(pocketId.value!!,namaTabungan.value.uppercase().trim(), warnaTabungan.value.trim(),index.value?:0,false,saldo.value?:0)
                .onSuccess {
                    clearMutbale()
                }
                .onFailure { exception ->
                    _errorMessage.value = "Update failed. "+exception.localizedMessage
                }
        }
    }

    fun deleteTabungan(){
        viewModelScope.launch {
            repository.deleteTabungan(showDeleteDialog.value!!,deleteAllTransaction.value!!)
                .onSuccess {
                    clearMutbale()
                    onDeleteDialogDismiss()
                }.onFailure { exception ->
                    _errorMessage.value = "Delete failed. "+exception.localizedMessage
                }
        }
    }
    fun onKategoriSaveClick(){
        if (kategoriId.value==null){
            insertKategori()
        }else {
            updateCategory()
        }
    }
    fun insertKategori(){
        viewModelScope.launch {
            repository.insertKategori(namaKategori.value.uppercase().trim(), tipeKategori.value, warnaKategori.value)
                .onSuccess {
                    clearMutbale()
                }.onFailure { exception ->
                    _errorMessage.value = "Insert failed. "+exception.localizedMessage
                }
        }
    }

    fun updateCategory(){
        viewModelScope.launch {
            repository.updateKategori(kategoriId.value!!,namaKategori.value.uppercase().trim(), tipeKategori.value, warnaKategori.value)
                .onSuccess {
                    clearMutbale()
                }.onFailure { exception ->
                   _errorMessage.value="Update Failed. "+exception.localizedMessage
                }
        }
    }

    fun deleteKategori(){
        viewModelScope.launch {
            if (showDeleteDialog.value!=5){
                repository.deleteKategori(showDeleteDialog.value!!,deleteAllTransaction.value)
                    .onSuccess {
                        clearMutbale()
                        onDeleteDialogDismiss()
                    }.onFailure { exception ->
                        _errorMessage.value = "Delete failed. "+exception.localizedMessage
                    }
            }else{
                _errorMessage.value = "Delete Failed. Initial saving is a default category which can't be deleted"
            }

        }
    }

}
