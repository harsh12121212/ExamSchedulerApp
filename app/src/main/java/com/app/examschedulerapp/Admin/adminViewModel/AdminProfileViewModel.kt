package com.app.examschedulerapp.Admin.adminViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.Admin.adminModel.admin
import com.app.examschedulerapp.repository.UserRepositoryInterface
import com.google.firebase.auth.FirebaseAuth

class AdminProfileViewModel : ViewModel() {
    private lateinit var userRepository: UserRepositoryInterface
    val adminData: MutableLiveData<admin> = MutableLiveData()

    fun setUserRepository(userRepository: UserRepositoryInterface) {
        this.userRepository = userRepository
    }

    fun fetchAdminData() {
        userRepository.fetchAdminData(getCurrentUserId(), adminData)
    }

    private fun getCurrentUserId(): String {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        return firebaseUser?.uid ?: ""
    }
}