package com.app.examschedulerapp.Admin

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.Student.studentView.INVALID_DATA
import com.app.examschedulerapp.Student.studentView.PASSWORD_LENGTH_ERROR
import com.app.examschedulerapp.R
import com.app.examschedulerapp.data.City
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.DBConstants.ADMIN
import com.app.examschedulerapp.data.admin
import com.app.examschedulerapp.databinding.FragmentAdminRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminRegisterFragment : Fragment() {

    var citylist = arrayOf("Select City", "Banglore", "Hyderabad", "Chennai")

    private lateinit var binding: FragmentAdminRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var name = ""
    private var email = ""
    private var city = ""
    private var centre = ""
    private var firstslot = ""
    private var secondslot = ""
    private var password = ""
    private var type = String()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        val cityarrayAdapter = activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, citylist) }
              binding.etAdminCity.setSelection(0)
        binding.etAdminCity.adapter = cityarrayAdapter
        binding.etAdminCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.btnSignin.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        name = binding.etAdminName.text.toString().trim()
        email = binding.etAdminEmail.text.toString().trim()
        city = binding.etAdminCity.selectedItem.toString().trim()
        centre = binding.etAdminCentre.text.toString().trim()
        firstslot = binding.etAdminSlotone.text.toString().trim()
        secondslot = binding.etAdminSlottwo.text.toString().trim()
        password = binding.etAdminPswd.text.toString().trim()
        type = ADMIN


        if (TextUtils.isEmpty(name)) {
            binding.etAdminName.error = INVALID_DATA
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etAdminEmail.error = INVALID_DATA
        } else if (password.length < 6) {
            binding.etAdminPswd.error = PASSWORD_LENGTH_ERROR
        } else if (centre.isEmpty()) {
            binding.etAdminCentre.error = "Please enter the data"
        } else if (firstslot.isEmpty()) {
            binding.etAdminSlotone.error = "Please enter the data"
        } else if (secondslot.isEmpty()) {
            binding.etAdminSlottwo.error = "Please enter the data"
        } else {
            //data is valid
            firebaseCreateAccount(email, password)
        }

    }

    private fun firebaseCreateAccount(email: String, password: String) {
        binding.progressbar.visibility = View.VISIBLE
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            binding.progressbar.visibility = View.GONE
            if (it.isSuccessful) {
                makeText(activity, "LoggedIn as $email", Toast.LENGTH_LONG).show()
                saveData()
            } else {
                makeText(
                    activity,
                    "Login failed due to ${it.exception.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun saveData() {
        databaseReference = FirebaseDatabase.getInstance().getReference(DBConstants.USERS)
        binding.progressbar.visibility = View.VISIBLE
        val adminData = admin(
            name,
            email,
            city,
            centre,
            firstslot,
            secondslot,
            password,
            type,
            FirebaseAuth.getInstance().currentUser?.uid
        )
        val centreData = City(
            city,
            centre,
            firstslot,
            secondslot,
        )

        FirebaseAuth.getInstance().currentUser?.uid?.let {
            databaseReference.child(it).setValue(adminData).addOnCompleteListener {
                binding.progressbar.visibility = View.GONE
                makeText(activity, "Account created successfully", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_adminRegisterFragment_to_adminFragment)
            }.addOnFailureListener { err ->
                makeText(activity, "Error${err.message}", Toast.LENGTH_LONG).show()
            }
            if (city == "Banglore") {
                FirebaseDatabase.getInstance().getReference("CenterData").child("Banglore").child(centre)
                    .setValue(centreData).addOnCompleteListener {
                        Log.d("Centredata", "Data is Saved!!!")
                    }
            } else if (city == "Chennai") {
                FirebaseDatabase.getInstance().getReference("CenterData").child("Chennai").child(centre)
                    .setValue(centreData).addOnCompleteListener {
                        Log.d("Centredata", "Data is Saved!!!")
                    }
            } else if (city == "Hyderabad") {
                FirebaseDatabase.getInstance().getReference("CenterData").child("Hyderabad").child(centre)
                    .setValue(centreData).addOnCompleteListener {
                        Log.d("Centredata", "Data is Saved!!!")
                    }
            } else {
                showSnackBar("ERROR!!!")
            }
        }
    }
    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

}