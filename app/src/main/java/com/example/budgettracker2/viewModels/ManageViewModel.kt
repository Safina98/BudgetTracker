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

    val namaTabungan=  MutableStateFlow<String>("")
    val saldo=MutableStateFlow<Int>(0)
    val pocketId= MutableStateFlow<Int?>(null)


    val _showUpsertDialog = MutableStateFlow<Boolean>(false)
    val showUpsertDialog: StateFlow<Boolean> = _showUpsertDialog
    val _showDeleteDialog = MutableStateFlow<Int?>(null)
    val showDeleteDialog: StateFlow<Int?> = _showDeleteDialog

    val _deleteAllTransaction = MutableStateFlow<Boolean>(false)
    val deleteAllTransaction: StateFlow<Boolean> = _deleteAllTransaction

    val namaKategori=  MutableStateFlow<String>("")
    val tipeKategori=  MutableStateFlow<String>("")
    val warnaKategori=  MutableStateFlow<String>("")
    val kategoriId= MutableStateFlow<Int?>(null)

    private val _errorMessage = MutableStateFlow<String?>(null)


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
    fun onEditKategoriClick(nkm: NewKategoriModel){
        namaKategori.value=nkm.categoryTable.category_name
        tipeKategori.value=nkm.categoryTable.category_type
        warnaKategori.value=nkm.categoryTable.category_color
        kategoriId.value=nkm.categoryTable.category_id
        onShowUpsertDialog()

    }

    fun clearMutbale(){
        _showUpsertDialog.value=false
        namaTabungan.value=""
        saldo.value=0
        namaKategori.value=""
        tipeKategori.value=""
        warnaKategori.value=""
        kategoriId.value=null
        pocketId.value=null
        _deleteAllTransaction.value=false
    }
    fun onEditTabunganClick(pocket: TabunganModel){
        namaTabungan.value=pocket.pocketTable.pocketName
        saldo.value=pocket.pocketTable.saldo
        pocketId.value=pocket.pocketTable.pocket_id
        onShowUpsertDialog()
    }

    fun onSaveTabunganClick(){
        Log.i("Tabungan", "onSaveTabunganClick:${pocketId.value}")
        if (pocketId.value==null)insertTabungan() else updateTabungan()
    }


    fun insertTabungan(){
        viewModelScope.launch {

                repository.insertPocketWithInitialBalance(namaTabungan.value.uppercase().trim(), saldo.value)
                .onSuccess {
                    //
                    clearMutbale()
                }
                .onFailure { exception ->
                    // Logic for error (e.g., show "Database Error: ${exception.message}")
                    _errorMessage.value = exception.localizedMessage
                    Log.i("Tabungan", "Insert failed due to: ${exception.localizedMessage}")
                }
        }
    }
    fun updateTabungan(){
        viewModelScope.launch {
            repository.updatePocketWithInitialBalance(pocketId.value!!,namaTabungan.value,saldo.value)
                .onSuccess {
                    //
                    clearMutbale()
                }
                .onFailure { exception ->
                    // Logic for error (e.g., show "Database Error: ${exception.message}")
                    Log.i("Tabungan", "Update failed due to ${exception.localizedMessage}")
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
                    Log.i("Tabungan", "Delete failed due to ${exception.localizedMessage}")
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
                    Log.i("Kategori", "${exception.localizedMessage}")
                }
        }
    }

    fun updateCategory(){
        viewModelScope.launch {
            repository.updateKategori(kategoriId.value!!,namaKategori.value.uppercase().trim(), tipeKategori.value, warnaKategori.value)
                .onSuccess {
                    clearMutbale()
                }.onFailure { exception ->
                    Log.i("Kategori", "${exception.localizedMessage}")
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
                        Log.i("Kategori", "${exception.localizedMessage}")
                    }
            }else{
                Log.i("Kategori", "Initial saving cannot be deleted")
            }

        }
    }

}
