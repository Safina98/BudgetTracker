package com.example.budgettracker2.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransaksiDao {
   /*
    @Insert
    fun insert(tabelTransaksi: TabelTransaksi)
    @Update
    fun update(tabelTransaksi: TabelTransaksi)

    @Query("SELECT * FROM tabel_transaksi")
    fun getAllTabelTransaksiCoba(): LiveData<List<TabelTransaksi>>


    */
    //@Query("SELECT * FROM tabel_transaksi, SELECT cath_name, tipe, color FROM tabel_kategori INNER JOIN tabel_kategori ON tabel_transaksi.cath_id = tabel_kategori.id")
   // @Query("SELECT trans_id, cath_id, ket,date, nominal, cath_name,tipe,color FROM tabel_transaksi JOIN tabel_kategori ")
    //fun getTransactionsWithCategory(): LiveData<List<TransactionWithCategory>>
    /*
    @Query("SELECT tabel_transaksi.*, tabel_kategori.cath_name, tabel_kategori.tipe, tabel_kategori.color " +
            "FROM tabel_transaksi " +
            "INNER JOIN tabel_kategori ON tabel_transaksi.cath_id = tabel_kategori.id")

     */
    //@Query("SELECT * FROM tabel_transaksi")
    // SELECT :brand_name_ as brand_name, (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1) as cath_code FROM brand_table WHERE NOT EXISTS(SELECT brand_name,cath_code FROM brand_table WHERE brand_name =:brand_name_ AND cath_code = (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1)) LIMIT 1 ")
   // @Query("SELECT trans_id AS id, ket as cath_name_m, ket AS ket,date AS date, nominal AS nominal FROM tabel_transaksi")
   // @Query("SELECT * from tabel_transaksi")
    //fun getTransactionsWithCategory(): LiveData<List<TabelTransaksi>>




}