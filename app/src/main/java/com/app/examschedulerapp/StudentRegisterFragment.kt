package com.app.examschedulerapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.databinding.FragmentHomeBinding
import com.app.examschedulerapp.databinding.FragmentStudentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class StudentRegisterFragment : Fragment() {

    private lateinit var binding: FragmentStudentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStudentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignin.setOnClickListener{
            validateData()
        }

    }

    private fun validateData() {
        val email = binding.etStudEmail.text.toString()
        val pswd = binding.etStudPswd.text.toString()

        if( email.isNotEmpty() && pswd.isNotEmpty()) {
            Log.d("register", "Email : $email, Password : $pswd")
            firebaseAuth.createUserWithEmailAndPassword(email, pswd)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_studentRegisterFragment_to_firstFragment)
                        showSnackBar("LoggedIn as $email")
                        Log.d("register", "Success runs")
                    } else {
                        showSnackBar("Login failed due to ${it.exception.toString()}")
                    }
                }
        }else{
            showSnackBar("Fields cannot be empty")
        }
    }
    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}