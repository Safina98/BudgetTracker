package com.example.budgettracker2.transactions

import android.annotation.SuppressLint
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
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.budgettracker2.R
import com.example.budgettracker2.databinding.FragmentTransactionBinding
import com.example.budgettracker2.viewModels.MainViewModel
import java.util.Calendar
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import com.example.budgettracker2.FragmentInput.InputFragmentArgs

class TransactionFragment : Fragment() {

    private val viewModel :MainViewModel by activityViewModels { MainViewModel.Factory }
    private lateinit var binding: FragmentTransactionBinding
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(
            inflater, R.layout.fragment_transaction,container,false
        )
        binding.tVmodel = viewModel
        binding.lifecycleOwner = this
        val args: TransactionFragmentArgs by navArgs()
        val cat_id: Int = args.id
        viewModel.setCatId(cat_id)
        if (cat_id!=-1){
            viewModel.getClickedCategory(cat_id)
        }
        val adapter = TransactionAdapter(
            this.requireContext(),
            TransaksiClickListener {
            },
            TransaksiLongClickListener {
                viewModel.getClickedTransTab(it)
                showOptionDialog(it)
            }
        )
        binding.listTransaksi.adapter = adapter

        val adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, resources.getStringArray(R.array.tipe_list_all))
        adapter2.setDropDownViewResource(R.layout.spinner_item_layout)
        binding.spinnerTipeT.adapter = adapter2
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

        val adapter3 = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, resources.getStringArray(R.array.bulan))
        adapter3.setDropDownViewResource(R.layout.spinner_item_layout)
        binding.spinnerBulan.adapter = adapter3


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

        viewModel.selectedTipeSpinner.observe(viewLifecycleOwner) { value ->
            // Handle the selected value
            viewModel.getKategoriEntries(value)
        }
        viewModel.selectedBulanSpinner.observe(viewLifecycleOwner) {
            // Handle the selected value
            viewModel.updateRv4()
        }
        viewModel.selectedStartDate.observe(viewLifecycleOwner) {}
        viewModel.clicked_transtab.observe(viewLifecycleOwner) {}
        viewModel.selectedEndDate.observe(viewLifecycleOwner) {
            viewModel.updateRv4()
        }
        viewModel.is_date_picker_clicked.observe(viewLifecycleOwner) {
            if (it == true) {
                showDatePickerDialog()
                viewModel.onDatePickerClicked()
            }
        }

        viewModel.kategori_entries.observe(viewLifecycleOwner) { entries ->
            val adapter1 = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, entries)
            adapter1.setDropDownViewResource(R.layout.spinner_item_layout)
            binding.spinnerKategori.adapter = adapter1
            //val catname = viewModel._clicked_category.value?.category_name
            //val defaultPosition = entries.indexOf(catname)
            //val cattype = viewModel._clicked_category.value!!.category_type
           // binding.spinnerTipeT.setSelection(resources.getStringArray(R.array.tipe_list).indexOf(cattype))
           // binding.spinnerKategori.setSelection(defaultPosition)
        }

        viewModel.recyclerViewData.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.filter_trans_sum.observe(viewLifecycleOwner){}

        viewModel.selectedKategoriSpinner.observe(viewLifecycleOwner) {
            // Handle the selected value
            viewModel.updateRv4()
        }
        viewModel.navigate_to_input.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController().navigate(
                    TransactionFragmentDirections.actionTransactionFragmentToInputFragment3(it)
                )
                viewModel.onNavigatedToInout()
            }
        }
        return binding.root

    }
    @RequiresApi(Build.VERSION_CODES.O)
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission if needed
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Update the filter with the new search query
                viewModel.filterData(newText)
                return true
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)



        // Trigger loading the spinner entries
        viewModel.getKategoriEntries("PENGELUARAN")
        //viewModel.updateRecyclerViewData("BEAUTY")
        viewModel.updateRv4()
    }




}