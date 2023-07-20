package com.example.budgettracker2.transactions

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.budgettracker2.R
import com.example.budgettracker2.databinding.FragmentTransactionBinding
import com.example.budgettracker2.viewModels.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Date

class TransactionFragment : Fragment() {

    private val viewModel :MainViewModel by activityViewModels { MainViewModel.Factory }
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
                Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
                //viewModel.deleteTransaction(it)
            },
            TransaksiLongClickListener {
              //  Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
                viewModel.getClickedTransTab(it)
                showOptionDialog(it)
               // viewModel.deleteTransaction(it)
            }
        )

        binding.listTransaksi.adapter = adapter
        val adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, resources.getStringArray(R.array.tipe_list_all))
        adapter2.setDropDownViewResource(R.layout.spinner_item_layout)
        binding.spinnerTipeT.adapter = adapter2

        val adapter3 = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, resources.getStringArray(R.array.bulan))
        adapter3.setDropDownViewResource(R.layout.spinner_item_layout)
        binding.spinnerBulan.adapter = adapter3

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
        viewModel.selectedStartDate.observe(viewLifecycleOwner,Observer{})
        viewModel.clicked_transtab.observe(viewLifecycleOwner, Observer {
            Log.i("UPDATET","fragment: "+it.toString())
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
            val adapter1 = ArrayAdapter(requireContext(),R.layout.spinner_item_layout, entries)
            adapter1.setDropDownViewResource(R.layout.spinner_item_layout)
            binding.spinnerKategori.adapter = adapter1
        })

        viewModel.recyclerViewData.observe(viewLifecycleOwner, Observer {
            it?.let{
            adapter.submitList(it)
                adapter.notifyDataSetChanged()
          //  Log.i("UPDATET",it.toString())
        }
            // Update the RecyclerView adapter with the new data
        })

        viewModel.selectedKategoriSpinner.observe(viewLifecycleOwner, Observer { value ->
            // Handle the selected value
            viewModel.updateRv4()
        })
        viewModel.navigate_to_input.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                this.findNavController().navigate(TransactionFragmentDirections.actionTransactionFragmentToInputFragment3(it))
                viewModel.onNavigatedToInout()
            }
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
                viewModel.setSelectedBulanValue("Date Range")
                viewModel.setDateRange(startDate, endDate)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showOptionDialog(id:Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Options")
        dialogBuilder.setMessage("Choose an action:")
        dialogBuilder.setPositiveButton("Update") { dialogInterface: DialogInterface, _: Int ->
           // showACDialog(id)
            viewModel.setValueForUpdate()
            viewModel.onNavigateToInput(id)
            dialogInterface.dismiss()
        }
        dialogBuilder.setNegativeButton("Delete") { dialogInterface: DialogInterface, _: Int ->
            Toast.makeText(requireContext(),id.toString(),Toast.LENGTH_SHORT).show()
            viewModel.deleteTransaction(0)
            dialogInterface.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }


}