package com.app.examschedulerapp.repository

import com.app.examschedulerapp.data.examdata
import com.google.firebase.database.*

/* Handles data operations related to exams, such as fetching, creating, updating,
and deleting exams from firebase*/
class  ExamRepository {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("APPLICATION")

    fun saveExamData(examData: examdata, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        dbRef.push().setValue(examData)
            .addOnCompleteListener {
                onSuccess()
            }
            .addOnFailureListener { err ->
                onFailure("Error: ${err.message}")
            }
    }
}
