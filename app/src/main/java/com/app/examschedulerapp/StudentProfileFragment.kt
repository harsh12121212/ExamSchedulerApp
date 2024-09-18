package com.app.examschedulerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.examschedulerapp.data.student
import com.app.examschedulerapp.databinding.FragmentStudentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StudentProfileFragment : Fragment() {

    private lateinit var binding: FragmentStudentProfileBinding
    private var dataBase: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        
        auth = FirebaseAuth.getInstance()

        dataBase = FirebaseDatabase.getInstance().reference
        fun currentUserReference(): DatabaseReference = dataBase!!.child("Users").child(auth.currentUser!!.uid)
        currentUserReference().addValueEventListener(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot){
                val student=dataSnapshot.getValue(student::class.java)

                binding.tvStudentprofileStudentname.text ="Name : "+ student!!.name
                binding.tvStudentprofileStudentemail.text ="Email : "+ student.email
                binding.tvStudentprofileStudentdob.text ="Birthdate : "+ student.dob
                binding.tvStudentprofileStudenteducation.text ="Education : "+ student.education
                binding.tvStudentprofileStudenttechnologytraining.text ="Technical Trainings : "+ student.technology
                binding.tvStudentprofileStudentworkexp.text ="Work Experience : "+ student.exp +"years"
                binding.tvStudentprofileStudenttechchoice.text ="Technology Choosen : "+ student.techchoice
                binding.tvStudentprofileStudentcity.text ="Preferred City of Exam : "+ student.loc

            }
            override fun onCancelled(error: DatabaseError){
            }
        })
    return binding.root
    }
}