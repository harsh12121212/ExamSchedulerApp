package com.app.examschedulerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.examschedulerapp.databinding.FragmentAdminDashboardBinding
import com.app.examschedulerapp.databinding.FragmentAdminProfileBinding

class AdminDashboardFragment : Fragment() {

    private lateinit var binding: FragmentAdminDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false)
    }
}