package com.example.budgettracker2.transactions

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.budgettracker2.R
import com.example.budgettracker2.databinding.FragmentTransactionBinding
import com.example.budgettracker2.viewModels.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Date

class TransactionFragment : Fragment() {
    private var startDate: Date? = null
    private var endDate: Date? = null

    private val viewModel :MainViewModel by viewModels {MainViewModel.Factory  }
    @RequiresApi(Build.VERSION_CODES.O)
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
                viewModel.setSelectedBulanValue(selectedItem)
                // Update the selected value in your ViewModel
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when nothing is selected
            }
        }
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

        viewModel.selectedTipeSpinner.observe(viewLifecycleOwner, Observer { value ->
            // Handle the selected value
            viewModel.getKategoriEntries(value)
        })
        viewModel.selectedBulanSpinner.observe(viewLifecycleOwner, Observer { value ->
            // Handle the selected value
            viewModel.updateRv4()
        })
        viewModel.selectedStartDate.observe(viewLifecycleOwner,Observer{

        })
        viewModel.selectedEndDate.observe(viewLifecycleOwner,Observer{
            viewModel.updateRv4()

        })
        viewModel.is_date_picker_clicked.observe(viewLifecycleOwner,Observer{
            if (it==true) {
                showDatePickerDialog()
                viewModel.onDatePickerClicked()}
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

        viewModel.selectedKategoriSpinner.observe(viewLifecycleOwner, Observer { value ->
            // Handle the selected value
            viewModel.updateRv4()
        })
        return binding.root

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Trigger loading the spinner entries
        viewModel.getKategoriEntries("PENGELUARAN")
        //viewModel.updateRecyclerViewData("BEAUTY")
        viewModel.updateRv4()
    }
    private fun showDatePickerDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.date_range_picker, null)
        val datePickerStart = dialogView.findViewById<DatePicker>(R.id.datepicker_start)
        val datePickerEnd = dialogView.findViewById<DatePicker>(R.id.datepicker_end)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select Date Range")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val startDate = Calendar.getInstance().apply {
                    set(datePickerStart.year, datePickerStart.month, datePickerStart.dayOfMonth)
                }.time
                val endDate = Calendar.getInstance().apply {
                    set(datePickerEnd.year, datePickerEnd.month, datePickerEnd.dayOfMonth)
                }.time

                // Perform any necessary actions with the selected date range
                // Update your ViewModel or perform any other logic
                Toast.makeText(context,startDate.toString()+" "+endDate.toString(),Toast.LENGTH_SHORT).show()

                viewModel.setSelectedBulanValue("Date Range")
                viewModel.setDateRange(startDate, endDate)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }


}