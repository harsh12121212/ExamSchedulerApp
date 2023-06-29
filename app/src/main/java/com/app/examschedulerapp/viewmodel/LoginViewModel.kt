package com.app.examschedulerapp.viewmodel

import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.admin.adminmodel.Admin
import com.app.examschedulerapp.student.studentModel.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    /**
     * Signs in the user with the provided email and password using FirebaseAuth.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @param onCompleteListener Callback called when the sign-in operation is complete.
     *                           The boolean parameter indicates whether the operation was successful or not.
     */
    fun signInWithEmailAndPassword(email: String, password: String, onCompleteListener: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            onCompleteListener(task.isSuccessful)
        }
    }

    /**
     * Checks the user's type (Admin or student) based on the user ID.
     * Retrieves the user data from the Firebase Realtime Database.
     *
     * @param uid The user ID.
     * @param onDataSuccessListener Callback called when the user data is retrieved.
     *                              The Admin and student parameters may be null if the data is not found.
     */
    fun checkForUserType(uid: String, onDataSuccessListener: (admin: Admin?, student: Student?) -> Unit) {
        val usersRef = firebaseDatabase.getReference(DBConstants.USERS).child(uid)
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val admin = snapshot.getValue(Admin::class.java)
                val student = snapshot.getValue(Student::class.java)
                onDataSuccessListener(admin, student)
            }

            override fun onCancelled(error: DatabaseError) {
                onDataSuccessListener(null, null)
            }
        })
    }
    /**
     * Retrieves the current user's UID (User ID) from FirebaseAuth.
     *
     * @return The current user's UID, or null if the user is not authenticated.
     */
    fun getCurrentUserUID(): String? {
        return firebaseAuth.currentUser?.uid
    }
}