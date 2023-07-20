package com.example.budgettracker2.category

import android.app.AlertDialog
import android.content.DialogInterface
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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.budgettracker2.R
import com.example.budgettracker2.adapterp.CategoryAdapter
import com.example.budgettracker2.adapterp.CategoryClickListener
import com.example.budgettracker2.adapterp.CategoryLongClickListener
import com.example.budgettracker2.databinding.FragmentCategoryBinding
import com.example.budgettracker2.databinding.PopUpAddCategoryBinding
import com.example.budgettracker2.viewModels.HSViewModel


class CategoryFragment : Fragment() {

    private val viewModel: HSViewModel by viewModels { HSViewModel.Factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding:FragmentCategoryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_category,container,false)
        val application = requireNotNull(this.activity).application
        binding.hsVmodel = viewModel
        binding.lifecycleOwner = this

        val adapter =CategoryAdapter(
            this.requireContext(),
           CategoryClickListener {
               Toast.makeText(application,it.toString(),Toast.LENGTH_SHORT).show()

           }, CategoryLongClickListener {
                viewModel.getClickedCategort(it)
               showOptionDialog(id)
            }
            )
        binding.listKategori.adapter = adapter
        val manager = GridLayoutManager(activity, 2)
        binding.listKategori.layoutManager = manager

        viewModel.kategori.observe(viewLifecycleOwner, Observer {
            it?.let {
                 adapter.submitList(it)
                //adapter.notifyDataSetChanged()
            }
        })
        viewModel.tm_income.observe(viewLifecycleOwner,Observer{})
        viewModel.tm_spend.observe(viewLifecycleOwner,Observer{})
        viewModel.selected_tipe.observe(viewLifecycleOwner,Observer{})
        viewModel.selected_color_ac.observe(viewLifecycleOwner, Observer {})
        viewModel.clicked_category.observe(viewLifecycleOwner, Observer {})
        viewModel.is_ac_dialog_show.observe(viewLifecycleOwner, Observer {
            if (it==true){
                showACDialog(-1)
                viewModel.onAddCategoryClicked()
            }
        })
        viewModel.c.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.i("UPDATEC","all "+it.toString())
            }
        })
        viewModel.navigate_to_transaction.observe(viewLifecycleOwner, Observer {
            it?.let {
                // this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragment2ToTransactionFragment(it))
                this.findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToTransactionFragment(it))
                viewModel.onNavigatedToTransaction()
            }
        })
        viewModel.navigate_to_input.observe(viewLifecycleOwner, Observer {
            if (it==true){
                // this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragment2ToInputFragment())
                this.findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToInputFragment(-1))
                viewModel.onNavigatedToInout()
            }
        })

        return binding.root
    }

    private fun showACDialog(code:Int) {

        val binding: PopUpAddCategoryBinding =
            PopUpAddCategoryBinding.inflate(LayoutInflater.from(requireContext()))
        binding.acVmodel = viewModel
        val adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, resources.getStringArray(R.array.tipe_list))
        adapter2.setDropDownViewResource(R.layout.spinner_item_layout)
        binding.spinnerTipeAc.adapter = adapter2
        val adapter3 = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, resources.getStringArray(R.array.color_list))
        adapter3.setDropDownViewResource(R.layout.spinner_item_layout)
        binding.spinnerColorAc.adapter = adapter3
        if (code!=-1){
            val c = viewModel.clicked_category.value
            if (c != null) {
                viewModel._kategori_name_ac.value = c.category_name ?: ""
                binding.spinnerTipeAc.setSelection(resources.getStringArray(R.array.tipe_list).indexOf(c.category_type))
                binding.spinnerColorAc.setSelection(resources.getStringArray(R.array.color_list).indexOf(c.category_color))
            }
        }
        binding.spinnerTipeAc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Update the selected value in your ViewModel
                viewModel.setSelectedTipeValue(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        binding.spinnerColorAc.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    viewModel.setSelectedColorValue(selectedItem)
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        val dialog = AlertDialog.Builder(requireContext()).setTitle("Add Item").setView(binding.root)
            .setPositiveButton("OK") { _, _ ->
                if (code!=-1){ viewModel.updateCategory() }else {viewModel.saveNewCategotry() }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()

    }
    private fun showOptionDialog(id:Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Options")
        dialogBuilder.setMessage("Choose an action:")
        dialogBuilder.setPositiveButton("Update") { dialogInterface: DialogInterface, _: Int ->
            showACDialog(id)
            dialogInterface.dismiss()
        }
        dialogBuilder.setNegativeButton("Delete") { dialogInterface: DialogInterface, _: Int ->
            Toast.makeText(requireContext(),id.toString(),Toast.LENGTH_SHORT).show()
            viewModel.deleteCategory(id)
            dialogInterface.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}