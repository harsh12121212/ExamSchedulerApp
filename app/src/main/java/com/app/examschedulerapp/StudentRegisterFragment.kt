package com.app.examschedulerapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.data.student
import com.app.examschedulerapp.databinding.FragmentStudentRegisterBinding
import com.app.examschedulerapp.utils.listdata
import com.app.examschedulerapp.utils.location
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

const val PASSWORD_LENGTH_ERROR = "password must at least 6 characters long"
const val INVALID_DATA ="Please enter valid data"
const val INVALID_VALUE ="Please enter the data"

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

        //Location spinner
        val locarrayAdapter =
            activity?.let {ArrayAdapter(it, R.layout.spinnerlayout, location.values())}
        binding.etStudLoc.adapter=locarrayAdapter
        binding.etStudLoc.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        //Technology choice spinner
        val techarrayAdapter =
            activity?.let {ArrayAdapter(it, R.layout.spinnerlayout, listdata.values())}
        binding.etStudTechchoice.adapter=techarrayAdapter
        binding.etStudTechchoice.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // DOB Calender
        val c= Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DAY_OF_MONTH)
        // Button show to display date picker
        binding.etStudDob.setOnClickListener {
            val dob = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                binding.etStudDob.setText(""+ mDay+"/"+ mMonth+"/"+mYear)
            },year,month,day)
            dob.getDatePicker().setMaxDate(System.currentTimeMillis()-1000)
            //show dialogue
            dob.show()
        }
        // EXAM Date Calender
        binding.etStudExamdate.setOnClickListener {
            val exmdate = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { datePicker,mYear,mMonth, mDay ->
                binding.etStudExamdate.setText(""+ mDay+"/"+ mMonth+"/"+mYear)
            },year,month,day)
            exmdate.getDatePicker().setMinDate(System.currentTimeMillis()-1000)
            //show dialogue
            exmdate.show()
        }

        binding.btnSignin.setOnClickListener{
            validateData()
        }
    }

    private fun validateData() {
        name=binding.etStudName.text.toString().trim()
        email=binding.etStudEmail.text.toString().trim()
        dob=binding.etStudDob.text.toString().trim()
        education=binding.etStudEducation.text.toString().trim()
        technology=binding.etStudTechnology.text.toString().trim()
        exp=binding.etStudWorkexp.text.toString().trim()
        techchoice=binding.etStudTechchoice.selectedItem.toString().trim()
        loc=binding.etStudLoc.selectedItem.toString().trim()
        date=binding.etStudExamdate.text.toString().trim()
        password=binding.etStudPswd.text.toString().trim()
        type = "STUDENT"
        uid= FirebaseAuth.getInstance().currentUser?.uid.toString()

        if (TextUtils.isEmpty(name)) {
            binding.etStudName.error = INVALID_DATA
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // invalid email format
            binding.etStudEmail.error = INVALID_DATA
        } else if (password.length < 6) {
            binding.etStudPswd.error = PASSWORD_LENGTH_ERROR
        } else {
            //data is valid
            firebaseLogin(email, password)
        }
    }

    private fun firebaseLogin(email: String, password: String){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    findNavController().navigate(R.id.action_studentRegisterFragment_to_firstFragment)
                    showSnackBar("LoggedIn as $email")
                    saveData()
                } else {
                    showSnackBar("Login failed due to ${it.exception.toString()}")
                }
            }

    }

    private fun saveData() {

        if(name.isEmpty()) {
            binding.etStudName.error = "Please enter name"
        }
        else if(email.isEmpty()) {
            binding.etStudEmail.error = "Please enter email"
        }
        else if (dob.isEmpty()) {
            binding.etStudDob.error = "Please enter the data"
        }
        else if (education.isEmpty()) {
            binding.etStudEducation.error = "Please enter the data"
        }
        else if (technology.isEmpty()) {
            binding.etStudTechnology.error = "Please enter the data"
        }
        else if (exp.isEmpty()) {
            binding.etStudWorkexp.error = "Please enter the data"
        }
        else if (date.isEmpty()) {
            binding.etStudExamdate.error = "Please enter the data"
        }
        else if (password.isEmpty()) {
            binding.etStudPswd.error = "Please enter the data"
        }else {
            val studentData = student(
                name,
                email,
                dob,
                education,
                technology,
                exp,
                techchoice,
                loc,
                date,
                password,
                type,
                uid
            )

            dbRef.child(name).setValue(studentData)
                .addOnCompleteListener {
                    // To clear the entry
//                    binding.etStudName.editableText.clear()
//                    binding.etStudEmail.editableText.clear()
//                    binding.etStudDob.editableText.clear()
//                    binding.etStudEducation.editableText.clear()
//                    binding.etStudTechnology.editableText.clear()
//                    binding.etStudWorkexp.editableText.clear()
//                    binding.etStudTechchoice.editableText.clear()
//                    binding.etStudLoc.editableText.clear()
//                    binding.etStudExamdate.editableText.clear()
//                    binding.etStudPswd.editableText.clear()
                    showSnackBar("Account created successfully")
                }.addOnFailureListener { err ->
                    showSnackBar("Error${err.message}")
                }
        }
    }
    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}