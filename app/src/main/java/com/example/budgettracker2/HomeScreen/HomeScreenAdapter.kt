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
import com.example.budgettracker2.databinding.KategoriItemListBinding

class HomeScreenAdapter(
    val context: Context,
    val clickListener: KategoriListener
) : ListAdapter<KategoriModel,
        HomeScreenAdapter.ViewHolder>(KategoriDiffCallback()) {
    class ViewHolder private constructor(val binding: KategoriItemListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind( context: Context,item: KategoriModel,position: Int) {

            binding.kategori = item
            when(item.category_color_){
                "BLUE"->binding.btnItemKategori.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_kategori_list))
                "GREEN"->binding.btnItemKategori.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_kategori_list2))
                "YELLOW"->binding.btnItemKategori.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_kategori_list3))
                "PURPLE"->binding.btnItemKategori.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_kategori_list4))
            }
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
                val binding = KategoriItemListBinding.inflate(layoutInflater, parent, false)

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
class KategoriDiffCallback : DiffUtil.ItemCallback<KategoriModel>() {
    override fun areItemsTheSame(oldItem: KategoriModel, newItem: KategoriModel): Boolean {
        return oldItem.id_ == newItem.id_
    }
    override fun areContentsTheSame(oldItem:KategoriModel, newItem: KategoriModel): Boolean {
        return oldItem == newItem
    }
}
class KategoriListener(val clickListener: (cathId: Int) -> Unit) {
    fun onClick(cath:KategoriModel) {
        clickListener(cath.id_)
    }
}