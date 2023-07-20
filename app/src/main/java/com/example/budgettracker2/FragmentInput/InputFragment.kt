package com.example.budgettracker2.FragmentInput

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgettracker2.R
import com.example.budgettracker2.databinding.FragmentInputBinding
import com.example.budgettracker2.viewModels.MainViewModel
import java.util.*


class InputFragment : Fragment() {
    private val viewModel: MainViewModel by  activityViewModels { MainViewModel.Factory }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding :FragmentInputBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_input,container,false)
        binding.iVmodel = viewModel
        binding.lifecycleOwner = this
        val args: InputFragmentArgs by navArgs()
        val transId: Int = args.transId
        viewModel.setTransId(transId)
       // Toast.makeText(context,transId.toString(),Toast.LENGTH_SHORT).show()
        if (transId!=-1){
            viewModel.getClickedTransTab(transId)
            //viewModel.setValueForUpdate()
        }else{
            viewModel.resetValue()
        }
        val adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, resources.getStringArray(R.array.tipe_list))
        adapter2.setDropDownViewResource(R.layout.spinner_item_layout)
        binding.spinnerTipe.adapter = adapter2
        binding.spinnerTipe.setSelection(resources.getStringArray(R.array.tipe_list).indexOf(viewModel._clicked_category.value?.category_type))
        binding.spinnerTipe.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                viewModel.setSelectedTipeValue(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        binding.spinnerKategoriI.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                viewModel.setSelectedKategoriValue(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        viewModel.selectedTipeSpinner.observe(viewLifecycleOwner, Observer { value ->
            viewModel.getKategoriEntries(value)
        })
        viewModel.kategori_entries.observe(viewLifecycleOwner, Observer { entries ->
            val adapter1 = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, entries)
            adapter1.setDropDownViewResource(R.layout.spinner_item_layout)
            binding.spinnerKategoriI.adapter = adapter1
            val catname = viewModel._clicked_category.value?.category_name
            val defaultPosition = entries.indexOf(catname)
            Toast.makeText(context,catname+"",Toast.LENGTH_SHORT).show()
            binding.spinnerKategoriI.setSelection(defaultPosition)
        })
        viewModel.selectedKategoriSpinner.observe(viewLifecycleOwner, Observer { value -> })
        viewModel.semuatabeltransaksi.observe(viewLifecycleOwner, Observer{it?.let {} })

         viewModel.navigate_to_toHomeScreen.observe(viewLifecycleOwner, Observer {if(it==true){
             this.findNavController().navigate(InputFragmentDirections.actionInputFragmentToCategoryFragment())
             viewModel.onNavigatedToHomeScreen()
         }
         })
        viewModel.navigate_to_transaction.observe(viewLifecycleOwner, Observer {if(it==-1){
            this.findNavController().navigate(InputFragmentDirections.actionInputFragmentToTransactionFragment(it))
            viewModel.onNavigatedToTransaction()
        }
        })
        viewModel.is_date_picker_clicked.observe(viewLifecycleOwner, Observer { if(it==true){
            showDatePickerDialog()
            viewModel.onDatePickerClicked()
        }
        })
        viewModel.selectedDate.observe(viewLifecycleOwner, { selectedDate -> })
        viewModel.clicked_transtab.observe(viewLifecycleOwner, { trans ->
            Toast.makeText(context,trans.toString(),Toast.LENGTH_SHORT).show()
          // viewModel.setValueForUpdate()
        })

        //viewModel.getCathegoryName(0)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Trigger loading the spinner entries

        //viewModel.updateRecyclerViewData("BEAUTY")

    }
    fun showDatePickerDialog(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth -> viewModel.setSelectedDate(year, monthOfYear, dayOfMonth) }, year, month, day)
        datePickerDialog.show()
    }






}