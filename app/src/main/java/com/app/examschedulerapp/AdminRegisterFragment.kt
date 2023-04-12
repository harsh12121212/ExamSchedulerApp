package com.app.examschedulerapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.data.admin
import com.app.examschedulerapp.databinding.FragmentAdminRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminRegisterFragment : Fragment() {

    private lateinit var binding: FragmentAdminRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var name=""
    private var email=""
    private var city=""
    private var centre=""
    private var firstslot=""
    private var secondslot=""
    private var password=""
    private var type = String()
    private var uid=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        dbRef= FirebaseDatabase.getInstance().getReference("Admin")

        binding.btnSignin.setOnClickListener{
            validateData()
            saveData()
        }
    }

    private fun validateData() {
        val email = binding.etAdminEmail.text.toString()
        val pswd = binding.etAdminPswd.text.toString()

        if( email.isNotEmpty() && pswd.isNotEmpty()) {
            Log.d("register", "Email : $email, Password : $pswd")
            firebaseAuth.createUserWithEmailAndPassword(email, pswd)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_adminRegisterFragment_to_adminFragment)
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

    private fun saveData() {
        name=binding.etAdminName.text.toString().trim()
        email=binding.etAdminEmail.text.toString().trim()
        city=binding.etAdminCity.text.toString().trim()
        centre=binding.etAdminCentre.text.toString().trim()
        firstslot=binding.etAdminSlotone.text.toString().trim()
        secondslot=binding.etAdminSlottwo.text.toString().trim()
        password=binding.etAdminPswd.text.toString().trim()
        type = "ADMIN"
        uid= FirebaseAuth.getInstance().currentUser?.uid.toString()


        if(name.isEmpty()) {
            binding.etAdminName.error = "Please enter name"
        }
        if(email.isEmpty()) {
            binding.etAdminEmail.error = "Please enter email"
        }
            if (city.isEmpty()) {
                binding.etAdminCity.error = "Please enter the data"
            }
            if (centre.isEmpty()) {
                binding.etAdminCentre.error = "Please enter the data"
            }
            if (firstslot.isEmpty()) {
                binding.etAdminSlotone.error = "Please enter the data"
            }
            if (secondslot.isEmpty()) {
                binding.etAdminSlottwo.error = "Please enter the data"
            }
            if (password.isEmpty()) {
                binding.etAdminPswd.error = "Please enter the password"
            }

        val adminData = admin(name,email, city, centre, firstslot, secondslot, password, type, uid)

        dbRef.child(name).setValue(adminData)
            .addOnCompleteListener {
                // To clear the entry
                binding.etAdminName.editableText.clear()
                binding.etAdminEmail.editableText.clear()
                binding.etAdminCity.editableText.clear()
                binding.etAdminCentre.editableText.clear()
                binding.etAdminSlotone.editableText.clear()
                binding.etAdminSlottwo.editableText.clear()
                binding.etAdminPswd.editableText.clear()
                showSnackBar("Account created successfully")
            }.addOnFailureListener{ err->
                showSnackBar("Error${err.message}")
            }


    }

    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

}