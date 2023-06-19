package com.app.examschedulerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.ViewModel.HomeViewModel
import com.app.examschedulerapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

//Fragment to give options of registeration and login to the user
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listeners for the UI components.
        binding.cvAdmin.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_adminRegisterFragment)
        }
        binding.cvStudent.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_studentRegisterFragment)
        }
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        // Perform sign-out when the view is created.
        viewModel.signOutCurrentUser()
    }
}