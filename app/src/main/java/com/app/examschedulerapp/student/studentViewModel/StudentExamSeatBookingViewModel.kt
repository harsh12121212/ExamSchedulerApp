package com.app.examschedulerapp.student.studentViewModel

import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.data.Examdata
import com.app.examschedulerapp.data.LoggedInUser.student
import com.app.examschedulerapp.repository.ExamRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentExamSeatBookingViewModel : ViewModel() {

    private val examRepository: ExamRepository = ExamRepository()
    private var countId = 0

    fun saveExamData(
        selectedCity: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        countId = 0
        checkExistingBookings {
            if (countId >= 2) {
                onFailure("You have already booked a seat twice.")
            } else {
                checkRequestStatus(selectedCity, onSuccess, onFailure)
            }
        }
    }

    private fun checkExistingBookings(callback: () -> Unit) {
        val userId = student.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("APPLICATION")
        dbRef.orderByChild("studentId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    countId = dataSnapshot.childrenCount.toInt()
                    callback()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun checkRequestStatus(
        selectedCity: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = student.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("APPLICATION")
        dbRef.orderByChild("studentId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var requestDeclined = false
                    for (snapshot in dataSnapshot.children) {
                        val examData = snapshot.getValue(Examdata::class.java)
                        if (examData?.status == "declined") {
                            requestDeclined = true
                            break
                        }
                    }
                    if (requestDeclined) {
                        countId = 0 // Reset the countId
                        checkCityAvailability(selectedCity, onSuccess, onFailure)
                    } else {
                        checkCityAvailability(selectedCity, onSuccess, onFailure)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun checkCityAvailability(
        selectedCity: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = student.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("APPLICATION")
        dbRef.orderByChild("studentId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var cityAlreadySubmitted = false
                    for (snapshot in dataSnapshot.children) {
                        val examData = snapshot.getValue(Examdata::class.java)
                        if (examData?.sf_city == selectedCity) {
                            cityAlreadySubmitted = true
                            break
                        }
                    }
                    if (cityAlreadySubmitted) {
                        onFailure("You have already submitted seat booking for this City")
                    } else {
                        onSuccess()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    fun saveSeatData(
        examData: Examdata,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        examRepository.saveExamData(examData, onSuccess, onFailure)
    }
}