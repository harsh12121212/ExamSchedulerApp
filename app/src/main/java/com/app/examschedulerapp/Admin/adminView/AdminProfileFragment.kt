package com.app.examschedulerapp.Admin.adminView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.examschedulerapp.Admin.adminViewModel.AdminProfileViewModel
import com.app.examschedulerapp.databinding.FragmentAdminProfileBinding


class AdminProfileFragment : Fragment() {
    private lateinit var binding: FragmentAdminProfileBinding
    private val viewModel: AdminProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false)

        viewModel.adminData.observe(viewLifecycleOwner) { admin ->
            admin?.let {
                binding.tvAdminprofileAdminname.text = "Name: ${admin.name}"
                binding.tvAdminprofileAdminemail.text = "Email: ${admin.email}"
                binding.tvAdminprofileAdmincity.text = "City of the center: ${admin.city}"
                binding.tvAdminprofileAdmincenter.text = "Centre name: ${admin.centre}"
            }
        }
        return binding.root
    }
}