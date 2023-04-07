package com.app.examschedulerapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.data.student
import com.app.examschedulerapp.databinding.FragmentStudentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StudentRegisterFragment : Fragment() {

    private lateinit var binding: FragmentStudentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var name =""
    private var email=""
    private var dob=""
    private var education=""
    private var technology=""
    private var exp=""
    private var techchoice=""
    private var loc=""
    private var date=""
    private var password=""
    private var type=String()
    private var uid =""

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

        dbRef= FirebaseDatabase.getInstance().getReference("Student")

        binding.btnSignin.setOnClickListener{
            validateData()
            saveData()
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

    private fun saveData() {
        name=binding.etStudName.text.toString().trim()
        email=binding.etStudEmail.text.toString().trim()
        dob=binding.etStudDob.text.toString().trim()
        education=binding.etStudEducation.text.toString().trim()
        technology=binding.etStudTechnology.text.toString().trim()
        exp=binding.etStudWorkexp.text.toString().trim()
        techchoice=binding.etStudTechchoice.text.toString().trim()
        loc=binding.etStudLoc.text.toString().trim()
        date=binding.etStudExamdate.text.toString().trim()
        password=binding.etStudPswd.text.toString().trim()
        type = "STUDENT"
        uid= FirebaseAuth.getInstance().currentUser?.uid.toString()


        if(name.isEmpty()) {
            binding.etStudName.error = "Please enter name"
        }
        if(email.isEmpty()) {
            binding.etStudEmail.error = "Please enter email"
        }
        if (dob.isEmpty()) {
            binding.etStudDob.error = "Please enter the data"
        }
        if (education.isEmpty()) {
            binding.etStudEducation.error = "Please enter the data"
        }
        if (technology.isEmpty()) {
            binding.etStudTechnology.error = "Please enter the data"
        }
        if (exp.isEmpty()) {
            binding.etStudWorkexp.error = "Please enter the data"
        }
        if (techchoice.isEmpty()) {
            binding.etStudTechchoice.error = "Please enter the data"
        }
        if (loc.isEmpty()) {
            binding.etStudWorkexp.error = "Please enter the data"
        }
        if (date.isEmpty()) {
            binding.etStudExamdate.error = "Please enter the data"
        }
        if (password.isEmpty()) {
            binding.etStudPswd.error = "Please enter the data"
        }
        val studentData = student(name,email,dob,education,technology,exp,techchoice,loc,date ,password,type,uid)

        dbRef.child(name).setValue(studentData)
            .addOnCompleteListener {
                // To clear the entry
                binding.etStudName.editableText.clear()
                binding.etStudEmail.editableText.clear()
                binding.etStudDob.editableText.clear()
                binding.etStudEducation.editableText.clear()
                binding.etStudTechnology.editableText.clear()
                binding.etStudWorkexp.editableText.clear()
                binding.etStudTechchoice.editableText.clear()
                binding.etStudLoc.editableText.clear()
                binding.etStudExamdate.editableText.clear()
                binding.etStudPswd.editableText.clear()
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