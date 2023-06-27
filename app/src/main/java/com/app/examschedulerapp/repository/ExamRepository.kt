package com.app.examschedulerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.examschedulerapp.data.examdata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/* Handles data operations related to exams, such as fetching, creating, updating,
and deleting exams from firebase*/
class  ExamRepository : ExamRepositoryInterface {
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("APPLICATION")

    override fun saveExamData(examData: examdata, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        dbRef.push().setValue(examData)
            .addOnCompleteListener {
                onSuccess()
            }
            .addOnFailureListener { err ->
                onFailure("Error: ${err.message}")
            }
    }

    override fun getExamData(studentId: String): LiveData<List<examdata>> {
        val examData = MutableLiveData<List<examdata>>()

        dbRef.orderByChild("studentId").equalTo(studentId)
            .addValueEventListener(object : ValueEventListener {
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
                    examData.value = list
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })

        return examData
    }
}