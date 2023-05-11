package com.app.examschedulerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.DBConstants.ADMIN
import com.app.examschedulerapp.data.LoggedInUser
import com.app.examschedulerapp.data.admin
import com.app.examschedulerapp.data.student
import com.app.examschedulerapp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    //for current user direct entry
    var user = FirebaseAuth.getInstance().currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()

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
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    binding.progressbar.visibility = View.GONE
                    if (it.isSuccessful) {
                        checkForUserType()
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

    fun checkForUserType() {
        binding.progressbar.visibility = View.VISIBLE
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference(
                DBConstants.USERS
            ).child(it)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        binding.progressbar.visibility = View.GONE
                        try {
                            val admin = p0.getValue(admin::class.java)
                            if (admin?.type == ADMIN) {
                                LoggedInUser.admin = admin
                                findNavController().navigate(R.id.action_loginFragment_to_adminFragment)
                            } else {
                                LoggedInUser.student = p0.getValue(student::class.java)!!
                                val i = Intent(activity, StudentMainActivity :: class.java )
                                startActivity(i)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                    override fun onCancelled(p0: DatabaseError) {
                        binding.progressbar.visibility = View.GONE

                    }

                })
        }
    }

    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}
