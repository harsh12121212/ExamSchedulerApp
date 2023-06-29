package com.app.examschedulerapp.repository

import androidx.lifecycle.MutableLiveData
import com.app.examschedulerapp.student.studentModel.Student
import com.app.examschedulerapp.admin.adminmodel.Admin

interface UserRepositoryInterface {
    fun saveStudentData(studentData: Student, onCompleteListener: (Boolean) -> Unit)
    fun fetchStudentData(userId: String, studentData: MutableLiveData<Student>)
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
    fun fetchAdminData(userId: String, adminData: MutableLiveData<Admin>)
}