package com.app.examschedulerapp

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.data.*
import com.app.examschedulerapp.data.DBConstants.APPLICATION
import com.app.examschedulerapp.databinding.StudentExamSeatBookingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class StudentExamSeatBookingFragment : Fragment() {

    private lateinit var binding: StudentExamSeatBookingBinding
    private lateinit var user: FirebaseAuth

    var slotlist = arrayOf("Select Slot", "Slot 1", "Slot 2")

    private lateinit var dbRef: DatabaseReference
    private var stud_city = ""
    private var stud_centre = ""
    private var stud_slot = ""
    private var stud_examdate = ""
    val list = ArrayList<City>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = StudentExamSeatBookingBinding.inflate(inflater, container, false)
        user = FirebaseAuth.getInstance()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference(
            DBConstants.CITY
        ).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                binding.progressbar.visibility = View.GONE
                try {
                    list.clear()
                    p0.children.forEach { it1 ->
                        it1.child("centre").getValue(City::class.java)?.let { list.add(it) }
                    }
                    setCityData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        // Exam date  Calender
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        // Button show to display date picker
        binding.etStudExamdate.setOnClickListener {
            val exmdt = DatePickerDialog(
                requireContext(), { datePicker, mYear, mMonth, mDay ->
                    binding.etStudExamdate.setText("$mDay/$mMonth/$mYear")
                }, year, month, day
            )
            exmdt.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
            //show dialogue
            exmdt.show()
        }

        binding.btnSearchseat.setOnClickListener {
            saveData()
        }

        val slotarrayAdapter = activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, slotlist) }
        binding.spSlot.setSelection(0)
        binding.spSlot.adapter = slotarrayAdapter
        binding.spSlot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setCityData() {
        //Location spinner
        val cityList = kotlin.collections.ArrayList<String>()
        list.forEach { cityList.add(it.Cityname!!) }
        val cityArrayAdapter = activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, cityList) }
        binding.spCity.adapter = cityArrayAdapter
        binding.spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                setCentreData(p2)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setCentreData(pos: Int) {
        //Centre spinner
        val centreList = kotlin.collections.ArrayList<String>()
        list.forEach { centreList.add(it.centre!!) }
        val centrearrayAdapter =
            activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, centreList) }
        binding.spCenter.adapter = centrearrayAdapter
        binding.spCenter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun saveData() {
        dbRef = FirebaseDatabase.getInstance().getReference(DBConstants.APPLICATION)
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid

        stud_city = binding.spCity.selectedItem.toString().trim()
        stud_centre = binding.spCenter.selectedItem.toString().trim()
        stud_slot = binding.spSlot.selectedItem.toString().trim()
        stud_examdate = binding.etStudExamdate.text.toString().trim()


        if (stud_examdate.isEmpty()) {
            showSnackBar("Enter date please")
        }else {
            binding.progressbar.visibility = View.VISIBLE
            dbRef = FirebaseDatabase.getInstance().getReference(APPLICATION)

            FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
                dbRef.child("$uid$stud_centre").setValue(
                    examdata(
                        stud_city,
                        stud_centre,
                        stud_slot,
                        stud_examdate,
                        studentName = LoggedInUser.student.name.orEmpty(),
                        studentEmailId = LoggedInUser.student.email.orEmpty(),
                        studentId = LoggedInUser.student.uid.orEmpty()
                    )
                ).addOnCompleteListener {
                    binding.progressbar.visibility = View.GONE
                    showSnackBar("Application submitted")
                    this.fragmentManager?.popBackStack()
                }.addOnFailureListener { err ->
                    binding.progressbar.visibility = View.GONE
                    showSnackBar("Error${err.message}")
                }
            }
        }
    }
//action bar menu code starts here
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
            }
            R.id.logout -> {
                val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            user.signOut();
                            showSnackBar("Successfully Logging out! ")
                            findNavController().navigate(R.id.action_firstFragment_to_loginFragment)
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            dialog.dismiss()
                        }
                    }
                }
//                harsh
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                builder.setMessage("Do you want to Logout?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
            }
        }
        return true
    }
//action bar menu code ends here

    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}


