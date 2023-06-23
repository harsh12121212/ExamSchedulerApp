package com.app.examschedulerapp.repository

import com.app.examschedulerapp.data.examdata

interface ExamRepositoryInterface {
    fun saveExamData(examData: examdata,
                     onSuccess: () -> Unit,
                     onFailure: (String) -> Unit
    )
}