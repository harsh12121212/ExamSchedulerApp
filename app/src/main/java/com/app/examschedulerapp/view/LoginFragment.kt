package com.app.examschedulerapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.admin.adminView.AdminMainActivity
import com.app.examschedulerapp.R
import com.app.examschedulerapp.student.studentView.StudentMainActivity
import com.app.examschedulerapp.viewmodel.LoginViewModel
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.LoggedInUser
import com.app.examschedulerapp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                binding.progressbar.visibility = View.VISIBLE

                viewModel.signInWithEmailAndPassword(email, password) { success ->
                    binding.progressbar.visibility = View.GONE
                    if (success) {
                        val uid = viewModel.getCurrentUserUID()
                        uid?.let {
                            viewModel.checkForUserType(it) { admin, student ->
                                if (admin != null && admin.type == DBConstants.ADMIN) {
                                    LoggedInUser.admin = admin
                                    val intent = Intent(activity, AdminMainActivity::class.java)
                                    startActivity(intent)
                                } else if (student != null) {
                                    LoggedInUser.student = student
                                    val intent = Intent(activity, StudentMainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    showSnackBar("User type not found")
                                }
                            }
                        }
                    } else {
                        showSnackBar("Login failed")
                    }
                }
            } else {
                showSnackBar("Please enter valid Email and Password")
            }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}