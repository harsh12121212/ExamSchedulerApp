package com.app.examschedulerapp.Student.studentViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.repository.ExamRepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StudentStatusViewModel(private val repository: ExamRepositoryInterface) : ViewModel() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    val examData: LiveData<List<examdata>> = repository.getExamData(getCurrentUser())

    private fun getCurrentUser(): String {
        return currentUser?.uid ?: ""
    }
}