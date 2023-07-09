package com.example.budgettracker2.FragmentInput

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.findNavController
import com.example.budgettracker2.R
import com.example.budgettracker2.databinding.FragmentInputBinding
import com.example.budgettracker2.viewModels.MainViewModel
import java.util.*


class InputFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding :FragmentInputBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_input,container,false)
        binding.iVmodel = viewModel
        binding.lifecycleOwner = this

        binding.spinnerTipe.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Update the selected value in your ViewModel
                viewModel.setSelectedTipeValue(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when nothing is selected
            }
        }
        binding.spinnerKategoriI.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        viewModel.kategori_entries.observe(viewLifecycleOwner, Observer { entries ->
            val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, entries)
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerKategoriI.adapter = adapter1
        })
        viewModel.selectedKategoriSpinner.observe(viewLifecycleOwner, Observer { value ->
            // Handle the selected value
            Log.i("THEBUG",value.toString())
        })

        viewModel._tipe_position.observe(viewLifecycleOwner, Observer{

        })
        viewModel._kategori_Position.observe(viewLifecycleOwner, Observer{

        })

        viewModel.semuatabeltransaksi.observe(viewLifecycleOwner, Observer{it?.let {

        }

        })
        viewModel.kategoricobe.observe(viewLifecycleOwner, Observer{it?.let {
            Log.i("THEBUG",it.toString())
        }

        })
         viewModel.navigate_to_toHomeScreen.observe(viewLifecycleOwner, Observer {if(it==true){
             this.findNavController().navigate(InputFragmentDirections.actionInputFragmentToHomeScreenFragment())
             viewModel.onNavigatedToHomeScreen()
         }
         })


        viewModel.nama_kategori.observe(viewLifecycleOwner, Observer {
            if (it!=null) {
               // Toast.makeText(context,it.toString(),Toast.LENGTH_LONG).show()
            }
        })


        viewModel.is_date_picker_clicked.observe(viewLifecycleOwner, Observer { if(it==true){
            showDatePickerDialog()
            viewModel.onDatePickerClicked()
        }
        })
        viewModel.selectedDate.observe(viewLifecycleOwner, { selectedDate ->
            // Do something with the selected date
        })

        //viewModel.getCathegoryName(0)

        return binding.root
    }
    fun showDatePickerDialog(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                viewModel.setSelectedDate(year, monthOfYear, dayOfMonth)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }




}