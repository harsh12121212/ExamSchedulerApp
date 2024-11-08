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
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.DBConstants.APPLICATION
import com.app.examschedulerapp.data.LoggedInUser
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.databinding.StudentExamSeatBookingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class StudentExamSeatBookingFragment : Fragment() {

    private lateinit var binding: StudentExamSeatBookingBinding
    private lateinit var user: FirebaseAuth

    private var slotlist = arrayOf("Select Slot", "Slot 1", "Slot 2")
    private var citylist = arrayOf("Select City", "Banglore", "Hyderabad", "Chennai")

    private lateinit var dbRef: DatabaseReference
    private var studCity = ""
    private var studCentre = ""
    private var studSlot = ""
    private var studExamdate = ""
    private var selectedCity: String = ""
    private var countId = 0
    val banglorecentres: MutableList<String> = ArrayList()
    val hyderabadcentres: MutableList<String> = ArrayList()
    val chennaicentres: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = StudentExamSeatBookingBinding.inflate(inflater, container, false)
        user = FirebaseAuth.getInstance()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

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
            exmdt.datePicker.minDate = System.currentTimeMillis() - 1000
            //show dialogue
            exmdt.show()
        }

        binding.btnSearchseat.setOnClickListener {
            saveData()
        }

        //code for spinners start here
        FirebaseDatabase.getInstance().getReference("CenterData").child("Banglore")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (ds in p0.children) {
                    val key = ds.key
                    if (key != null) {
                        banglorecentres.add(key)
                    }
                    Log.d("examform","Centre name : $key")
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        FirebaseDatabase.getInstance().getReference("CenterData").child("Hyderabad")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    for (ds in p0.children) {
                        val key = ds.key
                        if (key != null) {
                            hyderabadcentres.add(key)
                        }
                        Log.d("examform","Centre name : $key")
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })

        FirebaseDatabase.getInstance().getReference("CenterData").child("Chennai")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    for (ds in p0.children) {
                        val key = ds.key
                        if (key != null) {
                            chennaicentres.add(key)
                        }
                        Log.d("examform","Centre name : $key")
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })

        val slotarrayAdapter = activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, slotlist) }
        binding.spSlot.setSelection(0)
        binding.spSlot.adapter = slotarrayAdapter
        binding.spSlot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                showSnackBar("Select slot ")
            }
        }

        val cityarrayAdapter = activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, citylist) }
        binding.spCity.setSelection(0)
        binding.spCity.adapter = cityarrayAdapter
        binding.spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    1 -> setCentreData(banglorecentres)
                    2 -> setCentreData(hyderabadcentres)
                    3 -> setCentreData(chennaicentres)
                }
                selectedCity = citylist[p2]

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                showSnackBar("Select city ")
            }
        }

    }

    private fun setCentreData(listname: MutableList<String>) {
        //Centre spinner
        val centerarrayAdapter = activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, listname) }
        binding.spCenter.setSelection(0)
        binding.spCenter.adapter = centerarrayAdapter
        binding.spCenter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
    //code for spinners start here

    private fun saveData() {
        dbRef = FirebaseDatabase.getInstance().getReference(APPLICATION)

        dbRef = FirebaseDatabase.getInstance().getReference(APPLICATION)
        dbRef.orderByChild("studentId").equalTo(LoggedInUser.student.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    countId = dataSnapshot.childrenCount.toInt()
                    if (countId >= 2) {
                        showSnackBar("You have already booked a seat twice.")
                    } else {
                        checkCityAvailability()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun checkCityAvailability() {
        dbRef.orderByChild("sf_city").equalTo(selectedCity)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        showSnackBar("You have already submitted set booking for this City")
                    } else {
                        saveSeatData()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun saveSeatData() {
        studCity = binding.spCity.selectedItem.toString().trim()
        studCentre = binding.spCenter.selectedItem.toString().trim()
        studSlot = binding.spSlot.selectedItem.toString().trim()
        studExamdate = binding.etStudExamdate.text.toString().trim()

        if (studExamdate.isEmpty()) {
            showSnackBar("Enter date please")
        } else {
            binding.progressbar.visibility = View.VISIBLE
            dbRef.push().setValue(
                examdata(
                    studCity,
                    studCentre,
                    studSlot,
                    studExamdate,
                    studentName = LoggedInUser.student.name.orEmpty(),
                    studentEmailId = LoggedInUser.student.email.orEmpty(),
                    studentId = LoggedInUser.student.uid.orEmpty(),
                    countid = countId + 1
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

//action bar menu code starts here
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            user.signOut()
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


