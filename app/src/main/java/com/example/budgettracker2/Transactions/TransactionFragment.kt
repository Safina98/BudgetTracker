package com.example.budgettracker2.Transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.budgettracker2.HomeScreen.HomeScreenAdapter
import com.example.budgettracker2.HomeScreen.KategoriListener
import com.example.budgettracker2.HomeScreen.TransactionAdapter
import com.example.budgettracker2.HomeScreen.TransaksiListener
import com.example.budgettracker2.R
import com.example.budgettracker2.databinding.FragmentTransactionBinding
import com.example.budgettracker2.viewModels.MainViewModel

class TransactionFragment : Fragment() {

    private val viewModel :MainViewModel by viewModels {MainViewModel.Factory  }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding :FragmentTransactionBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_transaction,container,false
        )
        val application = requireNotNull(this.activity).application
        binding.tVmodel = viewModel
        binding.lifecycleOwner = this
        val adapter = TransactionAdapter(
            this.requireContext(),
            TransaksiListener {
                viewModel.onNavigateToTransaction(it)
            }
        )
        viewModel.transaction.observe(viewLifecycleOwner, Observer { it?.let {
            adapter.submitList(it)
        } })
        binding.listTransaksi.adapter = adapter
        //adapter.submitList(viewModel.transactions)


        return binding.root

    }

}