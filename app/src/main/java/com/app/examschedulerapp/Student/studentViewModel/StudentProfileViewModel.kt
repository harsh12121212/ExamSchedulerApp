package com.app.examschedulerapp.Student.studentViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.Student.studentModel.student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StudentProfileViewModel : ViewModel() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    val studentData: MutableLiveData<student> = MutableLiveData()

    fun fetchStudentData() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        val currentUserReference: DatabaseReference = database.child("Users").child(auth.currentUser!!.uid)
        currentUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val student = dataSnapshot.getValue(student::class.java)
                studentData.value = student!!
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}