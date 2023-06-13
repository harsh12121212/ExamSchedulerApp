package com.app.examschedulerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.examschedulerapp.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val bnv = binding.btmnvAdmin
        replaceFragment(AdminDashboardFragment())

        bnv.setOnItemSelectedListener {
            val id = it.itemId
            when (id) {
                R.id.admin_dashboard -> replaceFragment(AdminDashboardFragment())
                R.id.admin_profile -> replaceFragment(AdminProfileFragment())
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_admin, fragment)
        fragmentTransaction.commit()

    }
}