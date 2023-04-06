package com.app.examschedulerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.examschedulerapp.databinding.FragmentHomeBinding
import com.app.examschedulerapp.databinding.FragmentStudentRegisterBinding

class StudentRegisterFragment : Fragment() {

    private lateinit var binding: FragmentStudentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStudentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


}