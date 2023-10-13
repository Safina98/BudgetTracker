package com.example.budgettracker2.category

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.budgettracker2.R
import com.example.budgettracker2.adapterp.CategoryAdapter
import com.example.budgettracker2.adapterp.CategoryClickListener
import com.example.budgettracker2.adapterp.CategoryLongClickListener
import com.example.budgettracker2.database.CategoryTable
import com.example.budgettracker2.database.TransactionTable
import com.example.budgettracker2.databinding.FragmentCategoryBinding
import com.example.budgettracker2.databinding.PopUpAddCategoryBinding
import com.example.budgettracker2.viewModels.HSViewModel
import java.io.BufferedReader
import java.io.InputStreamReader


class CategoryFragment : Fragment() {
    private val REQUEST_CODE_FILE_PICKER = 123
    private val viewModel: HSViewModel by viewModels { HSViewModel.Factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding:FragmentCategoryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_category,container,false)
        binding.hsVmodel = viewModel
        binding.lifecycleOwner = this
        val toolbar: Toolbar = binding.toolbarHs
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val adapter =CategoryAdapter(
            this.requireContext(),
           CategoryClickListener {
               viewModel.onNavigateToTransaction(it)
            //   Log.i("SPINNERPROB","Category fragment: id: "+it)
           }, CategoryLongClickListener {
                viewModel.getClickedCategort(it)
               showOptionDialog(id)
            }
            )
        binding.listKategori.adapter = adapter
        val manager = GridLayoutManager(activity, 2)
        binding.listKategori.layoutManager = manager

        viewModel.kategori.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
              //  Log.i("INSERTCSV","Categori fragment kategori list: "+ it)
                //adapter.notifyDataSetChanged()
            }
        }
        viewModel.tySpent.observe(viewLifecycleOwner) {}
        viewModel.tm_spend.observe(viewLifecycleOwner){}
        viewModel.selected_tipe.observe(viewLifecycleOwner){}
        viewModel.selected_color_ac.observe(viewLifecycleOwner){}
        viewModel.clicked_category.observe(viewLifecycleOwner){}
        viewModel.is_ac_dialog_show.observe(viewLifecycleOwner) {
            if (it==true){
                showACDialog(-1)
                viewModel.onAddCategoryClicked()
            }
        }

        viewModel.navigate_to_transaction.observe(viewLifecycleOwner){
            it?.let {
                this.findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToTransactionFragment(it))
                viewModel.onNavigatedToTransaction()
            }
        }
        viewModel.navigate_to_input.observe(viewLifecycleOwner) {
            if (it!=null){
                this.findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToInputFragment(it))
                viewModel.onNavigatedToInout()
            }
        }

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
                viewModel._kategori_name_ac.value = c.category_name
                binding.spinnerTipeAc.setSelection(resources.getStringArray(R.array.tipe_list).indexOf(c.category_type))
                binding.spinnerColorAc.setSelection(resources.getStringArray(R.array.color_list).indexOf(c.category_color))
            }
        }
        binding.spinnerTipeAc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
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
            viewModel.deleteCategory()
            dialogInterface.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)  // Indicates that this fragment has an options menu.
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "text/*" // Filter for CSV files
        startActivityForResult(intent, REQUEST_CODE_FILE_PICKER)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_FILE_PICKER && resultCode == Activity.RESULT_OK) {
            val selectedFileUri = data?.data
            if (selectedFileUri != null) {
                readCSVFile(selectedFileUri)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_import -> {
                // Handle menu item 1 click
                openFilePicker()
                return true
            }
            R.id.menu_export -> {
                // Handle menu item 2 click
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun readCSVFile(fileUri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(fileUri)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        var i  = 0
        while (reader.readLine().also { line = it } != null) {

            val tokens: List<String> = line!!.split(",")
            if (i!=0) {
                viewModel.insertCsv(tokens)
            }
            Log.i("INSERTCSV","Fragment: " +i )
            i+=1
        }
    }



     }