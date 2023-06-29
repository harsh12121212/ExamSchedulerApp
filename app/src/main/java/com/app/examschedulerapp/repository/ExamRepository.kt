package com.app.examschedulerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.examschedulerapp.data.Examdata
import com.google.firebase.database.*

/* Handles data operations related to exams, such as fetching, creating, updating,
and deleting exams from firebase*/
class ExamRepository : ExamRepositoryInterface {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("APPLICATION")

    override fun saveExamData(examData: Examdata, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        dbRef.push().setValue(examData)
            .addOnCompleteListener {
                onSuccess()
            }
            .addOnFailureListener { err ->
                onFailure("Error: ${err.message}")
            }
    }

    override fun getExamData(studentId: String): LiveData<List<Examdata>> {
        val examData = MutableLiveData<List<Examdata>>()

        dbRef.orderByChild("studentId").equalTo(studentId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list: MutableList<Examdata> = mutableListOf()
                    if (dataSnapshot.exists()) {
                        for (childSnapshot in dataSnapshot.children) {
                            childSnapshot.getValue(Examdata::class.java)?.let {
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
    } override fun getPendingExamData(callback: (List<Examdata>) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val examDataList = mutableListOf<Examdata>()
                for (snapshot in dataSnapshot.children) {
                    val examData = snapshot.getValue(Examdata::class.java)
                    examData?.let {
                        examDataList.add(it)
                    }
                }
                callback(examDataList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled if needed
            }
        })
    }

    override fun updateStatus(item: Examdata, status: String, callback: (Boolean) -> Unit) {
        val query = dbRef.orderByChild("sf_centre").equalTo(item.sf_centre)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key
                    val data = snapshot.getValue(Examdata::class.java)
                    if (data != null && data == item) {
                        val updateData = HashMap<String, Any>()
                        updateData["status"] = status
                        if (status == "Declined") {
                            updateData["countid"] = 0
                        }
                        dbRef.child(key!!).updateChildren(updateData)
                            .addOnSuccessListener {
                                callback.invoke(true)
                            }
                            .addOnFailureListener {
                                callback.invoke(true)
                            }
                        break
                    }
                }
                callback.invoke(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled if needed
            }
        })
    }

    override fun getExams(callback: (List<Examdata>) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val exams: MutableList<Examdata> = mutableListOf()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(Examdata::class.java)?.let {
                        exams.add(it)
                    }
                }
                callback(exams)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
