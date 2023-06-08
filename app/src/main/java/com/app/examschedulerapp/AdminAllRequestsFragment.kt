package com.app.examschedulerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.examschedulerapp.databinding.FragmentAdminAllRequestsBinding
import com.app.examschedulerapp.databinding.FragmentStudentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class AdminAllRequestsFragment : Fragment() {

    private lateinit var binding: FragmentAdminAllRequestsBinding
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_all_requests, container, false)
    }

}