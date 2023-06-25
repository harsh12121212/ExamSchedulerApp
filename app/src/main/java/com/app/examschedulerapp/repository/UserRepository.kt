package com.app.examschedulerapp.repository

import androidx.lifecycle.MutableLiveData
import com.app.examschedulerapp.Student.studentModel.student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserRepository : UserRepositoryInterface {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun saveStudentData(studentData: student, onCompleteListener: (Boolean) -> Unit) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        val currentUserReference: DatabaseReference = database.child("Users").child(auth.currentUser!!.uid)
        currentUserReference.setValue(studentData)
            .addOnCompleteListener { task ->
                onCompleteListener(task.isSuccessful)
            }
    }
    override fun fetchStudentData(userId: String, studentData: MutableLiveData<student>) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        val currentUserReference: DatabaseReference = database.child("Users").child(userId)
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