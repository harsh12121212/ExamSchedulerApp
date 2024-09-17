package com.app.examschedulerapp.student.studentView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.app.examschedulerapp.view.MainActivity
import com.app.examschedulerapp.R
import com.app.examschedulerapp.databinding.ActivityStudentMainBinding
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
            when (it.itemId) {
                R.id.stud_seatbook -> replaceFragment(StudentExamSeatBookingFragment())
                R.id.stud_status -> replaceFragment(StudentStatusFragment())
                R.id.stud_profile -> replaceFragment(StudentProfileFragment())
            }
            true
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_cust, fragment)
        fragmentTransaction.commit()
    }
}
