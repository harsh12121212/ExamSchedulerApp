package com.app.examschedulerapp.data

import com.app.examschedulerapp.student.studentModel.Student
import com.app.examschedulerapp.admin.adminmodel.Admin

object LoggedInUser {
    var student: Student = Student()
    var admin: Admin = Admin()
}