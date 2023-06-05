package com.app.examschedulerapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.databinding.ActivityStudentMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class StudentMainActivity : AppCompatActivity() {

    lateinit var binding : ActivityStudentMainBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentMainBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()

        setContentView(binding.root)

        val bnv = binding.btmnvCust
        replaceFragment(StudentExamSeatBookingFragment())

        bnv.setOnItemSelectedListener {
            val id = it.itemId
            when (id) {
                R.id.stud_seatbook -> replaceFragment(StudentExamSeatBookingFragment())
                R.id.stud_status -> replaceFragment(StudentStatusFragment())
                R.id.stud_profile -> replaceFragment(StudentProfileFragment())
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_cust, fragment)
        fragmentTransaction.commit()

    }
}
