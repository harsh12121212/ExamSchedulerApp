package com.app.examschedulerapp.Student.studentViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.Student.studentModel.student
import com.app.examschedulerapp.repository.UserRepository
import com.app.examschedulerapp.repository.UserRepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StudentProfileViewModel : ViewModel() {
    private lateinit var userRepository: UserRepositoryInterface
    val studentData: MutableLiveData<student> = MutableLiveData()

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