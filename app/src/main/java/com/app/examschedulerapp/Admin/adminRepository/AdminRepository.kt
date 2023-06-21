package com.app.examschedulerapp.Admin.adminRepository

import com.app.examschedulerapp.Admin.adminModel.admin

interface AdminRepository {
    fun getAdminProfile(userId: String, callback: (admin: admin?) -> Unit)
}