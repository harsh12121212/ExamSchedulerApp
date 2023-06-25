package com.app.examschedulerapp.Student.studentViewModel

import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.Student.studentModel.student
import com.app.examschedulerapp.repository.UserRepository
import com.app.examschedulerapp.repository.UserRepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StudentRegisterViewModel(private val userRepository: UserRepositoryInterface) : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    constructor() : this(UserRepository())

    /**
     * Creates a new user account with the provided email and password using FirebaseAuth.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @param onCompleteListener Callback called when the account creation is complete.
     *                           The boolean parameter indicates whether the operation was successful or not.
     */
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onCompleteListener: (Boolean) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onCompleteListener(task.isSuccessful)
            }
    }

    /**
     * Saves the student data to the Firebase Realtime Database.
     *
     * @param studentData The student data to be saved.
     * @param onCompleteListener Callback called when the data saving is complete.
     *                           The boolean parameter indicates whether the operation was successful or not.
     */
    fun saveStudentData(
        studentData: student,
        onCompleteListener: (Boolean) -> Unit
    ) {
        firebaseAuth.currentUser?.uid?.let { uid ->
            studentData.uid = uid // Set the UID
            userRepository.saveStudentData(studentData, onCompleteListener)
        } ?: run {
            onCompleteListener(false)
        }
    }
}