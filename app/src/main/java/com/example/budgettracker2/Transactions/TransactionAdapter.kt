package com.example.budgettracker2.HomeScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker2.R
import com.example.budgettracker2.database.KategoriModel
import com.example.budgettracker2.database.TransaksiModel
import com.example.budgettracker2.databinding.KategoriItemListBinding
import com.example.budgettracker2.databinding.TransactionItemListBinding
import com.example.budgettracker2.warna
import kotlin.random.Random

class TransactionAdapter(
    val context: Context,
    val clickListener: TransaksiListener
) : ListAdapter<TransaksiModel,
        TransactionAdapter.ViewHolder>(TransaksiDiffCallback()) {
    class ViewHolder private constructor(val binding: TransactionItemListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind( context: Context,item: TransaksiModel,position: Int) {

            binding.transaksi = item
            
            /*
           if (position%5==0|| position%5==4){
               binding.btnItemKategori.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_kategori_list))
           }else if(position%5==1){
               binding.btnItemKategori.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_kategori_list2))
           }else if(position%5==2){
               binding.btnItemKategori.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_kategori_list3))
           }else if(position%5==3){
               binding.btnItemKategori.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_kategori_list4))
           }

             */

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TransactionItemListBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(context,item,position)
    }
}
class TransaksiDiffCallback : DiffUtil.ItemCallback<TransaksiModel>() {
    override fun areItemsTheSame(oldItem: TransaksiModel, newItem: TransaksiModel): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem:TransaksiModel, newItem: TransaksiModel): Boolean {
        return oldItem == newItem
    }
}
class TransaksiListener(val clickListener: (transId: Int) -> Unit) {
    fun onClick(trans:TransaksiModel) {
        clickListener(trans.id)
    }
}