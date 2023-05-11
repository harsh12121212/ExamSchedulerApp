package com.app.examschedulerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.app.examschedulerapp.databinding.ActivityStudentMainBinding

class StudentMainActivity : AppCompatActivity() {

    lateinit var binding : ActivityStudentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val bnv = binding.btmnvCust
        replaceFragment(StudentExamSeatBookingFragment())

        bnv.setOnItemSelectedListener {
            val id = it.itemId
            when (id) {
                R.id.stud_seatbook -> replaceFragment(StudentExamSeatBookingFragment())
                R.id.stud_status -> replaceFragment(StudentStatusFragment())
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
