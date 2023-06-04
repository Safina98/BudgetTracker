package com.example.budgettracker2.HomeScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.budgettracker2.R
//import com.example.budgettracker2.database.BudgetDatabase
import com.example.budgettracker2.databinding.FragmentHomeScreenBinding
import com.example.budgettracker2.viewModels.MainViewModel


class HomeScreenFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }
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
        binding.listKategori.adapter = adapter

        viewModel.kategori.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()

            }
        })
        /*
        viewModel.budget_rn.observe(viewLifecycleOwner, Observer {
            it?.let {

            }
        })

         */


        // Inflate the layout for this fragment
        return binding.root
    }


    }
