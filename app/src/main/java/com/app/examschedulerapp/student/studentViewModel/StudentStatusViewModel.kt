package com.app.examschedulerapp.student.studentViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.data.Examdata
import com.app.examschedulerapp.repository.ExamRepositoryInterface
import com.google.firebase.auth.FirebaseAuth

class StudentStatusViewModel(repository: ExamRepositoryInterface) : ViewModel() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    val examData: LiveData<List<Examdata>> = repository.getExamData(getCurrentUser())

    private fun getCurrentUser(): String {
        return currentUser?.uid ?: ""
    }
}