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
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var user: FirebaseAuth
    var citylist = arrayOf("Select City","Banglore", "Hyderabad", "Chennai")
    var centrelist = arrayOf("Select Centre","ABC", "DEF", "XYZ")
    var slotlist = arrayOf("Select Slot","Slot A", "Slot B")
    private lateinit var dbRef: DatabaseReference
    private var stud_city =""
    private var stud_centre=""
    private var stud_slot=""
    private var stud_examdate=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        user = FirebaseAuth.getInstance()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        //Location spinner
        val cityarrayAdapter =
            activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, citylist) }
        binding.spCity.setSelection(0)
        binding.spCity.adapter=cityarrayAdapter
        binding.spCity.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        //Slot spinner
        val slotarrayAdapter =
            activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, slotlist) }
        binding.spSlot.setSelection(0)
        binding.spSlot.adapter=slotarrayAdapter
        binding.spSlot.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        //Centre spinner
        val centrearrayAdapter =
            activity?.let { ArrayAdapter(it, R.layout.spinnerlayout, centrelist) }
        binding.spCenter.setSelection(0)
        binding.spCenter.adapter=centrearrayAdapter
        binding.spCenter.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // Exam date  Calender
        val c= Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DAY_OF_MONTH)
        // Button show to display date picker
        binding.etStudExamdate.setOnClickListener {
            val exmdt = DatePickerDialog(requireContext(),
                { datePicker, mYear, mMonth, mDay ->
                    binding.etStudExamdate.setText("$mDay/$mMonth/$mYear")
                },year,month,day)
            exmdt.getDatePicker().setMinDate(System.currentTimeMillis()-1000)
            //show dialogue
            exmdt.show()
        }

        binding.btnSearchseat.setOnClickListener{
            saveData()
        }
    }

    private fun saveData() {

        stud_city=binding.spCity.selectedItem.toString().trim()
        stud_centre=binding.spCenter.selectedItem.toString().trim()
        stud_slot=binding.spSlot.selectedItem.toString().trim()
        stud_examdate=binding.etStudExamdate.text.toString().trim()
        dbRef = FirebaseDatabase.getInstance().getReference("Student")
        var userId = FirebaseAuth.getInstance().currentUser?.uid

        val exmdata = examdata(stud_city, stud_centre, stud_slot, stud_examdate)
        if (userId != null) {
            dbRef.child(userId).child(stud_centre).setValue(exmdata)
                .addOnCompleteListener {
                    showSnackBar("Data is saved")
                }.addOnFailureListener { err ->
                    showSnackBar("Error${err.message}")
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile ->{
            }
            R.id.logout -> {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                user.signOut();
                                showSnackBar( "Successfully Logging out! ")
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
                    .setNegativeButton("No", dialogClickListener)
                    .show()
            }
        }
        return true
    }

    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}