package com.example.budgettracker2.FragmentInput

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

/*
        viewModel._tipe_position.observe(requireActivity(),
            object : Observer<Int> {
                override fun onChanged(t: Int?) {
                    //you will get the position on selection os spinner

                    viewModel.nama_kategori?.observe(viewLifecycleOwner, Observer {
                        it?.let {
                           // adapter.submitList(it)
                            //adapter.notifyDataSetChanged()
                          //  Toast.makeText(context,it.toString(),Toast.LENGTH_LONG).show()
                        }
                    })
                }})

 */
        viewModel._tipe_position.observe(viewLifecycleOwner, Observer{

        })
        viewModel._kategori_Position.observe(viewLifecycleOwner, Observer{

        })
        /*
        viewModel.semuatabeltransaksi.observe(viewLifecycleOwner, Observer{it?.let {
            Toast.makeText(context,it.toString(),Toast.LENGTH_LONG).show()
        }

        })

         */


        viewModel.nama_kategori?.observe(viewLifecycleOwner, Observer {
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
/*

        viewModel._tipe_position.observe(viewLifecycleOwner, Observer {
         //Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
            viewModel.getCathegoryName(0)
        })
        viewModel.nama_kategori.observe(viewLifecycleOwner, Observer {
            //Toast.makeText(context,it.toString(),Toast.LENGTH_LONG).show()


        })

 */

        /*viewModel.kategori.observe(viewLifecycleOwner, Observer {
            it?.let {

            }
        })

         */
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