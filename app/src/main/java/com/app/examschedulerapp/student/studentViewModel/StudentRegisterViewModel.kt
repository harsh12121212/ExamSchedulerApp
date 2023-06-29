package com.app.examschedulerapp.student.studentViewModel

import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.student.studentModel.Student
import com.app.examschedulerapp.repository.UserRepositoryInterface
import com.google.firebase.auth.FirebaseAuth


class StudentRegisterViewModel(private val userRepository: UserRepositoryInterface) : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()

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

    fun saveStudentData(
        studentData: Student,
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