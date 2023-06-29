package com.app.examschedulerapp.student.studentViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.student.studentModel.Student
import com.app.examschedulerapp.repository.UserRepositoryInterface
import com.google.firebase.auth.FirebaseAuth

class StudentProfileViewModel : ViewModel() {
    private lateinit var userRepository: UserRepositoryInterface
    val studentData: MutableLiveData<Student> = MutableLiveData()

    fun setUserRepository(userRepository: UserRepositoryInterface) {
        this.userRepository = userRepository
    }

    fun fetchStudentData() {
        userRepository.fetchStudentData(getCurrentUserId(), studentData)
    }

    private fun getCurrentUserId(): String {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        return firebaseUser?.uid ?: ""
    }
}