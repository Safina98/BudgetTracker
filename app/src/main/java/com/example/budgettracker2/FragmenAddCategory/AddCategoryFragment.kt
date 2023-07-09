package com.example.budgettracker2.FragmenAddCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.budgettracker2.R
import com.example.budgettracker2.databinding.FragmentAddCategoryBinding

class AddCategoryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddCategoryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_category,container,false)

        return binding.root
    }

}