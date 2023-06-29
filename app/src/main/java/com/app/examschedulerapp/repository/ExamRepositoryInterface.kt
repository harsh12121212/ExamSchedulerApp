package com.app.examschedulerapp.repository

import androidx.lifecycle.LiveData
import com.app.examschedulerapp.data.Examdata

interface ExamRepositoryInterface {
    fun saveExamData(examData: Examdata,
                     onSuccess: () -> Unit,
                     onFailure: (String) -> Unit
    )
    fun getExamData(studentId: String): LiveData<List<Examdata>>
    fun getPendingExamData(callback: (List<Examdata>) -> Unit)
    fun updateStatus(item: Examdata, status: String, callback: (Boolean) -> Unit)
    fun getExams(callback: (List<Examdata>) -> Unit)
}


