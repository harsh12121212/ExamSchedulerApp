package com.app.examschedulerapp.Admin.adminView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.examschedulerapp.Admin.adminModel.admin
import com.app.examschedulerapp.Admin.adminViewModel.AdminProfileViewModel
import com.app.examschedulerapp.databinding.FragmentAdminProfileBinding
import com.app.examschedulerapp.repository.UserRepository


class AdminProfileFragment : Fragment() {
    private lateinit var binding: FragmentAdminProfileBinding
    private lateinit var viewModel: AdminProfileViewModel
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        userRepository = UserRepository()
        viewModel = ViewModelProvider(this)[AdminProfileViewModel::class.java]
        viewModel.setUserRepository(userRepository)

        viewModel.adminData.observe(viewLifecycleOwner) { admin ->
            bindAdminData(admin)
        }

        viewModel.fetchAdminData()

        return binding.root
    }

    private fun bindAdminData(admin: admin?) {
        admin?.let {
            binding.tvAdminprofileAdminname.text = "Name: ${admin.name}"
            binding.tvAdminprofileAdminemail.text = "Email: ${admin.email}"
            binding.tvAdminprofileAdmincity.text = "City of the center: ${admin.city}"
            binding.tvAdminprofileAdmincenter.text = "Centre name: ${admin.centre}"
        }
    }
}

