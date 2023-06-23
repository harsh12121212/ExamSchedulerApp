package com.app.examschedulerapp.Student.studentView

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.examschedulerapp.R
import com.app.examschedulerapp.Student.studentViewModel.StudentExamSeatBookingViewModel
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
    private lateinit var viewModel: StudentExamSeatBookingViewModel

    var slotlist = arrayOf("Select Slot", "Slot 1", "Slot 2")
    var citylist = arrayOf("Select City", "Banglore", "Hyderabad", "Chennai")

    private var selectedCity: String = ""
    val banglorecentres: MutableList<String> = ArrayList()
    val hyderabadcentres: MutableList<String> = ArrayList()
    val chennaicentres: MutableList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = StudentExamSeatBookingBinding.inflate(inflater, container, false)
        user = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[StudentExamSeatBookingViewModel::class.java]

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

        //code for spinners start here
        FirebaseDatabase.getInstance().getReference("CenterData").child("Banglore")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (ds in p0.children) {
                    val key = ds.getKey()
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
                        val key = ds.getKey()
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
                        val key = ds.getKey()
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

    private fun saveData() {
        val selectedItemstud_city = binding.spCity.selectedItem
        val selectedItemstud_centre = binding.spCenter.selectedItem
        val selectedItemstud_slot= binding.spSlot.selectedItem
        val stud_city = selectedItemstud_city?.toString()?.trim() ?: ""
        val stud_centre = selectedItemstud_centre?.toString()?.trim() ?: ""
        val stud_slot = selectedItemstud_slot?.toString()?.trim() ?: ""
        val stud_examdate = binding.etStudExamdate?.text?.toString()?.trim() ?: ""

        if (stud_examdate.isEmpty()) {
            showSnackBar("Enter date please")
        } else {
            val examData = examdata(
                stud_city,
                stud_centre,
                stud_slot,
                stud_examdate,
                studentName = LoggedInUser.student.name.orEmpty(),
                studentEmailId = LoggedInUser.student.email.orEmpty(),
                studentId = LoggedInUser.student.uid.orEmpty(),
                countid = 0
            )

            viewModel.saveExamData(
                selectedCity = stud_city,
                selectedCenter = stud_centre,
                selectedSlot = stud_slot,
                selectedExamDate = stud_examdate,
                onSuccess = {
                    binding.progressbar.visibility = View.VISIBLE
                    viewModel.saveSeatData(
                        examData,
                        onSuccess = {
                            binding.progressbar.visibility = View.GONE
                            showSnackBar("Application submitted")
                            this.fragmentManager?.popBackStack()
                        },
                        onFailure = { errorMessage ->
                            binding.progressbar.visibility = View.GONE
                            showSnackBar(errorMessage)
                        }
                    )
                },
                onFailure = { errorMessage ->
                    showSnackBar(errorMessage)
                }
            )
        }
    }
    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}