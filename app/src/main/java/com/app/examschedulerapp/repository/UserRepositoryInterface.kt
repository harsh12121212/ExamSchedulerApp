package com.app.examschedulerapp.repository

import androidx.lifecycle.MutableLiveData
import com.app.examschedulerapp.Admin.adminModel.admin
import com.app.examschedulerapp.Student.studentModel.student

interface UserRepositoryInterface {
    fun saveStudentData(studentData: student, onCompleteListener: (Boolean) -> Unit)
    fun fetchStudentData(userId: String, studentData: MutableLiveData<student>)
    fun createAdmin(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun saveAdminData(
        name: String,
        email: String,
        city: String,
        centre: String,
        firstslot: String,
        secondslot: String,
        password: String,
        type: String,
        uid: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
    fun fetchAdminData(userId: String, adminData: MutableLiveData<admin>)
}