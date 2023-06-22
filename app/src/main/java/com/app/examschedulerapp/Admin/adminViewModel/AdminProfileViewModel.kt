package com.app.examschedulerapp.Admin.adminViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.Admin.adminModel.admin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminProfileViewModel : ViewModel() {
    private val _adminData = MutableLiveData<admin>()
    val adminData: LiveData<admin> = _adminData

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        currentUserReference().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val admin = dataSnapshot.getValue(admin::class.java)
                _adminData.value = admin!!
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun currentUserReference(): DatabaseReference =
        database.child("Users").child(auth.currentUser!!.uid)
}