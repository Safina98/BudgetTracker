package com.example.budgettracker2.transactions


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker2.R
import com.example.budgettracker2.database.TransaksiModel
import com.example.budgettracker2.databinding.TransactionItemListBinding

class TransactionAdapter(
    val context: Context,
    private val clickListener: TransaksiClickListener,
    private val longListener: TransaksiLongClickListener
) : ListAdapter<TransaksiModel,
        TransactionAdapter.ViewHolder>(TransaksiDiffCallback()) {
    class ViewHolder private constructor(val binding: TransactionItemListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind( context: Context,item: TransaksiModel,clickListener: TransaksiClickListener,longListener: TransaksiLongClickListener) {
            binding.transaksi = item
            binding.clickListener = clickListener
            binding.longC = longListener
            binding.cv.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_1))
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
        holder.bind(context,item,clickListener,longListener)
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
class TransaksiClickListener(val clickListener: (transId: Int) -> Unit) {
    fun onClick(id:Int) {
        clickListener(id)
    }
}
class TransaksiLongClickListener(val longListener:(transId:Int)->Unit){
    fun onLongClick(v: View, id:Int): Boolean {
        longListener(id)
        return true}

}