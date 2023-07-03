package com.example.budgettracker2.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
            TransaksiClickListener {
                //viewModel.onNavigateToTransaction(it)
                Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show()
            },
            TransaksiLongClickListener {
                viewModel.deleteTransaction(it)
            }
        )

        binding.listTransaksi.adapter = adapter
        binding.spinnerTipeT.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Update the selected value in your ViewModel
                viewModel.setSelectedTipeValue(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when nothing is selected
            }
        }
        binding.spinnerBulan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Update the selected value in your ViewModel
                viewModel.setSelectedBulanValue(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when nothing is selected
            }
        }

        viewModel.selectedTipeSpinner.observe(viewLifecycleOwner, Observer { value ->
            // Handle the selected value
            viewModel.getKategoriEntries(value)
        })
        viewModel.selectedBulanSpinner.observe(viewLifecycleOwner, Observer { value ->
            // Handle the selected value
           // viewModel.updateRecyclerViewData2(value)
            viewModel.updateRecyclerViewData3()
        })

        viewModel.kategori_entries.observe(viewLifecycleOwner, Observer { entries ->
            val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, entries)
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerKategori.adapter = adapter1
        })

        viewModel.recyclerViewData.observe(viewLifecycleOwner, Observer { data ->
            // Update the RecyclerView adapter with the new data
            adapter.submitList(data)

        })

        binding.spinnerKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Update the selected value in your ViewModel
                viewModel.setSelectedKategoriValue(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when nothing is selected
            }
        }

        viewModel.selectedKategoriSpinner.observe(viewLifecycleOwner, Observer { value ->
            // Handle the selected value
            //viewModel.updateRecyclerViewData(value)
            viewModel.updateRecyclerViewData3()
        })
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Trigger loading the spinner entries
        viewModel.getKategoriEntries("PENGELUARAN")
        viewModel.updateRecyclerViewData("BEAUTY")
    }

}