package com.app.examschedulerapp.Student.studentViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.examdata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StudentStatusViewModel : ViewModel() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference(
        DBConstants.APPLICATION)
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private val _examData = MutableLiveData<List<examdata>>()
    val examData: LiveData<List<examdata>>
        get() = _examData

    fun init() {
        fetchData()
    }

    private fun fetchData() {
        databaseReference.orderByChild("studentId").equalTo(currentUser).addValueEventListener(
            object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list: MutableList<examdata> = mutableListOf()
                if (dataSnapshot.exists()) {
                    for (childSnapshot in dataSnapshot.children) {
                        childSnapshot.getValue(examdata::class.java)?.let {
                            list.add(it)
                            println("TAG onDataChange: $it")
                        }
                    }
                }
                _examData.value = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }
}