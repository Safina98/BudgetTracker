package com.example.budgettracker2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

@Dao
interface TransactionDao{
    @Insert
    fun insert(TransactionTable: TransactionTable)
    @Update
    fun update(TransactionTable: TransactionTable)

    @Query("DELETE FROM transaction_table WHERE transaction_id =:trans_id")
    fun delete(trans_id:Int)

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactionTableCoba(): LiveData<List<TransactionTable>>
    // // SELECT :brand_name_ as brand_name, (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1) as cath_code FROM brand_table WHERE NOT EXISTS(SELECT brand_name,cath_code FROM brand_table WHERE brand_name =:brand_name_ AND cath_code = (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1)) LIMIT 1 ")
    @Query("SELECT transaction_table.transaction_id as id, category_table.category_name as category_name_model_, transaction_table.note as ket, strftime('%Y-%m-%d', transaction_table.date) as date, transaction_table.nominal FROM transaction_table JOIN category_table ON transaction_table.category_id = category_table.category_id")
    fun getAllTransactionsWithCategoryName(): LiveData<List<TransaksiModel>>

    @Query("SELECT transaction_table.transaction_id as id, category_table.category_name as category_name_model_, transaction_table.note as ket, strftime('%Y-%m-%d', transaction_table.date) as date, transaction_table.nominal FROM transaction_table JOIN category_table ON transaction_table.category_id = category_table.category_id")
    fun getAllTransactionsWithCategoryList(): List<TransaksiModel>

   // @Query("SELECT * FROM transaction_table WHERE category_id IN (SELECT category_id FROM category_table WHERE category_type = :tipe)")
   @Query("SELECT transaction_id as id, category_name as category_name_model_, note as ket, date, nominal FROM transaction_table INNER JOIN category_table ON transaction_table.category_id = category_table.category_id WHERE category_table.category_type = :tipe")
    fun getAllTransactionWithCategoriNameTipe(tipe:String): List<TransaksiModel>

    @Query("SELECT transaction_table.transaction_id as id, category_table.category_name as category_name_model_, transaction_table.note as ket, strftime('%Y-%m-%d', transaction_table.date) as date, transaction_table.nominal FROM transaction_table JOIN category_table ON transaction_table.category_id = category_table.category_id WHERE category_name =:kategori")
    fun getAllTransactionsWithCategoryNameKategori(kategori:String): List<TransaksiModel>


    //@Query("SELECT transaction_id as id, category_name as category_name_model_, note as ket, strftime('%Y-%m-%d', transaction_table.date) as date, nominal FROM transaction_table INNER JOIN category_table ON transaction_table.category_id = category_table.category_id WHERE category_table.category_type = :tipe and strftime('%m', date) = :bulan")
    @Query("SELECT transaction_table.transaction_id as id, category_table.category_name as category_name_model_, transaction_table.note as ket, strftime('%Y-%m-%d', transaction_table.date) as date, transaction_table.nominal FROM transaction_table JOIN category_table ON transaction_table.category_id = category_table.category_id WHERE category_type =:tipe and strftime('%m', date) = :bulan")
    fun getAllTransactionsWithCategoryNameTipeDate(tipe:String,bulan:String): List<TransaksiModel>

    @Query("SELECT transaction_table.transaction_id as id, category_table.category_name as category_name_model_, transaction_table.note as ket, strftime('%Y-%m-%d', transaction_table.date) as date, transaction_table.nominal FROM transaction_table JOIN category_table ON transaction_table.category_id = category_table.category_id WHERE strftime('%m', date) = :bulan")
    fun getAllTransactionsWithCategoryNameDate(bulan:String): List<TransaksiModel>
    @Query("SELECT transaction_table.transaction_id as id, category_table.category_name as category_name_model_, transaction_table.note as ket, strftime('%Y-%m-%d', transaction_table.date) as date, transaction_table.nominal FROM transaction_table JOIN category_table ON transaction_table.category_id = category_table.category_id WHERE category_name =:kategori and strftime('%m', date) = :bulan")
    fun getAllTransactionsWithCategoryNameKategoriDate(kategori:String,bulan:String): List<TransaksiModel>
    @Query("SELECT transaction_table.transaction_id AS id, category_table.category_name AS category_name_model_, " +
            "transaction_table.note AS ket, transaction_table.date AS date, transaction_table.nominal AS nominal " +
            "FROM transaction_table " +
            "INNER JOIN category_table ON transaction_table.category_id = category_table.category_id " +
            "WHERE (:type IS NULL OR category_table.category_type = :type) " +
            "AND (:categoryId IS NULL OR transaction_table.category_id = :categoryId) " +
            "AND (:startDate IS NULL OR strftime('%Y-%m-%d', transaction_table.date) >= :startDate) " +
            "AND (:endDate IS NULL OR strftime('%Y-%m-%d', transaction_table.date) <= :endDate)")
    fun getFilteredData(type: String?, categoryId: Int?, startDate: String?, endDate: String?): List<TransaksiModel>
    @Query("SELECT t.transaction_id as id, c.category_name as category_name_model_, t.note as ket, t.date as date, t.nominal FROM transaction_table t INNER JOIN category_table c ON t.category_id = c.category_id WHERE (:type IS NULL OR c.category_type = :type OR :type = 'All') AND (:categoryId IS NULL OR t.category_id = :categoryId) AND (:startDate IS NULL OR t.date >= :startDate) AND (:endDate IS NULL OR t.date <= :endDate) ORDER BY t.date DESC")
    fun getFilteredData2(type: String?, categoryId: Int?, startDate: Long?, endDate: Long?): List<TransaksiModel>

    @Query("SELECT t.transaction_id AS id, c.category_name AS category_name_model_, t.note AS ket, t.date, t.nominal FROM transaction_table t " +
            "INNER JOIN category_table c ON t.category_id = c.category_id WHERE" +
            " (:type IS NULL OR c.category_type = :type OR :type = 'ALL') " +
            "AND (:categoryId IS NULL OR t.category_id = :categoryId) " +
            "AND (:startDate IS NULL OR t.date >= :startDate) " +
            "AND (:endDate IS NULL OR  t.date <= :endDate) ORDER BY t.date DESC")
    fun getFilteredData3(type: String?, categoryId: Int?, startDate: String?, endDate: String?): List<TransaksiModel>
    @Query("SELECT t.transaction_id AS id, c.category_name AS category_name_model_, t.note AS ket, t.date, t.nominal FROM transaction_table t INNER JOIN category_table c ON t.category_id = c.category_id WHERE (:type IS NULL OR c.category_type = :type OR :type = 'All') AND (:categoryId IS NULL OR t.category_id = :categoryId) AND (t.date >= :startDate OR :startDate IS NULL) AND (t.date <= :endDate OR :endDate IS NULL) ORDER BY t.date DESC")
    fun getFilteredData4(type: String?, categoryId: Int?, startDate: Long?, endDate: Long?): List<TransaksiModel>


    @Query("SELECT ifnull(SUM(nominal),0) from transaction_table")
    fun getBuget():LiveData<Int>

    @Query("SELECT ifnull(SUM(nominal),0)  from transaction_table WHERE nominal < 0")
    fun getBugetTM():LiveData<Int>

    @Query("SELECT SUM(nominal) FROM transaction_table t JOIN category_table c ON t.category_id = c.category_id WHERE c.category_type = :categoryType")
    fun getSumByCategoryType(categoryType: String): LiveData<Int>


}