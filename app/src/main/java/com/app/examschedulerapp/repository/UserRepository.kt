package com.app.examschedulerapp.repository

import androidx.lifecycle.MutableLiveData
import com.app.examschedulerapp.Admin.adminModel.City
import com.app.examschedulerapp.Admin.adminModel.admin
import com.app.examschedulerapp.Student.studentModel.student
import com.app.examschedulerapp.data.DBConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserRepository : UserRepositoryInterface {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference(
        DBConstants.USERS)

    override fun saveStudentData(studentData: student, onCompleteListener: (Boolean) -> Unit) {
        databaseReference.child(firebaseAuth.currentUser!!.uid).setValue(studentData)
            .addOnCompleteListener { task ->
                onCompleteListener(task.isSuccessful)
            }
    }
    override fun fetchStudentData(userId: String, studentData: MutableLiveData<student>) {
        databaseReference.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val student = dataSnapshot.getValue(student::class.java)
                studentData.value = student!!
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }

    override fun createAdmin(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception.toString())
                }
            }
    }

    override fun saveAdminData(
        name: String,
        email: String,
        city: String,
        centre: String,
        firstslot: String,
        secondslot: String,
        password: String,
        type: String,
        uid: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val adminData = admin(
            name,
            email,
            city,
            centre,
            firstslot,
            secondslot,
            password,
            type,
            uid
        )
        val centreData = City(
            city,
            centre,
            firstslot,
            secondslot
        )

        uid?.let {
            databaseReference.child(it).setValue(adminData).addOnCompleteListener { userCreationTask ->
                if (userCreationTask.isSuccessful) {
                    when (city) {
                        "Banglore" -> {
                            FirebaseDatabase.getInstance().getReference("CenterData")
                                .child("Banglore").child(centre)
                                .setValue(centreData).addOnCompleteListener { centerCreationTask ->
                                    if (centerCreationTask.isSuccessful) {
                                        onSuccess()
                                    } else {
                                        onFailure("Error: ${centerCreationTask.exception?.message}")
                                    }
                                }
                        }
                        "Chennai" -> {
                            FirebaseDatabase.getInstance().getReference("CenterData")
                                .child("Chennai").child(centre)
                                .setValue(centreData).addOnCompleteListener { centerCreationTask ->
                                    if (centerCreationTask.isSuccessful) {
                                        onSuccess()
                                    } else {
                                        onFailure("Error: ${centerCreationTask.exception?.message}")
                                    }
                                }
                        }
                        "Hyderabad" -> {
                            FirebaseDatabase.getInstance().getReference("CenterData")
                                .child("Hyderabad").child(centre)
                                .setValue(centreData).addOnCompleteListener { centerCreationTask ->
                                    if (centerCreationTask.isSuccessful) {
                                        onSuccess()
                                    } else {
                                        onFailure("Error: ${centerCreationTask.exception?.message}")
                                    }
                                }
                        }
                        else -> {
                            onFailure("ERROR!!!")
                        }
                    }
                } else {
                    onFailure("Error: ${userCreationTask.exception?.message}")
                }
            }
        }
    }
    override fun fetchAdminData(userId: String, adminData: MutableLiveData<admin>) {
        databaseReference.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val admin = dataSnapshot.getValue(admin::class.java)
                adminData.value = admin!!
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}