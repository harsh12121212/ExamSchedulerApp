package com.app.examschedulerapp.repository

import androidx.lifecycle.LiveData
import com.app.examschedulerapp.data.examdata

interface ExamRepositoryInterface {
    fun saveExamData(examData: examdata,
                     onSuccess: () -> Unit,
                     onFailure: (String) -> Unit
    )
    fun getExamData(studentId: String): LiveData<List<examdata>>
    fun getPendingExamData(callback: (List<examdata>) -> Unit)
    fun updateStatus(item: examdata, status: String, callback: (Boolean) -> Unit)
}


