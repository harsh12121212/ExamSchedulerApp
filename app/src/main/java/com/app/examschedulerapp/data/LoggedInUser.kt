package com.app.examschedulerapp.data

import com.app.examschedulerapp.Admin.adminModel.admin
import com.app.examschedulerapp.Student.studentModel.student

object LoggedInUser {
    var student: student = student()
    var admin: admin = admin()
}