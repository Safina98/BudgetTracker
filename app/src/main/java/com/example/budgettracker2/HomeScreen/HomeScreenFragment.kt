package com.example.budgettracker2.HomeScreen

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.budgettracker2.R
//import com.example.budgettracker2.database.BudgetDatabase
import com.example.budgettracker2.databinding.FragmentHomeScreenBinding
import com.example.budgettracker2.databinding.PopUpAddCategoryBinding
import com.example.budgettracker2.viewModels.HSViewModel
import com.example.budgettracker2.viewModels.MainViewModel
import java.util.Calendar


class HomeScreenFragment : Fragment() {

    private val viewModel: HSViewModel by viewModels { HSViewModel.Factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentHomeScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home_screen, container, false)
        val application = requireNotNull(this.activity).application
      //  val dataSource = BudgetDatabase.getInstance(application).kategori_dao
    //  val dataSource2 = BudgetDatabase.getInstance(application).transaksi_dao

       binding.hsVmodel = viewModel
        binding.lifecycleOwner = this
        val manager = GridLayoutManager(activity, 2)
        binding.listKategori.layoutManager = manager
        val adapter = HomeScreenAdapter(
            this.requireContext(),
            KategoriListener {
                viewModel.onNavigateToTransaction(it)
            }
            )
        binding.listKategori.adapter = adapter

        viewModel.kategori.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.selected_tipe.observe(viewLifecycleOwner,Observer{

        })
        viewModel.selected_color_ac.observe(viewLifecycleOwner, Observer {

        })

        viewModel.is_ac_dialog_show.observe(viewLifecycleOwner, Observer {
            if (it==true){
                showACDialog()
                viewModel.onAddCategoryClicked()
            }
        })
        viewModel.navigate_to_transaction.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToTransactionFragment(it))
                viewModel.onNavigatedToTransaction()
            }
        })
        viewModel.navigate_to_input.observe(viewLifecycleOwner, Observer {
            if (it==true){
                this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToInputFragment())
                viewModel.onNavigatedToInout()
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }
    fun showACDialog(){

        val binding: PopUpAddCategoryBinding = PopUpAddCategoryBinding.inflate(LayoutInflater.from(requireContext()))
        binding.acVmodel = viewModel
        binding.spinnerTipeAc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Update the selected value in your ViewModel
                viewModel.setSelectedTipeValue(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

        }}
        binding.spinnerColorAc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Update the selected value in your ViewModel
                viewModel.setSelectedColorValue(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when nothing is selected
            }
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Item")
            .setView(binding.root)
            .setPositiveButton("OK") { _, _ ->
                viewModel.saveNewCategotry()

                // Perform any necessary actions with the selected date range
                // Update your ViewModel or perform any other logic

            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()

    }



    }
