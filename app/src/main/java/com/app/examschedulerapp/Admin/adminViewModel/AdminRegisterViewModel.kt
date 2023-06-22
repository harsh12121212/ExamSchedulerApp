package com.app.examschedulerapp.Admin.adminViewModel

import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.Admin.adminModel.admin
import com.app.examschedulerapp.Admin.adminModel.City
import com.app.examschedulerapp.data.DBConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminRegisterViewModel : ViewModel() {
    internal lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    fun firebaseCreateAccount(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(it.exception.toString())
                }
            }
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
        databaseReference = FirebaseDatabase.getInstance().getReference(DBConstants.USERS)
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
            secondslot,
        )

        uid?.let {
            databaseReference.child(it).setValue(adminData).addOnCompleteListener {
                onSuccess()
            }.addOnFailureListener { err ->
                onFailure("Error: ${err.message}")
            }
            when (city) {
                "Banglore" -> {
                    FirebaseDatabase.getInstance().getReference("CenterData").child("Banglore").child(centre)
                        .setValue(centreData).addOnCompleteListener {
                            onSuccess()
                        }
                }
                "Chennai" -> {
                    FirebaseDatabase.getInstance().getReference("CenterData").child("Chennai").child(centre)
                        .setValue(centreData).addOnCompleteListener {
                            onSuccess()
                        }
                }
                "Hyderabad" -> {
                    FirebaseDatabase.getInstance().getReference("CenterData").child("Hyderabad").child(centre)
                        .setValue(centreData).addOnCompleteListener {
                            onSuccess()
                        }
                }
                else -> {
                    onFailure("ERROR!!!")
                }
            }
        }
    }
}