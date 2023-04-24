package com.app.examschedulerapp

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.data.student
import com.app.examschedulerapp.databinding.FragmentAdminBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminFragment : Fragment() {

    private lateinit var binding: FragmentAdminBinding
    private lateinit var user: FirebaseAuth
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference : DatabaseReference = database.getReference("Student")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        user = FirebaseAuth.getInstance()
        retrieveDataFromDatabase()

        return binding.root
    }

    fun retrieveDataFromDatabase() {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (eachUser in snapshot.children) {
                    val user = eachUser.getValue(student::class.java)
                    val exam = eachUser.getValue(examdata::class.java)

                    if (user != null) {
                        println("Name : ${user.name}")
                        println("Email : ${user.email}")
                        println("****************************")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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
                                user.signOut()
                                showSnackBar( "Successfully Logging out! ")
                                findNavController().navigate(R.id.action_adminFragment_to_loginFragment)
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