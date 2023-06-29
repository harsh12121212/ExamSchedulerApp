package com.app.examschedulerapp.admin.adminViewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.admin.adminmodel.Admin
import com.app.examschedulerapp.repository.UserRepositoryInterface
import com.google.firebase.auth.FirebaseAuth

class AdminProfileViewModel : ViewModel() {
    private lateinit var userRepository: UserRepositoryInterface
    val adminData: MutableLiveData<Admin> = MutableLiveData()

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