package com.app.examschedulerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.examschedulerapp.databinding.FragmentAdminProfileBinding
import com.app.examschedulerapp.databinding.FragmentStudentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class AdminProfileFragment : Fragment() {

    private lateinit var binding: FragmentAdminProfileBinding
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false)

        return binding.root
    }
}