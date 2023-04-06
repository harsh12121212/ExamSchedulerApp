package com.app.examschedulerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.examschedulerapp.databinding.FragmentAdminRegisterBinding
import com.app.examschedulerapp.databinding.FragmentStudentRegisterBinding

class AdminRegisterFragment : Fragment() {

    private lateinit var binding: FragmentAdminRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

}