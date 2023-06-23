package com.app.examschedulerapp.repository

import androidx.lifecycle.MutableLiveData
import com.app.examschedulerapp.Student.studentModel.student

interface UserRepositoryInterface {
    fun saveStudentData(studentData: student, onCompleteListener: (Boolean) -> Unit)
    fun fetchStudentData(userId: String, studentData: MutableLiveData<student>)
}