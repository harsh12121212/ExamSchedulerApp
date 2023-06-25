package com.app.examschedulerapp.Admin.adminViewModel

import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.Admin.adminModel.City
import com.app.examschedulerapp.Admin.adminModel.admin
import com.app.examschedulerapp.repository.UserRepository
import com.app.examschedulerapp.repository.UserRepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminRegisterViewModel : ViewModel() {
    internal val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository: UserRepositoryInterface = UserRepository()

    fun createUserAccount(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        userRepository.createAdmin(email, password, onSuccess, onFailure)
    }

    fun saveData(
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
        userRepository.saveAdminData(
            name, email, city, centre, firstslot, secondslot, password, type, uid,
            onSuccess = {
                saveCentreData(city, centre, firstslot, secondslot, onSuccess, onFailure)
            },
            onFailure = onFailure
        )
    }

    private fun saveCentreData(
        city: String,
        centre: String,
        firstslot: String,
        secondslot: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val centreData = City(city, centre, firstslot, secondslot)

        val centerRef = FirebaseDatabase.getInstance().getReference("CenterData").child(city)
        centerRef.child(centre).setValue(centreData)
            .addOnCompleteListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onFailure("Error: ${error.message}")
            }
    }
}