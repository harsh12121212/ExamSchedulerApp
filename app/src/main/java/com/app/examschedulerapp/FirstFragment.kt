package com.app.examschedulerapp

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.data.Centre
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.DBConstants.APPLICATION
import com.app.examschedulerapp.data.LoggedInUser
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var user: FirebaseAuth

    private lateinit var dbRef: DatabaseReference
    private var stud_city = ""
    private var stud_centre = ""
    private var stud_slot = ""
    private var stud_examdate = ""
    val list = ArrayList<Centre>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false)
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
                        it1.getValue(Centre::class.java)?.let { list.add(it) }
//                        it1.children.forEach {
//                            Log.e("TAG", "onDataChange: "+it )
//                        }
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
    }

    private fun setCityData() {
        //Location spinner
        val cityList = kotlin.collections.ArrayList<String>()
        list.forEach { cityList.add(it.name) }
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
        for (i in 1..(list[pos].centre)) {
            centreList.add("Centre $i")
        }
        val centrearrayAdapter =
            activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, centreList) }
        binding.spCenter.adapter = centrearrayAdapter
        binding.spCenter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                setSlotData(pos)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setSlotData(pos: Int) {
        val slotList = kotlin.collections.ArrayList<String>()
        for (i in 1..(list[pos].slot)) {
            slotList.add("Slot $i")
        }
        //Slot spinner
        val slotarrayAdapter = activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, slotList) }
        binding.spSlot.setSelection(0)
        binding.spSlot.adapter = slotarrayAdapter
    }

    private fun saveData() {
        stud_city = binding.spCity.selectedItem.toString().trim()
        stud_centre = binding.spCenter.selectedItem.toString().trim()
        stud_slot = binding.spSlot.selectedItem.toString().trim()
        stud_examdate = binding.etStudExamdate.text.toString().trim()


        if (stud_examdate.isEmpty() || stud_examdate.isBlank()) {
            showSnackBar("Enter date please")
        } else {
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
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                builder.setMessage("Do you want to Logout?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
            }
        }
        return true
    }

    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}