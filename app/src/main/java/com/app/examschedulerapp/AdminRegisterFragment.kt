package com.app.examschedulerapp

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
import com.app.examschedulerapp.data.admin
import com.app.examschedulerapp.databinding.FragmentAdminRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminRegisterFragment : Fragment() {

    var citylist = arrayOf("Select City","Banglore", "Hyderabad", "Chennai")

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
    private var firebasedb = Firebase.firestore

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


        val cityarrayAdapter =
            activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, citylist) }
        binding.etAdminCity.setSelection(0)
        binding.etAdminCity.adapter=cityarrayAdapter
        binding.etAdminCity.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.btnSignin.setOnClickListener{
            validateData()
        }
    }

    private fun validateData() {
        name=binding.etAdminName.text.toString().trim()
        email=binding.etAdminEmail.text.toString().trim()
        city=binding.etAdminCity.selectedItem.toString().trim()
        centre=binding.etAdminCentre.text.toString().trim()
        firstslot=binding.etAdminSlotone.text.toString().trim()
        secondslot=binding.etAdminSlottwo.text.toString().trim()
        password=binding.etAdminPswd.text.toString().trim()

        type = "ADMIN"
        val userMap = hashMapOf(
            "Centercity" to city,
            "ACenter" to centre
        )

        if (TextUtils.isEmpty(name)) {
            binding.etAdminName.error = INVALID_DATA
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // invalid email format
            binding.etAdminEmail.error = INVALID_DATA
        } else if (password.length < 6) {
            binding.etAdminPswd.error = PASSWORD_LENGTH_ERROR
        } else {
            //data is valid
            firebaseLogin(email, password)
            firebasedb.collection("Centre").document(centre).set(userMap)
                .addOnCompleteListener{
                    makeText(activity, "Data is saved", Toast.LENGTH_LONG).show()
                }
        }
    }
    private fun firebaseLogin(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    findNavController().navigate(R.id.action_adminRegisterFragment_to_adminFragment)
                    makeText(activity, "LoggedIn as $email", Toast.LENGTH_LONG).show()
                    saveData()
                } else {
                    makeText(activity, "Login failed due to ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveData() {
        uid= FirebaseAuth.getInstance().currentUser?.uid.toString()

        if(name.isEmpty()) {
            binding.etAdminName.error = "Please enter name"
        } else if(email.isEmpty()) {
            binding.etAdminEmail.error = "Please enter email"
        } else if (centre.isEmpty()) {
                binding.etAdminCentre.error = "Please enter the data"
        } else if (firstslot.isEmpty()) {
                binding.etAdminSlotone.error = "Please enter the data"
        } else if (secondslot.isEmpty()) {
                binding.etAdminSlottwo.error = "Please enter the data"
        } else if (password.isEmpty()) {
                binding.etAdminPswd.error = "Please enter the password"
        }else {

            val adminData =
                admin(name, email, city, centre, firstslot, secondslot, password, type, uid)

            dbRef.child(name).setValue(adminData)
                .addOnCompleteListener {
                    makeText(activity, "Account created successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { err ->
                    makeText(activity, "Error${err.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

}